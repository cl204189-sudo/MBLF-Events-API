package com.example.demoApiVersao23082026.service;

import com.example.demoApiVersao23082026.dto.InscricaoDTO;
import com.example.demoApiVersao23082026.dto.InscricaoRequestDTO;
import com.example.demoApiVersao23082026.exception.BadRequestException;
import com.example.demoApiVersao23082026.exception.NotFoundException;
import com.example.demoApiVersao23082026.model.Evento;
import com.example.demoApiVersao23082026.model.EventoColaborador;
import com.example.demoApiVersao23082026.model.Inscricao;
import com.example.demoApiVersao23082026.model.Notificacao;
import com.example.demoApiVersao23082026.model.Usuario;
import com.example.demoApiVersao23082026.repository.EventoColaboradorRepository;
import com.example.demoApiVersao23082026.repository.EventoRepository;
import com.example.demoApiVersao23082026.repository.InscricaoRepository;
import com.example.demoApiVersao23082026.repository.NotificacaoRepository;
import com.example.demoApiVersao23082026.repository.UsuarioRepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Service
public class InscricaoService {

    @Autowired
    private InscricaoRepository repository;

    @Autowired
    private UsuarioRepository usuarioRepository;

    @Autowired
    private UsuarioAutenticadoService usuarioAutenticadoService;

    @Autowired
    private EventoColaboradorRepository colaboradorRepository;

    @Autowired
    private EventoRepository eventoRepository;

    @Autowired
    private UsuarioNomeService usuarioNomeService;

    @Autowired
    private NotificacaoRepository notificacaoRepository;

    @Transactional
    public InscricaoDTO criar(
            InscricaoRequestDTO dto) {

        Usuario usuario =
                usuarioAutenticadoService
                        .getUsuarioAutenticado();

        Evento evento = eventoRepository
                .buscarParaAtualizacao(dto.getEventoId())
                .orElseThrow(() ->
                        new NotFoundException(
                                "Evento não encontrado"
                        ));

        validarInscricao(usuario, evento);

        Inscricao inscricao =
                new Inscricao();

        inscricao.setUsuario(usuario);
        inscricao.setEvento(evento);

        inscricao.setStatus(
                Inscricao.Status.INSCRITO
        );

        inscricao.setTokenQr(
                UUID.randomUUID().toString()
        );

        if (evento.getVagasDisponiveis() != null) {

            evento.setVagasDisponiveis(
                    evento.getVagasDisponiveis() - 1
            );

            eventoRepository.save(evento);
        }

        Inscricao salva = repository.save(inscricao);

        criarNotificacao(usuario, evento, Notificacao.Tipo.INSCRICAO_CONFIRMADA,
                "Inscrição confirmada",
                "Sua inscrição no evento \"" + evento.getTitulo() + "\" foi registrada com sucesso.");

        if (evento.getOrganizador() != null) {
            criarNotificacao(evento.getOrganizador(), evento, Notificacao.Tipo.NOVA_INSCRICAO,
                    "Nova inscrição",
                    usuarioNomeService.obterNome(usuario) + " se inscreveu no evento \""
                            + evento.getTitulo() + "\".");
        }

        return converterParaDTO(salva);
    }

    public List<InscricaoDTO> minhasInscricoes() {

        Usuario usuario =
                usuarioAutenticadoService
                        .getUsuarioAutenticado();

        return repository
                .findByUsuarioId(usuario.getId())
                .stream()
                .map(this::converterParaDTO)
                .toList();
    }

    public InscricaoDTO buscar(Integer id) {

        Inscricao inscricao = repository
                .findById(id)
                .orElseThrow(() ->
                        new NotFoundException(
                                "Inscrição não encontrada"
                        ));

        return converterParaDTO(inscricao);
    }

    public List<InscricaoDTO> listarPorUsuario(
            Integer usuarioId) {

        return repository
                .findByUsuarioId(usuarioId)
                .stream()
                .map(this::converterParaDTO)
                .toList();
    }

    public List<InscricaoDTO> listarPorEvento(
            Integer eventoId) {

        Usuario usuario =
                usuarioAutenticadoService
                        .getUsuarioAutenticado();

        Evento evento = eventoRepository
                .findById(eventoId)
                .orElseThrow(() ->
                        new NotFoundException(
                                "Evento não encontrado"
                        ));

        boolean organizador =
                evento.getOrganizador()
                        .getId()
                        .equals(usuario.getId());

        if (!organizador) {

            EventoColaborador colaborador =
                    colaboradorRepository
                            .findByEventoIdAndUsuarioId(
                                    eventoId,
                                    usuario.getId()
                            )
                            .orElseThrow(() ->
                                    new BadRequestException(
                                            "Usuário não possui permissão para visualizar inscritos"
                                    ));

            if (colaborador.getStatus()
                    != EventoColaborador.Status.ACEITO) {

                throw new BadRequestException(
                        "Usuário não é colaborador ativo"
                );
            }

            if (!Boolean.TRUE.equals(
                    colaborador.getPodeVerInscritos())) {

                throw new BadRequestException(
                        "Este colaborador não possui permissão para visualizar inscritos"
                );
            }
        }

        return repository
                .findByEventoId(eventoId)
                .stream()
                .map(this::converterParaDTO)
                .toList();
    }

