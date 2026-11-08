package com.example.demoApiVersao23082026.service;

import com.example.demoApiVersao23082026.dto.MensagemDTO;
import com.example.demoApiVersao23082026.dto.MensagemRequestDTO;
import com.example.demoApiVersao23082026.exception.BadRequestException;
import com.example.demoApiVersao23082026.exception.NotFoundException;

import com.example.demoApiVersao23082026.model.Evento;
import com.example.demoApiVersao23082026.model.Inscricao;
import com.example.demoApiVersao23082026.model.EventoColaborador;
import com.example.demoApiVersao23082026.model.Mensagem;
import com.example.demoApiVersao23082026.model.Notificacao;
import com.example.demoApiVersao23082026.model.Usuario;

import com.example.demoApiVersao23082026.repository.NotificacaoRepository;
import com.example.demoApiVersao23082026.repository.EventoColaboradorRepository;
import com.example.demoApiVersao23082026.repository.EventoRepository;
import com.example.demoApiVersao23082026.repository.InscricaoRepository;
import com.example.demoApiVersao23082026.repository.MensagemRepository;
import com.example.demoApiVersao23082026.repository.UsuarioRepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Set;
import java.util.HashSet;

@Service
public class MensagemService {

    @Autowired
    private MensagemRepository repository;

    @Autowired
    private EventoRepository eventoRepository;

    @Autowired
    private UsuarioAutenticadoService usuarioAutenticadoService;

    @Autowired
    private UsuarioRepository usuarioRepository;

    @Autowired
    private EventoColaboradorRepository colaboradorRepository;

    @Autowired
    private InscricaoRepository inscricaoRepository;

    @Autowired
    private NotificacaoRepository notificacaoRepository;

    @Autowired
    private UsuarioNomeService usuarioNomeService;

    @Transactional
    public MensagemDTO enviar(
            Integer eventoId,
            MensagemRequestDTO dto) {

        return enviarCanal(eventoId, "ORGANIZACAO", dto);
    }

    @Transactional
    public MensagemDTO enviarCanal(
            Integer eventoId,
            String canal,
            MensagemRequestDTO dto) {

        Evento evento = eventoRepository
                .findById(eventoId)
                .orElseThrow(() ->
                        new NotFoundException(
                                "Evento não encontrado"
                        ));

        Usuario usuario = usuarioAutenticadoService
                .getUsuarioAutenticado();

        if (dto == null ||
                dto.getMensagem() == null ||
                dto.getMensagem().isBlank()) {

            throw new BadRequestException(
                    "A mensagem não pode estar vazia"
            );
        }

        validarHub(evento);
        validarCanal(canal);

        if ("GERAL".equals(canal)) {
            validarAcessoGeral(evento, usuario.getId());
        } else {
            validarEnvioEquipe(evento, usuario.getId());
        }

        Mensagem mensagem = new Mensagem();

        mensagem.setEvento(evento);
        mensagem.setUsuario(usuario);
        mensagem.setMensagem(dto.getMensagem().trim());
        mensagem.setCanal(canal);

        Mensagem salva = repository.save(mensagem);

        notificarDestinatarios(evento, usuario, canal);

        return converterParaDTO(salva);
    }

    private void notificarDestinatarios(
            Evento evento,
            Usuario autor,
            String canal) {

        Set<Integer> destinatarios = new HashSet<>();

        destinatarios.add(
                evento.getOrganizador().getId()
        );

        for (EventoColaborador colaborador :
                colaboradorRepository.findByEventoId(evento.getId())) {

            if (colaborador.getStatus()
                    == EventoColaborador.Status.ACEITO &&
                    Boolean.TRUE.equals(
                            colaborador.getPodeChat()
                    )) {

                destinatarios.add(
                        colaborador.getUsuario().getId()
                );
            }
        }

        if ("GERAL".equals(canal)) {

            for (Inscricao inscricao :
                    inscricaoRepository.findByEventoId(evento.getId())) {

                if (inscricao.getStatus()
                        == Inscricao.Status.INSCRITO ||
                        inscricao.getStatus()
                                == Inscricao.Status.PRESENTE) {

                    destinatarios.add(
                            inscricao.getUsuario().getId()
                    );
                }
            }
        }

        destinatarios.remove(autor.getId());

        String nomeAutor = usuarioNomeService.obterNome(autor);

        for (Integer id : destinatarios) {

            Notificacao aviso = new Notificacao();

            aviso.setUsuario(
                    usuarioRepository.getReferenceById(id)
            );

            aviso.setEvento(evento);

            aviso.setTipo(
                    "GERAL".equals(canal)
                            ? Notificacao.Tipo.CHAT_GERAL
                            : Notificacao.Tipo.CHAT_ORGANIZACAO
            );

            aviso.setTitulo(
                    "GERAL".equals(canal)
                            ? "Nova mensagem no chat geral"
                            : "Nova mensagem no chat da organização"
            );

            aviso.setMensagem(
                    nomeAutor + " enviou uma mensagem em "
                            + evento.getTitulo()
            );

            aviso.setLida(false);

            notificacaoRepository.save(aviso);
        }
    }

