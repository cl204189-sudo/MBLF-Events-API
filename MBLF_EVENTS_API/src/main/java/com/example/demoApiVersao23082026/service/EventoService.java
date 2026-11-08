package com.example.demoApiVersao23082026.service;

import com.example.demoApiVersao23082026.dto.EventoDTO;
import com.example.demoApiVersao23082026.exception.BadRequestException;
import com.example.demoApiVersao23082026.exception.NotFoundException;
import com.example.demoApiVersao23082026.model.Evento;
import com.example.demoApiVersao23082026.model.Inscricao;
import com.example.demoApiVersao23082026.model.EventoColaborador;
import com.example.demoApiVersao23082026.model.Notificacao;
import com.example.demoApiVersao23082026.model.Usuario;
import com.example.demoApiVersao23082026.repository.EventoRepository;
import com.example.demoApiVersao23082026.repository.InscricaoRepository;
import com.example.demoApiVersao23082026.repository.EventoColaboradorRepository;
import com.example.demoApiVersao23082026.repository.NotificacaoRepository;
import com.example.demoApiVersao23082026.repository.UsuarioRepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.LinkedHashMap;

@Service
public class EventoService {

    @Autowired
    private EventoRepository repository;

    @Autowired
    private UsuarioRepository usuarioRepository;

    @Autowired
    private UsuarioAutenticadoService usuarioAutenticadoService;

    @Autowired
    private UsuarioNomeService usuarioNomeService;

    @Autowired
    private InscricaoRepository inscricaoRepository;

    @Autowired
    private EventoColaboradorRepository colaboradorRepository;

    @Autowired
    private NotificacaoRepository notificacaoRepository;

    public List<EventoDTO> listarTodos() {

        return repository.findAll()
                .stream()
                .map(this::converterParaDTO)
                .toList();
    }

    public EventoDTO buscar(Integer id) {

        Evento evento = repository.findById(id)
                .orElseThrow(() ->
                        new NotFoundException("Evento não encontrado"));

        return converterParaDTO(evento);
    }

    public List<EventoDTO> listarPorOrganizador(Integer organizadorId) {

        return repository.findByOrganizadorId(organizadorId)
                .stream()
                .map(this::converterParaDTO)
                .toList();
    }

    public void validarPermissaoImagem(Integer eventoId) {

        Evento evento = repository.findById(eventoId)
                .orElseThrow(() ->
                        new NotFoundException("Evento não encontrado"));

        validarOrganizador(evento);

        if (evento.getStatus() == Evento.Status.CANCELADO ||
                evento.getStatus() == Evento.Status.REMOVIDO_ADMIN ||
                evento.getStatus() == Evento.Status.ENCERRADO) {

            throw new BadRequestException(
                    "Não é possível alterar a imagem deste evento"
            );
        }
    }

    public EventoDTO atualizarImagem(Integer eventoId, String nome) {

        Evento evento = repository.findById(eventoId)
                .orElseThrow(() ->
                        new NotFoundException("Evento não encontrado"));

        validarOrganizador(evento);

        if (evento.getStatus() == Evento.Status.CANCELADO ||
                evento.getStatus() == Evento.Status.REMOVIDO_ADMIN ||
                evento.getStatus() == Evento.Status.ENCERRADO) {

            throw new BadRequestException(
                    "Não é possível alterar a imagem deste evento"
            );
        }

        evento.setImagemNome(nome);

        return converterParaDTO(repository.save(evento));
    }

    private void validarOrganizador(Evento evento) {

        Usuario usuario = usuarioAutenticadoService
                .getUsuarioAutenticado();

        if (!evento.getOrganizador()
                .getId()
                .equals(usuario.getId())) {

            throw new BadRequestException(
                    "Somente o organizador pode realizar esta ação"
            );
        }
    }

    public EventoDTO criar(Evento evento) {

        Usuario organizador = usuarioAutenticadoService
                .getUsuarioAutenticado();

        if (evento.getTitulo() == null ||
                evento.getTitulo().isBlank()) {

            throw new BadRequestException(
                    "Título do evento é obrigatório"
            );
        }

        if (evento.getDataInicio() == null ||
                evento.getDataFim() == null) {

            throw new BadRequestException(
                    "Data de início e fim são obrigatórias"
            );
        }

        if (!evento.getDataFim()
                .isAfter(evento.getDataInicio())) {

            throw new BadRequestException(
                    "A data de fim deve ser posterior à data de início"
            );
        }

        if (evento.getVagasTotal() != null &&
                evento.getVagasTotal() < 0) {

            throw new BadRequestException(
                    "Quantidade de vagas inválida"
            );
        }

        evento.setOrganizador(organizador);

        evento.setStatus(Evento.Status.RASCUNHO);

        evento.setInscricoesAbertas(false);

        evento.setChatSomenteOrganizador(false);

        if (evento.getVagasTotal() != null) {
            evento.setVagasDisponiveis(
                    evento.getVagasTotal()
            );
        }

        evento.setHubAte(null);
        evento.setPublicacaoAte(null);
        evento.setMotivoRemocao(null);

        Evento salvo = repository.save(evento);

        return converterParaDTO(salvo);
    }

