package com.example.demoApiVersao23082026.service;

import com.example.demoApiVersao23082026.dto.NotificacaoDTO;
import com.example.demoApiVersao23082026.exception.BadRequestException;
import com.example.demoApiVersao23082026.exception.NotFoundException;
import com.example.demoApiVersao23082026.model.Evento;
import com.example.demoApiVersao23082026.model.EventoColaborador;
import com.example.demoApiVersao23082026.model.Inscricao;
import com.example.demoApiVersao23082026.model.Notificacao;
import com.example.demoApiVersao23082026.model.Usuario;
import com.example.demoApiVersao23082026.repository.NotificacaoRepository;
import com.example.demoApiVersao23082026.repository.EventoRepository;
import com.example.demoApiVersao23082026.repository.InscricaoRepository;
import com.example.demoApiVersao23082026.repository.EventoColaboradorRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.time.LocalDateTime;
import java.util.Locale;

@Service
public class NotificacaoService {

    @Autowired
    private NotificacaoRepository repository;

    @Autowired
    private UsuarioAutenticadoService usuarioAutenticadoService;

    @Autowired
    private EventoRepository eventoRepository;

    @Autowired
    private InscricaoRepository inscricaoRepository;

    @Autowired
    private EventoColaboradorRepository colaboradorRepository;

    public List<NotificacaoDTO> minhasNotificacoes() {

        Usuario usuario =
                usuarioAutenticadoService
                        .getUsuarioAutenticado();

        gerarLembretesDeEventosProximos(usuario);

        return repository
                .findByUsuarioIdOrderByDataCriacaoDesc(
                        usuario.getId()
                )
                .stream()
                .filter(n -> podeVerNotificacaoChat(n, usuario))
                .map(this::converterParaDTO)
                .toList();
    }

    private boolean podeVerNotificacaoChat(Notificacao notificacao, Usuario usuario) {
        if (notificacao.getTipo() != Notificacao.Tipo.CHAT_GERAL) {
            return true;
        }

        String titulo = notificacao.getTitulo() == null
                ? "" : notificacao.getTitulo().toLowerCase(Locale.ROOT);
        String mensagem = notificacao.getMensagem() == null
                ? "" : notificacao.getMensagem().toLowerCase(Locale.ROOT);
        boolean equipe = titulo.contains("chat da equipe")
                || mensagem.contains("chat interno da equipe")
                || mensagem.contains("chat interno");
        if (!equipe) return true;

        Evento evento = notificacao.getEvento();
        if (evento == null || usuario == null || usuario.getId() == null) return false;

        if (evento.getOrganizador() != null
                && usuario.getId().equals(evento.getOrganizador().getId())) return true;

        return colaboradorRepository
                .findByEventoIdAndUsuarioId(evento.getId(), usuario.getId())
                .filter(c -> c.getStatus() == EventoColaborador.Status.ACEITO
                        && Boolean.TRUE.equals(c.getPodeChat())
                        && !Boolean.TRUE.equals(c.getSilenciado())
                        && (c.getSilenciadoAte() == null
                            || !c.getSilenciadoAte().isAfter(LocalDateTime.now())))
                .isPresent();
    }

    private void gerarLembretesDeEventosProximos(Usuario usuario) {
        LocalDateTime agora = LocalDateTime.now();
        List<Evento> eventos = eventoRepository.findByStatusAndDataInicioBetween(
                Evento.Status.PUBLICADO, agora, agora.plusHours(24));

        for (Evento evento : eventos) {
            boolean organizador = evento.getOrganizador() != null
                    && usuario.getId().equals(evento.getOrganizador().getId());
            boolean inscrito = inscricaoRepository.findByEventoId(evento.getId())
                    .stream().anyMatch(i -> i.getUsuario() != null
                            && usuario.getId().equals(i.getUsuario().getId())
                            && (i.getStatus() == Inscricao.Status.INSCRITO
                                || i.getStatus() == Inscricao.Status.PRESENTE));
            boolean colaborador = colaboradorRepository.findByEventoId(evento.getId())
                    .stream().anyMatch(c -> c.getUsuario() != null
                            && usuario.getId().equals(c.getUsuario().getId())
                            && c.getStatus() == EventoColaborador.Status.ACEITO);
            if (!organizador && !inscrito && !colaborador) continue;
            if (repository.existsByUsuarioIdAndEventoIdAndTipo(
                    usuario.getId(), evento.getId(), Notificacao.Tipo.EVENTO_PROXIMO)) continue;

            Notificacao n = new Notificacao();
            n.setUsuario(usuario);
            n.setEvento(evento);
            n.setTipo(Notificacao.Tipo.EVENTO_PROXIMO);
            n.setTitulo("Evento próximo");
            n.setMensagem("O evento \"" + evento.getTitulo()
                    + "\" acontece nas próximas 24 horas. Confira a data, o horário e o local.");
            n.setLida(false);
            repository.save(n);
        }
    }

    public NotificacaoDTO marcarComoLida(
            Integer id) {

        Usuario usuario =
                usuarioAutenticadoService
                        .getUsuarioAutenticado();

        Notificacao notificacao =
                repository.findById(id)
                        .orElseThrow(() ->
                                new NotFoundException(
                                        "Notificação não encontrada"
                                ));

        if (!notificacao.getUsuario()
                .getId()
                .equals(usuario.getId())) {

            throw new BadRequestException(
                    "Esta notificação não pertence ao usuário autenticado"
            );
        }

        notificacao.setLida(true);

        return converterParaDTO(
                repository.save(notificacao)
        );
    }

    private NotificacaoDTO converterParaDTO(
            Notificacao notificacao) {

        return new NotificacaoDTO(
                notificacao.getId(),

                notificacao.getEvento() != null
                        ? notificacao.getEvento().getId()
                        : null,

                notificacao.getTipo().name(),

                notificacao.getTitulo(),
                notificacao.getMensagem(),

                notificacao.getLida(),

                notificacao.getDataCriacao()
        );
    }
}