    public List<MensagemDTO> listarPorEvento(
            Integer eventoId) {

        return listarCanal(eventoId, "ORGANIZACAO");
    }

    @Transactional(readOnly = true)
    public List<MensagemDTO> listarCanal(
            Integer eventoId,
            String canal) {

        Evento evento = eventoRepository
                .findById(eventoId)
                .orElseThrow(() ->
                        new NotFoundException(
                                "Evento não encontrado"
                        ));

        validarHub(evento);
        validarCanal(canal);

        Usuario usuario = usuarioAutenticadoService
                .getUsuarioAutenticado();

        if ("GERAL".equals(canal)) {
            validarAcessoGeral(evento, usuario.getId());
        } else {
            validarAcessoAoChat(evento, usuario.getId());
        }

        return repository
                .findByEventoIdAndCanalOrderByDataEnvioAsc(
                        eventoId,
                        canal
                )
                .stream()
                .map(this::converterParaDTO)
                .toList();
    }

    private void validarCanal(String canal) {

        if (!"GERAL".equals(canal) &&
                !"ORGANIZACAO".equals(canal)) {

            throw new BadRequestException(
                    "Canal de chat inválido"
            );
        }
    }

    private void validarAcessoGeral(
            Evento evento,
            Integer usuarioId) {

        if (evento.getOrganizador()
                .getId()
                .equals(usuarioId)) {

            return;
        }

        EventoColaborador colaborador = colaboradorRepository
                .findByEventoIdAndUsuarioId(
                        evento.getId(),
                        usuarioId
                )
                .orElse(null);

        if (colaborador != null &&
                colaborador.getStatus()
                        == EventoColaborador.Status.ACEITO &&
                Boolean.TRUE.equals(
                        colaborador.getPodeChat()
                )) {

            return;
        }

        if (inscricaoRepository
                .existsByUsuarioIdAndEventoIdAndStatusIn(
                        usuarioId,
                        evento.getId(),
                        List.of(
                                Inscricao.Status.INSCRITO,
                                Inscricao.Status.PRESENTE
                        )
                )) {

            return;
        }

        throw new BadRequestException(
                "É necessária inscrição ativa para acessar o chat geral"
        );
    }

    private void validarEnvioEquipe(
            Evento evento,
            Integer usuarioId) {

        validarAcessoAoChat(evento, usuarioId);

        if (evento.getOrganizador()
                .getId()
                .equals(usuarioId)) {

            return;
        }

        EventoColaborador colaborador = colaboradorRepository
                .findByEventoIdAndUsuarioId(
                        evento.getId(),
                        usuarioId
                )
                .orElseThrow(() ->
                        new BadRequestException(
                                "Usuário não possui acesso ao chat deste evento"
                        ));

        if (Boolean.TRUE.equals(
                colaborador.getSilenciado()) ||
                (colaborador.getSilenciadoAte() != null &&
                        colaborador.getSilenciadoAte()
                                .isAfter(LocalDateTime.now()))) {

            throw new BadRequestException(
                    "Colaborador silenciado"
            );
        }

        if (Boolean.TRUE.equals(
                evento.getChatSomenteOrganizador())) {

            throw new BadRequestException(
                    "O chat está no modo somente organizador"
            );
        }
    }

    private void validarAcessoAoChat(
            Evento evento,
            Integer usuarioId) {

        if (evento.getOrganizador()
                .getId()
                .equals(usuarioId)) {

            return;
        }

        EventoColaborador colaborador = colaboradorRepository
                .findByEventoIdAndUsuarioId(
                        evento.getId(),
                        usuarioId
                )
                .orElseThrow(() ->
                        new BadRequestException(
                                "Usuário não possui acesso ao chat deste evento"
                        ));

        if (colaborador.getStatus()
                != EventoColaborador.Status.ACEITO) {

            throw new BadRequestException(
                    "Usuário não é colaborador ativo deste evento"
            );
        }

        if (!Boolean.TRUE.equals(
                colaborador.getPodeChat())) {

            throw new BadRequestException(
                    "Este colaborador não possui acesso ao chat"
            );
        }
    }

    private void validarHub(Evento evento) {

        if (evento.getStatus()
                == Evento.Status.CANCELADO ||
                evento.getStatus()
                        == Evento.Status.REMOVIDO_ADMIN) {

            throw new BadRequestException(
                    "Chat indisponível: evento cancelado ou removido"
            );
        }

        if (evento.getHubAte() == null ||
                evento.getHubAte()
                        .isBefore(LocalDateTime.now())) {

            throw new BadRequestException(
                    "O Hub deste evento está expirado"
            );
        }
    }

    private MensagemDTO converterParaDTO(
            Mensagem mensagem) {

        return new MensagemDTO(
                mensagem.getId(),

                mensagem.getEvento().getId(),

                mensagem.getUsuario().getId(),
                usuarioNomeService.obterNome(
                        mensagem.getUsuario()
                ),

                mensagem.getMensagem(),

                mensagem.getDataEnvio()
        );
    }
}