    @Transactional
    public EventoDTO atualizar(Integer id, Evento novo) {

        Evento existente = repository.buscarParaAtualizacao(id)
                .orElseThrow(() ->
                        new NotFoundException(
                                "Evento não encontrado"
                        )
                );

        validarOrganizador(existente);

        if (existente.getStatus() == Evento.Status.CANCELADO ||
                existente.getStatus() == Evento.Status.REMOVIDO_ADMIN ||
                existente.getStatus() == Evento.Status.ENCERRADO) {

            throw new BadRequestException(
                    "Não é possível editar um evento encerrado, cancelado ou removido"
            );
        }

        if (novo.getTitulo() == null ||
                novo.getTitulo().isBlank()) {

            throw new BadRequestException(
                    "Título do evento é obrigatório"
            );
        }

        if (novo.getDataInicio() == null ||
                novo.getDataFim() == null) {

            throw new BadRequestException(
                    "Data de início e fim são obrigatórias"
            );
        }

        if (!novo.getDataFim()
                .isAfter(novo.getDataInicio())) {

            throw new BadRequestException(
                    "A data de fim deve ser posterior à data de início"
            );
        }

        // Mantém as vagas já ocupadas pelas inscrições.
        Integer totalAnterior = existente.getVagasTotal();
        Integer disponiveisAnteriores = existente.getVagasDisponiveis();
        Integer novoTotal = novo.getVagasTotal();

        if (novoTotal != null && novoTotal < 0) {
            throw new BadRequestException(
                    "Quantidade de vagas inválida"
            );
        }

        if (totalAnterior != null &&
                disponiveisAnteriores != null) {

            int ocupadas = totalAnterior - disponiveisAnteriores;

            if (ocupadas < 0) {
                throw new BadRequestException(
                        "Quantidade de vagas do evento está inconsistente"
                );
            }

            if (novoTotal == null) {

                if (ocupadas > 0) {
                    throw new BadRequestException(
                            "Não é possível remover o limite de vagas com inscrições existentes"
                    );
                }

                existente.setVagasDisponiveis(null);

            } else {

                if (novoTotal < ocupadas) {
                    throw new BadRequestException(
                            "O novo total não pode ser menor que as "
                                    + ocupadas
                                    + " vagas já ocupadas"
                    );
                }

                existente.setVagasDisponiveis(
                        novoTotal - ocupadas
                );
            }

        } else if (totalAnterior == null &&
                disponiveisAnteriores == null) {

            // Sem limite anterior, não há contagem confiável.
            if (novoTotal != null) {
                throw new BadRequestException(
                        "Não é possível definir vagas em evento sem limite anterior; "
                                + "é necessário reconciliar as inscrições primeiro"
                );
            }

        } else {

            throw new BadRequestException(
                    "Quantidade de vagas do evento está inconsistente"
            );
        }

        existente.setTitulo(novo.getTitulo());
        existente.setDescricao(novo.getDescricao());
        existente.setTipo(novo.getTipo());
        existente.setEndereco(novo.getEndereco());
        existente.setCidade(novo.getCidade());
        existente.setDataInicio(novo.getDataInicio());
        existente.setDataFim(novo.getDataFim());
        existente.setCargaHoraria(novo.getCargaHoraria());
        existente.setVagasTotal(novo.getVagasTotal());

        Evento salvo = repository.save(existente);
        notificarAlteracaoEvento(salvo);
        return converterParaDTO(salvo);
    }

    private void notificarAlteracaoEvento(Evento evento) {
        Map<Integer, Usuario> destinatarios = new LinkedHashMap<>();
        for (Inscricao i : inscricaoRepository.findByEventoId(evento.getId())) {
            if (i.getUsuario() != null &&
                    (i.getStatus() == Inscricao.Status.INSCRITO
                            || i.getStatus() == Inscricao.Status.PRESENTE)) {
                destinatarios.put(i.getUsuario().getId(), i.getUsuario());
            }
        }
        for (EventoColaborador c : colaboradorRepository.findByEventoId(evento.getId())) {
            if (c.getUsuario() != null && c.getStatus() == EventoColaborador.Status.ACEITO) {
                destinatarios.put(c.getUsuario().getId(), c.getUsuario());
            }
        }
        if (evento.getOrganizador() != null) {
            destinatarios.remove(evento.getOrganizador().getId());
        }
        for (Usuario usuario : destinatarios.values()) {
            Notificacao n = new Notificacao();
            n.setUsuario(usuario);
            n.setEvento(evento);
            n.setTipo(Notificacao.Tipo.EVENTO_ATUALIZADO);
            n.setTitulo("Evento atualizado");
            n.setMensagem("O evento \"" + evento.getTitulo()
                    + "\" foi atualizado pelo organizador. Confira os detalhes mais recentes.");
            n.setLida(false);
            notificacaoRepository.save(n);
        }
    }

