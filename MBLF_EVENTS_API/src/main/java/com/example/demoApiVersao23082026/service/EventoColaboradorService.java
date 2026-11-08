package com.example.demoApiVersao23082026.service;

import com.example.demoApiVersao23082026.dto.ConviteColaboradorDTO;
import com.example.demoApiVersao23082026.dto.ConviteColaboradorEmailDTO;
import com.example.demoApiVersao23082026.dto.EventoColaboradorDTO;
import com.example.demoApiVersao23082026.dto.PermissaoColaboradorDTO;
import com.example.demoApiVersao23082026.exception.BadRequestException;
import com.example.demoApiVersao23082026.exception.NotFoundException;
import com.example.demoApiVersao23082026.model.Evento;
import com.example.demoApiVersao23082026.model.EventoColaborador;
import com.example.demoApiVersao23082026.model.Notificacao;
import com.example.demoApiVersao23082026.model.Usuario;
import com.example.demoApiVersao23082026.repository.EventoColaboradorRepository;
import com.example.demoApiVersao23082026.repository.EventoRepository;
import com.example.demoApiVersao23082026.repository.NotificacaoRepository;
import com.example.demoApiVersao23082026.repository.UsuarioRepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class EventoColaboradorService {

    @Autowired
    private EventoColaboradorRepository repository;

    @Autowired
    private EventoRepository eventoRepository;

    @Autowired
    private UsuarioAutenticadoService usuarioAutenticadoService;

    @Autowired
    private UsuarioRepository usuarioRepository;

    @Autowired
    private EmailHashService emailHashService;

    @Autowired
    private NotificacaoRepository notificacaoRepository;

    @Autowired
    private UsuarioNomeService usuarioNomeService;

    public List<EventoColaboradorDTO> listarMeus() {

        Usuario usuario = usuarioAutenticadoService
                .getUsuarioAutenticado();

        return repository.findByUsuarioId(usuario.getId())
                .stream()
                .map(this::converterParaDTO)
                .toList();
    }

    @Transactional
    public EventoColaboradorDTO convidar(
            Integer eventoId,
            ConviteColaboradorDTO dto) {

        Evento evento = buscarEvento(eventoId);
        Usuario organizador = usuarioAutenticadoService
                .getUsuarioAutenticado();

        validarOrganizador(evento, organizador);
        validarHubAtivo(evento);

        if (dto == null || dto.getUsuarioConvidadoId() == null) {
            throw new BadRequestException(
                    "Usuário convidado é obrigatório"
            );
        }

        if (evento.getOrganizador().getId()
                .equals(dto.getUsuarioConvidadoId())) {
            throw new BadRequestException(
                    "O organizador não pode convidar a si mesmo"
            );
        }

        Usuario convidado = usuarioRepository
                .findById(dto.getUsuarioConvidadoId())
                .orElseThrow(() ->
                        new NotFoundException(
                                "Usuário convidado não encontrado"
                        )
                );

        EventoColaborador colaborador = prepararNovoConvite(
                evento,
                convidado,
                dto.getFuncao()
        );

        EventoColaborador salvo = repository.save(colaborador);

        criarNotificacaoConvite(evento, organizador, convidado);

        return converterParaDTO(salvo);
    }

    @Transactional
    public EventoColaboradorDTO aceitar(Integer eventoId) {

        Usuario usuarioAutenticado = usuarioAutenticadoService
                .getUsuarioAutenticado();

        EventoColaborador colaborador = repository
                .findByEventoIdAndUsuarioId(
                        eventoId,
                        usuarioAutenticado.getId()
                )
                .orElseThrow(() ->
                        new NotFoundException(
                                "Convite não encontrado"
                        )
                );

        if (colaborador.getStatus()
                != EventoColaborador.Status.PENDENTE) {
            throw new BadRequestException(
                    "Este convite já foi respondido"
            );
        }

        colaborador.setStatus(EventoColaborador.Status.ACEITO);
        colaborador.setDataResposta(LocalDateTime.now());

        EventoColaborador atualizado = repository.save(colaborador);

        criarNotificacaoParaOrganizador(colaborador, true);

        return converterParaDTO(atualizado);
    }

    @Transactional
    public EventoColaboradorDTO recusar(Integer eventoId) {

        Usuario usuarioAutenticado = usuarioAutenticadoService
                .getUsuarioAutenticado();

        EventoColaborador colaborador = repository
                .findByEventoIdAndUsuarioId(
                        eventoId,
                        usuarioAutenticado.getId()
                )
                .orElseThrow(() ->
                        new NotFoundException(
                                "Convite não encontrado"
                        )
                );

        if (colaborador.getStatus()
                != EventoColaborador.Status.PENDENTE) {
            throw new BadRequestException(
                    "Este convite já foi respondido"
            );
        }

        colaborador.setStatus(EventoColaborador.Status.RECUSADO);
        colaborador.setDataResposta(LocalDateTime.now());

        EventoColaborador atualizado = repository.save(colaborador);

        criarNotificacaoParaOrganizador(colaborador, false);

        return converterParaDTO(atualizado);
    }

    @Transactional
    public EventoColaboradorDTO convidarPorEmail(
            Integer eventoId,
            ConviteColaboradorEmailDTO dto) {

        Evento evento = buscarEvento(eventoId);

        Usuario organizador = usuarioAutenticadoService
                .getUsuarioAutenticado();

        validarOrganizador(evento, organizador);
        validarHubAtivo(evento);

        if (dto == null ||
                dto.getEmail() == null ||
                dto.getEmail().isBlank()) {
            throw new BadRequestException(
                    "Email é obrigatório"
            );
        }

        String email = dto.getEmail().trim();
        String emailHash = emailHashService.gerarHash(email);

        Usuario convidado = usuarioRepository
                .findByEmailHash(emailHash)
                .or(() -> usuarioRepository.findByEmail(email))
                .orElseThrow(() ->
                        new NotFoundException(
                                "Nenhum usuário encontrado com este email"
                        )
                );

        if (evento.getOrganizador().getId()
                .equals(convidado.getId())) {
            throw new BadRequestException(
                    "O organizador não pode convidar a si mesmo"
            );
        }

        EventoColaborador colaborador = prepararNovoConvite(
                evento,
                convidado,
                dto.getFuncao()
        );

        EventoColaborador salvo = repository.save(colaborador);

        criarNotificacaoConvite(evento, organizador, convidado);

        return converterParaDTO(salvo);
    }

    private EventoColaborador prepararNovoConvite(
            Evento evento,
            Usuario convidado,
            String funcao) {

        if (funcao == null || funcao.isBlank()) {
            throw new BadRequestException(
                    "Função é obrigatória"
            );
        }

        EventoColaborador colaborador = repository
                .findByEventoIdAndUsuarioId(
                        evento.getId(),
                        convidado.getId()
                )
                .orElseGet(EventoColaborador::new);

        if (colaborador.getId() != null
                && colaborador.getStatus()
                != EventoColaborador.Status.RECUSADO
                && colaborador.getStatus()
                != EventoColaborador.Status.EXPULSO) {

            throw new BadRequestException(
                    "Este usuário já possui convite ou vínculo ativo com este evento"
            );
        }

        colaborador.setEvento(evento);
        colaborador.setUsuario(convidado);
        colaborador.setFuncao(funcao);
        colaborador.setStatus(EventoColaborador.Status.PENDENTE);
        colaborador.setDataResposta(null);

        colaborador.setPodeChat(false);
        colaborador.setPodeCalendario(false);
        colaborador.setPodeTarefas(false);
        colaborador.setPodeVerInscritos(false);
        colaborador.setPodeRegistrarPresencas(false);
        colaborador.setPodeVerRelatorios(false);
        colaborador.setPodeSilenciar(false);
        colaborador.setPodeExpulsar(false);
        colaborador.setPodeLiberarCertificados(false);

        colaborador.setSilenciado(false);
        colaborador.setSilenciadoAte(null);

        return colaborador;
    }

    @Transactional
    public EventoColaboradorDTO remover(
            Integer eventoId,
            Integer usuarioId) {

        Evento evento = buscarEvento(eventoId);

        Usuario operador = usuarioAutenticadoService
                .getUsuarioAutenticado();

        validarOrganizador(evento, operador);

        EventoColaborador colaborador = repository
                .findByEventoIdAndUsuarioId(eventoId, usuarioId)
                .orElseThrow(() ->
                        new NotFoundException(
                                "Colaborador não encontrado"
                        )
                );

        if (colaborador.getStatus()
                != EventoColaborador.Status.ACEITO
                && colaborador.getStatus()
                != EventoColaborador.Status.PENDENTE) {

            throw new BadRequestException(
                    "Colaborador já não está ativo"
            );
        }

        colaborador.setStatus(EventoColaborador.Status.EXPULSO);
        colaborador.setDataResposta(LocalDateTime.now());

        colaborador.setPodeChat(false);
        colaborador.setPodeCalendario(false);
        colaborador.setPodeTarefas(false);
        colaborador.setPodeVerInscritos(false);
        colaborador.setPodeRegistrarPresencas(false);
        colaborador.setPodeVerRelatorios(false);
        colaborador.setPodeSilenciar(false);
        colaborador.setPodeExpulsar(false);
        colaborador.setPodeLiberarCertificados(false);

        return converterParaDTO(repository.save(colaborador));
    }

    public List<EventoColaboradorDTO> listarPorEvento(
            Integer eventoId) {

        Evento evento = buscarEvento(eventoId);

        Usuario usuarioAutenticado = usuarioAutenticadoService
                .getUsuarioAutenticado();

        boolean organizador = evento.getOrganizador()
                .getId()
                .equals(usuarioAutenticado.getId());

        boolean colaboradorAceito = repository
                .findByEventoIdAndUsuarioId(
                        eventoId,
                        usuarioAutenticado.getId()
                )
                .map(c -> c.getStatus()
                        == EventoColaborador.Status.ACEITO)
                .orElse(false);

        if (!organizador && !colaboradorAceito) {
            throw new BadRequestException(
                    "Você não possui permissão para visualizar a equipe deste evento"
            );
        }

        return repository.findByEventoId(eventoId)
                .stream()
                .map(this::converterParaDTO)
                .toList();
    }

    private void criarNotificacaoConvite(
            Evento evento,
            Usuario organizador,
            Usuario convidado) {

        Notificacao notificacao = new Notificacao();

        notificacao.setUsuario(convidado);
        notificacao.setEvento(evento);

        notificacao.setTipo(
                Notificacao.Tipo.CONVITE_COLABORADOR
        );

        notificacao.setTitulo(
                "Novo convite para colaborar"
        );

        String nomeOrganizador = usuarioNomeService
                .obterNome(organizador);

        notificacao.setMensagem(
                nomeOrganizador
                        + " convidou você para colaborar no evento "
                        + evento.getTitulo()
        );

        notificacao.setLida(false);

        notificacaoRepository.save(notificacao);
    }

    private void criarNotificacaoParaOrganizador(
            EventoColaborador colaborador,
            boolean aceitou) {

        Notificacao notificacao = new Notificacao();

        notificacao.setUsuario(
                colaborador.getEvento().getOrganizador()
        );

        notificacao.setEvento(colaborador.getEvento());

        String nomeColaborador = usuarioNomeService
                .obterNome(colaborador.getUsuario());

        if (aceitou) {

            notificacao.setTipo(
                    Notificacao.Tipo.CONVITE_ACEITO
            );

            notificacao.setTitulo("Convite aceito");

            notificacao.setMensagem(
                    nomeColaborador
                            + " aceitou o convite para colaborar no evento "
                            + colaborador.getEvento().getTitulo()
            );

        } else {

            notificacao.setTipo(
                    Notificacao.Tipo.CONVITE_RECUSADO
            );

            notificacao.setTitulo("Convite recusado");

            notificacao.setMensagem(
                    nomeColaborador
                            + " recusou o convite para colaborar no evento "
                            + colaborador.getEvento().getTitulo()
            );
        }

        notificacao.setLida(false);

        notificacaoRepository.save(notificacao);
    }

    @Transactional
    public EventoColaboradorDTO alterarPermissoes(
            Integer eventoId,
            Integer colaboradorUsuarioId,
            PermissaoColaboradorDTO dto) {

        Evento evento = buscarEvento(eventoId);

        Usuario usuarioAutenticado = usuarioAutenticadoService
                .getUsuarioAutenticado();

        validarOrganizador(evento, usuarioAutenticado);

        if (dto == null) {
            throw new BadRequestException(
                    "Dados de permissões são obrigatórios"
            );
        }

        EventoColaborador colaborador = repository
                .findByEventoIdAndUsuarioId(
                        eventoId,
                        colaboradorUsuarioId
                )
                .orElseThrow(() ->
                        new NotFoundException(
                                "Colaborador não encontrado"
                        )
                );

        if (colaborador.getStatus()
                != EventoColaborador.Status.ACEITO) {

            throw new BadRequestException(
                    "Somente colaboradores aceitos podem receber permissões"
            );
        }

        if (Boolean.TRUE.equals(
                dto.getPodeLiberarCertificados())) {

            boolean jaTinhaPermissao = Boolean.TRUE.equals(
                    colaborador.getPodeLiberarCertificados()
            );

            if (!jaTinhaPermissao) {

                long quantidade = repository
                        .countByEventoIdAndPodeLiberarCertificadosTrue(
                                eventoId
                        );

                if (quantidade >= 2) {
                    throw new BadRequestException(
                            "No máximo 2 colaboradores podem liberar certificados"
                    );
                }
            }
        }

        if (dto.getPodeChat() != null) {
            colaborador.setPodeChat(dto.getPodeChat());
        }

        if (dto.getPodeCalendario() != null) {
            colaborador.setPodeCalendario(
                    dto.getPodeCalendario()
            );
        }

        if (dto.getPodeTarefas() != null) {
            colaborador.setPodeTarefas(
                    dto.getPodeTarefas()
            );
        }

        if (dto.getPodeVerInscritos() != null) {
            colaborador.setPodeVerInscritos(
                    dto.getPodeVerInscritos()
            );
        }

        if (dto.getPodeRegistrarPresencas() != null) {
            colaborador.setPodeRegistrarPresencas(
                    dto.getPodeRegistrarPresencas()
            );
        }

        if (dto.getPodeVerRelatorios() != null) {
            colaborador.setPodeVerRelatorios(
                    dto.getPodeVerRelatorios()
            );
        }

        if (dto.getPodeSilenciar() != null) {
            colaborador.setPodeSilenciar(
                    dto.getPodeSilenciar()
            );
        }

        if (dto.getPodeExpulsar() != null) {
            colaborador.setPodeExpulsar(
                    dto.getPodeExpulsar()
            );
        }

        if (dto.getPodeLiberarCertificados() != null) {
            colaborador.setPodeLiberarCertificados(
                    dto.getPodeLiberarCertificados()
            );
        }

        return converterParaDTO(
                repository.save(colaborador)
        );
    }

    private Evento buscarEvento(Integer eventoId) {

        return eventoRepository.findById(eventoId)
                .orElseThrow(() ->
                        new NotFoundException(
                                "Evento não encontrado"
                        )
                );
    }

    private void validarOrganizador(
            Evento evento,
            Usuario usuario) {

        if (!evento.getOrganizador().getId()
                .equals(usuario.getId())) {

            throw new BadRequestException(
                    "Somente o organizador pode realizar esta operação"
            );
        }
    }

    private void validarHubAtivo(Evento evento) {

        if (evento.getHubAte() == null ||
                !evento.getHubAte().isAfter(LocalDateTime.now())) {

            throw new BadRequestException(
                    "O Hub deste evento não está ativo"
            );
        }
    }

    private EventoColaboradorDTO converterParaDTO(
            EventoColaborador colaborador) {

        return new EventoColaboradorDTO(
                colaborador.getId(),

                colaborador.getEvento().getId(),
                colaborador.getEvento().getTitulo(),

                colaborador.getUsuario().getId(),
                usuarioNomeService.obterNome(
                        colaborador.getUsuario()
                ),

                colaborador.getFuncao(),
                colaborador.getStatus().name(),

                colaborador.getPodeChat(),
                colaborador.getPodeCalendario(),
                colaborador.getPodeTarefas(),

                colaborador.getPodeVerInscritos(),
                colaborador.getPodeRegistrarPresencas(),
                colaborador.getPodeVerRelatorios(),

                colaborador.getPodeSilenciar(),
                colaborador.getPodeExpulsar(),
                colaborador.getPodeLiberarCertificados(),

                colaborador.getSilenciado()
        );
    }
}
