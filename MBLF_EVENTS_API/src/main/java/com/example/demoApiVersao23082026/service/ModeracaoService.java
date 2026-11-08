package com.example.demoApiVersao23082026.service;

import com.example.demoApiVersao23082026.dto.ModeracaoDTO;
import com.example.demoApiVersao23082026.exception.BadRequestException;
import com.example.demoApiVersao23082026.exception.NotFoundException;

import com.example.demoApiVersao23082026.model.Evento;
import com.example.demoApiVersao23082026.model.EventoColaborador;
import com.example.demoApiVersao23082026.model.Moderacao;
import com.example.demoApiVersao23082026.model.Usuario;

import com.example.demoApiVersao23082026.repository.EventoColaboradorRepository;
import com.example.demoApiVersao23082026.repository.EventoRepository;
import com.example.demoApiVersao23082026.repository.ModeracaoRepository;
import com.example.demoApiVersao23082026.repository.UsuarioRepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class ModeracaoService {

    @Autowired
    private ModeracaoRepository repository;

    @Autowired
    private EventoRepository eventoRepository;

    @Autowired
    private UsuarioAutenticadoService usuarioAutenticadoService;

    @Autowired
    private UsuarioRepository usuarioRepository;

    @Autowired
    private EventoColaboradorRepository colaboradorRepository;

    @Autowired
    private UsuarioNomeService usuarioNomeService;

    @Transactional
    public ModeracaoDTO silenciar(
            Integer eventoId,
            Integer alvoId,
            String motivo) {

        Evento evento = buscarEvento(eventoId);

        Usuario executor = usuarioAutenticadoService
                .getUsuarioAutenticado();

        Usuario alvo = buscarUsuario(alvoId);

        validarAlvoNaoOrganizador(evento, alvo);

        EventoColaborador alvoColaborador =
                buscarColaboradorAceito(eventoId, alvoId);

        validarPodeSilenciar(evento, executor.getId());

        alvoColaborador.setSilenciado(true);
        alvoColaborador.setSilenciadoAte(null);

        colaboradorRepository.save(alvoColaborador);

        return registrar(
                evento,
                executor,
                alvo,
                Moderacao.Acao.SILENCIAR,
                motivo
        );
    }

    @Transactional
    public ModeracaoDTO dessilenciar(
            Integer eventoId,
            Integer alvoId) {

        Evento evento = buscarEvento(eventoId);

        Usuario executor = usuarioAutenticadoService
                .getUsuarioAutenticado();

        Usuario alvo = buscarUsuario(alvoId);

        validarAlvoNaoOrganizador(evento, alvo);

        EventoColaborador alvoColaborador =
                buscarColaboradorAceito(eventoId, alvoId);

        validarPodeSilenciar(evento, executor.getId());

        alvoColaborador.setSilenciado(false);
        alvoColaborador.setSilenciadoAte(null);

        colaboradorRepository.save(alvoColaborador);

        return registrar(
                evento,
                executor,
                alvo,
                Moderacao.Acao.DESSILENCIAR,
                null
        );
    }

    @Transactional
    public ModeracaoDTO expulsar(
            Integer eventoId,
            Integer alvoId,
            String motivo) {

        Evento evento = buscarEvento(eventoId);

        Usuario executor = usuarioAutenticadoService
                .getUsuarioAutenticado();

        Usuario alvo = buscarUsuario(alvoId);

        validarAlvoNaoOrganizador(evento, alvo);

        EventoColaborador alvoColaborador =
                buscarColaboradorAceito(eventoId, alvoId);

        validarPodeExpulsar(evento, executor.getId());

        alvoColaborador.setStatus(
                EventoColaborador.Status.EXPULSO
        );

        alvoColaborador.setSilenciado(true);

        colaboradorRepository.save(alvoColaborador);

        return registrar(
                evento,
                executor,
                alvo,
                Moderacao.Acao.EXPULSAR,
                motivo
        );
    }

    @Transactional
    public ModeracaoDTO silenciarTodos(Integer eventoId) {

        Evento evento = buscarEvento(eventoId);

        Usuario executor = usuarioAutenticadoService
                .getUsuarioAutenticado();

        validarOrganizador(evento, executor.getId());

        evento.setChatSomenteOrganizador(true);

        eventoRepository.save(evento);

        return registrar(
                evento,
                executor,
                null,
                Moderacao.Acao.SILENCIAR_TODOS,
                null
        );
    }

    @Transactional
    public ModeracaoDTO liberarTodos(Integer eventoId) {

        Evento evento = buscarEvento(eventoId);

        Usuario executor = usuarioAutenticadoService
                .getUsuarioAutenticado();

        validarOrganizador(evento, executor.getId());

        evento.setChatSomenteOrganizador(false);

        eventoRepository.save(evento);

        return registrar(
                evento,
                executor,
                null,
                Moderacao.Acao.LIBERAR_TODOS,
                null
        );
    }

    @Transactional(readOnly = true)
    public List<ModeracaoDTO> listarHistorico(
            Integer eventoId) {

        Evento evento = buscarEvento(eventoId);

        Usuario usuario = usuarioAutenticadoService
                .getUsuarioAutenticado();

        boolean organizador = evento.getOrganizador()
                .getId()
                .equals(usuario.getId());

        if (!organizador) {

            EventoColaborador colaborador =
                    buscarColaboradorAceito(
                            eventoId,
                            usuario.getId()
                    );

            boolean podeSilenciar = Boolean.TRUE.equals(
                    colaborador.getPodeSilenciar()
            );

            boolean podeExpulsar = Boolean.TRUE.equals(
                    colaborador.getPodeExpulsar()
            );

            if (!podeSilenciar && !podeExpulsar) {
                throw new BadRequestException(
                        "Usuário não possui permissão para visualizar o histórico de moderação"
                );
            }
        }

        return repository
                .findByEventoIdOrderByDataAcaoDesc(eventoId)
                .stream()
                .map(this::converterParaDTO)
                .toList();
    }

    private void validarPodeSilenciar(
            Evento evento,
            Integer executorId) {

        if (evento.getOrganizador()
                .getId()
                .equals(executorId)) {

            return;
        }

        EventoColaborador executor =
                buscarColaboradorAceito(
                        evento.getId(),
                        executorId
                );

        if (!Boolean.TRUE.equals(
                executor.getPodeSilenciar())) {

            throw new BadRequestException(
                    "Este colaborador não possui permissão para silenciar"
            );
        }
    }

    private void validarPodeExpulsar(
            Evento evento,
            Integer executorId) {

        if (evento.getOrganizador()
                .getId()
                .equals(executorId)) {

            return;
        }

        EventoColaborador executor =
                buscarColaboradorAceito(
                        evento.getId(),
                        executorId
                );

        if (!Boolean.TRUE.equals(
                executor.getPodeExpulsar())) {

            throw new BadRequestException(
                    "Este colaborador não possui permissão para expulsar"
            );
        }
    }

    private void validarOrganizador(
            Evento evento,
            Integer usuarioId) {

        if (!evento.getOrganizador()
                .getId()
                .equals(usuarioId)) {

            throw new BadRequestException(
                    "Somente o organizador pode realizar esta ação"
            );
        }
    }

    private void validarAlvoNaoOrganizador(
            Evento evento,
            Usuario alvo) {

        if (evento.getOrganizador()
                .getId()
                .equals(alvo.getId())) {

            throw new BadRequestException(
                    "O organizador do evento não pode ser moderado"
            );
        }
    }

    private EventoColaborador buscarColaboradorAceito(
            Integer eventoId,
            Integer usuarioId) {

        EventoColaborador colaborador = colaboradorRepository
                .findByEventoIdAndUsuarioId(
                        eventoId,
                        usuarioId
                )
                .orElseThrow(() ->
                        new NotFoundException(
                                "Colaborador não encontrado"
                        )
                );

        if (colaborador.getStatus()
                != EventoColaborador.Status.ACEITO) {

            throw new BadRequestException(
                    "O usuário não é um colaborador ativo deste evento"
            );
        }

        return colaborador;
    }

    private Evento buscarEvento(Integer id) {

        return eventoRepository
                .findById(id)
                .orElseThrow(() ->
                        new NotFoundException(
                                "Evento não encontrado"
                        )
                );
    }

    private Usuario buscarUsuario(Integer id) {

        return usuarioRepository
                .findById(id)
                .orElseThrow(() ->
                        new NotFoundException(
                                "Usuário não encontrado"
                        )
                );
    }

    private ModeracaoDTO registrar(
            Evento evento,
            Usuario executor,
            Usuario alvo,
            Moderacao.Acao acao,
            String motivo) {

        Moderacao moderacao = new Moderacao();

        moderacao.setEvento(evento);
        moderacao.setExecutor(executor);
        moderacao.setAlvo(alvo);
        moderacao.setAcao(acao);
        moderacao.setMotivo(motivo);

        Moderacao salva = repository.save(moderacao);

        return converterParaDTO(salva);
    }

    private ModeracaoDTO converterParaDTO(
            Moderacao moderacao) {

        return new ModeracaoDTO(
                moderacao.getId(),

                moderacao.getEvento().getId(),
                moderacao.getEvento().getTitulo(),

                moderacao.getExecutor().getId(),
                usuarioNomeService.obterNome(
                        moderacao.getExecutor()
                ),

                moderacao.getAlvo() != null
                        ? moderacao.getAlvo().getId()
                        : null,

                moderacao.getAlvo() != null
                        ? usuarioNomeService.obterNome(
                        moderacao.getAlvo()
                )
                        : null,

                moderacao.getAcao().name(),

                moderacao.getMotivo(),

                moderacao.getDataAcao()
        );
    }
}