    @Transactional
    public EventoDTO cancelar(Integer id) {

        Evento evento = repository.buscarParaAtualizacao(id)
                .orElseThrow(() ->
                        new NotFoundException("Evento não encontrado")
                );

        validarOrganizador(evento);

        if (evento.getStatus() == Evento.Status.CANCELADO ||
                evento.getStatus() == Evento.Status.REMOVIDO_ADMIN) {

            throw new BadRequestException(
                    "Evento já está cancelado ou foi removido"
            );
        }

        if (evento.getStatus() == Evento.Status.ENCERRADO ||
                (evento.getDataFim() != null &&
                        !evento.getDataFim().isAfter(LocalDateTime.now()))) {

            throw new BadRequestException(
                    "Não é possível cancelar um evento já encerrado"
            );
        }

        evento.setStatus(Evento.Status.CANCELADO);
        evento.setInscricoesAbertas(false);

        // Mantém inscrições, presenças e certificados como histórico.
        return converterParaDTO(repository.save(evento));
    }

    public void deletar(Integer id) {

        Evento evento = repository.findById(id)
                .orElseThrow(() ->
                        new NotFoundException("Evento não encontrado")
                );

        validarOrganizador(evento);

        if (evento.getStatus() != Evento.Status.RASCUNHO) {
            throw new BadRequestException(
                    "Somente eventos em rascunho podem ser excluídos. "
                            + "Preserve o histórico dos demais eventos."
            );
        }

        repository.delete(evento);
    }

    public EventoDTO abrirInscricoes(Integer eventoId) {

        Evento evento = repository.findById(eventoId)
                .orElseThrow(() ->
                        new NotFoundException("Evento não encontrado")
                );

        validarOrganizador(evento);

        if (evento.getPublicacaoAte() == null) {
            throw new BadRequestException(
                    "Este evento não possui um plano de publicação ativo"
            );
        }

        if (evento.getPublicacaoAte()
                .isBefore(LocalDateTime.now())) {

            throw new BadRequestException(
                    "O período de publicação deste evento expirou"
            );
        }

        if (evento.getStatus() == Evento.Status.CANCELADO) {
            throw new BadRequestException(
                    "Não é possível publicar um evento cancelado"
            );
        }

        if (evento.getStatus() == Evento.Status.ENCERRADO) {
            throw new BadRequestException(
                    "Não é possível reabrir um evento encerrado"
            );
        }

        if (evento.getDataFim() != null &&
                !evento.getDataFim().isAfter(LocalDateTime.now())) {

            throw new BadRequestException(
                    "Não é possível abrir inscrições após o término do evento"
            );
        }

        if (evento.getStatus() == Evento.Status.REMOVIDO_ADMIN) {
            throw new BadRequestException(
                    "Este evento foi removido pela administração"
            );
        }

        evento.setInscricoesAbertas(true);
        evento.setStatus(Evento.Status.PUBLICADO);

        return converterParaDTO(repository.save(evento));
    }

    public EventoDTO fecharInscricoes(Integer eventoId) {

        Evento evento = repository.findById(eventoId)
                .orElseThrow(() ->
                        new NotFoundException("Evento não encontrado")
                );

        validarOrganizador(evento);

        if (evento.getStatus() == Evento.Status.CANCELADO ||
                evento.getStatus() == Evento.Status.REMOVIDO_ADMIN) {

            throw new BadRequestException(
                    "Evento cancelado ou removido não pode ser alterado"
            );
        }

        evento.setInscricoesAbertas(false);

        return converterParaDTO(repository.save(evento));
    }

    public List<EventoDTO> listarPublicos() {

        return repository
                .findByStatusAndInscricoesAbertasTrueAndPublicacaoAteAfter(
                        Evento.Status.PUBLICADO,
                        LocalDateTime.now()
                )
                .stream()
                .map(this::converterParaDTO)
                .toList();
    }

    private EventoDTO converterParaDTO(Evento evento) {

        EventoDTO dto = new EventoDTO(
                evento.getId(),

                evento.getOrganizador().getId(),

                // Nome legível, mesmo quando armazenado criptografado.
                usuarioNomeService.obterNome(
                        evento.getOrganizador()
                ),

                evento.getTitulo(),
                evento.getDescricao(),
                evento.getTipo(),
                evento.getEndereco(),
                evento.getCidade(),

                evento.getDataInicio(),
                evento.getDataFim(),

                evento.getCargaHoraria(),

                evento.getVagasTotal(),
                evento.getVagasDisponiveis(),

                evento.getHubAte(),
                evento.getPublicacaoAte(),

                evento.getInscricoesAbertas(),

                evento.getStatus().name()
        );

        if (evento.getImagemNome() != null) {
            dto.setImagemUrl(
                    "/eventos/imagens/" + evento.getImagemNome()
            );
        }

        return dto;
    }
}