    @Transactional
    public InscricaoDTO cancelar(
            Integer inscricaoId) {

        Usuario usuario =
                usuarioAutenticadoService
                        .getUsuarioAutenticado();

        // Bloqueia o evento primeiro, mantendo a mesma
        // ordem de locks utilizada no cadastro.
        Inscricao referencia = repository.findById(inscricaoId)
                .orElseThrow(() ->
                        new NotFoundException(
                                "Inscrição não encontrada"
                        ));

        eventoRepository.buscarParaAtualizacao(
                referencia.getEvento().getId()
        ).orElseThrow(() ->
                new NotFoundException(
                        "Evento não encontrado"
                ));

        Inscricao inscricao = repository
                .findById(inscricaoId)
                .orElseThrow(() ->
                        new NotFoundException(
                                "Inscrição não encontrada"
                        ));

        if (!inscricao.getUsuario()
                .getId()
                .equals(usuario.getId())) {

            throw new BadRequestException(
                    "Esta inscrição não pertence ao usuário autenticado"
            );
        }

        if (inscricao.getStatus()
                == Inscricao.Status.CANCELADO) {

            throw new BadRequestException(
                    "Esta inscrição já está cancelada"
            );
        }

        if (inscricao.getStatus()
                == Inscricao.Status.PRESENTE) {

            throw new BadRequestException(
                    "Não é possível cancelar uma inscrição com presença registrada"
            );
        }

        inscricao.setStatus(
                Inscricao.Status.CANCELADO
        );

        inscricao.setDataCancelamento(
                LocalDateTime.now()
        );

        Evento evento =
                inscricao.getEvento();

        if (evento.getVagasDisponiveis() != null) {

            evento.setVagasDisponiveis(
                    evento.getVagasDisponiveis() + 1
            );

            eventoRepository.save(evento);
        }

        return converterParaDTO(
                repository.save(inscricao)
        );
    }

    private void criarNotificacao(Usuario destinatario, Evento evento,
                                  Notificacao.Tipo tipo, String titulo, String mensagem) {
        Notificacao n = new Notificacao();
        n.setUsuario(destinatario);
        n.setEvento(evento);
        n.setTipo(tipo);
        n.setTitulo(titulo);
        n.setMensagem(mensagem);
        n.setLida(false);
        notificacaoRepository.save(n);
    }

    private void validarInscricao(
            Usuario usuario,
            Evento evento) {

        if (evento.getOrganizador()
                .getId()
                .equals(usuario.getId())) {

            throw new BadRequestException(
                    "O organizador não pode se inscrever no próprio evento"
            );
        }

        if (evento.getDataFim() != null &&
                !evento.getDataFim().isAfter(LocalDateTime.now())) {

            throw new BadRequestException(
                    "Este evento já terminou"
            );
        }

        if (evento.getStatus()
                != Evento.Status.PUBLICADO) {

            throw new BadRequestException(
                    "Este evento não está publicado"
            );
        }

        if (!Boolean.TRUE.equals(
                evento.getInscricoesAbertas())) {

            throw new BadRequestException(
                    "As inscrições deste evento estão fechadas"
            );
        }

        if (evento.getPublicacaoAte() == null ||
                evento.getPublicacaoAte()
                        .isBefore(LocalDateTime.now())) {

            throw new BadRequestException(
                    "A publicação deste evento expirou"
            );
        }

        if (repository
                .existsByUsuarioIdAndEventoId(
                        usuario.getId(),
                        evento.getId()
                )) {

            throw new BadRequestException(
                    "Você já possui inscrição neste evento"
            );
        }

        if (evento.getVagasDisponiveis() != null &&
                evento.getVagasDisponiveis() <= 0) {

            throw new BadRequestException(
                    "Não há vagas disponíveis"
            );
        }
    }

    private InscricaoDTO converterParaDTO(
            Inscricao inscricao) {

        return new InscricaoDTO(
                inscricao.getId(),

                inscricao.getUsuario().getId(),
                usuarioNomeService.obterNome(
                        inscricao.getUsuario()
                ),

                inscricao.getEvento().getId(),
                inscricao.getEvento().getTitulo(),

                inscricao.getDataInscricao(),

                inscricao.getStatus().name(),

                inscricao.getTokenQr(),
                inscricao.getDataCancelamento()
        );
    }
}
