package com.example.demoApiVersao23082026.service;

import com.example.demoApiVersao23082026.dto.TarefaEventoDTO;
import com.example.demoApiVersao23082026.dto.TarefaEventoRequestDTO;
import com.example.demoApiVersao23082026.exception.BadRequestException;
import com.example.demoApiVersao23082026.exception.NotFoundException;

import com.example.demoApiVersao23082026.model.Evento;
import com.example.demoApiVersao23082026.model.EventoColaborador;
import com.example.demoApiVersao23082026.model.TarefaEvento;
import com.example.demoApiVersao23082026.model.Usuario;

import com.example.demoApiVersao23082026.repository.EventoColaboradorRepository;
import com.example.demoApiVersao23082026.repository.EventoRepository;
import com.example.demoApiVersao23082026.repository.TarefaEventoRepository;
import com.example.demoApiVersao23082026.repository.UsuarioRepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class TarefaEventoService {

    @Autowired
    private TarefaEventoRepository repository;

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
    public TarefaEventoDTO criar(
            Integer eventoId,
            TarefaEventoRequestDTO dto) {

        Evento evento = buscarEvento(eventoId);

        validarHub(evento);

        Usuario criador = usuarioAutenticadoService
                .getUsuarioAutenticado();

        validarPermissaoTarefas(
                evento,
                criador.getId()
        );

        if (dto == null ||
                dto.getTitulo() == null ||
                dto.getTitulo().isBlank()) {

            throw new BadRequestException(
                    "Título da tarefa é obrigatório"
            );
        }

        Usuario responsavel = null;

        if (dto.getResponsavelId() != null) {

            responsavel = usuarioRepository
                    .findById(dto.getResponsavelId())
                    .orElseThrow(() ->
                            new NotFoundException(
                                    "Responsável não encontrado"
                            ));

            validarResponsavelDoEvento(
                    evento,
                    responsavel.getId()
            );
        }

        TarefaEvento tarefa = new TarefaEvento();

        tarefa.setEvento(evento);
        tarefa.setCriadoPor(criador);
        tarefa.setResponsavel(responsavel);

        tarefa.setTitulo(dto.getTitulo());
        tarefa.setDescricao(dto.getDescricao());

        tarefa.setPrioridade(
                dto.getPrioridade() != null
                        ? dto.getPrioridade()
                        : TarefaEvento.Prioridade.MEDIA
        );

        tarefa.setStatus(
                TarefaEvento.Status.PENDENTE
        );

        tarefa.setPrazo(dto.getPrazo());

        return converterParaDTO(
                repository.save(tarefa)
        );
    }

    @Transactional(readOnly = true)
    public List<TarefaEventoDTO> listarPorEvento(
            Integer eventoId) {

        Evento evento = buscarEvento(eventoId);

        validarHub(evento);

        Usuario usuario = usuarioAutenticadoService
                .getUsuarioAutenticado();

        validarPermissaoTarefas(
                evento,
                usuario.getId()
        );

        return repository
                .findByEventoIdOrderByPrazoAsc(eventoId)
                .stream()
                .map(this::converterParaDTO)
                .toList();
    }

    @Transactional
    public TarefaEventoDTO alterarStatus(
            Integer tarefaId,
            TarefaEvento.Status novoStatus) {

        TarefaEvento tarefa = repository
                .findById(tarefaId)
                .orElseThrow(() ->
                        new NotFoundException(
                                "Tarefa não encontrada"
                        ));

        Evento evento = tarefa.getEvento();

        validarHub(evento);

        Usuario usuario = usuarioAutenticadoService
                .getUsuarioAutenticado();

        validarPermissaoTarefas(
                evento,
                usuario.getId()
        );

        tarefa.setStatus(novoStatus);

        if (novoStatus == TarefaEvento.Status.CONCLUIDA) {

            tarefa.setDataConclusao(
                    LocalDateTime.now()
            );

        } else {

            tarefa.setDataConclusao(null);
        }

        return converterParaDTO(
                repository.save(tarefa)
        );
    }

    @Transactional
    public void deletar(Integer tarefaId) {

        TarefaEvento tarefa = repository
                .findById(tarefaId)
                .orElseThrow(() ->
                        new NotFoundException(
                                "Tarefa não encontrada"
                        ));

        Evento evento = tarefa.getEvento();

        validarHub(evento);

        Usuario usuario = usuarioAutenticadoService
                .getUsuarioAutenticado();

        validarPermissaoTarefas(
                evento,
                usuario.getId()
        );

        repository.delete(tarefa);
    }

    private void validarPermissaoTarefas(
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
                                "Usuário não faz parte da equipe"
                        ));

        if (colaborador.getStatus()
                != EventoColaborador.Status.ACEITO) {

            throw new BadRequestException(
                    "Usuário não é colaborador ativo"
            );
        }

        if (!Boolean.TRUE.equals(
                colaborador.getPodeTarefas())) {

            throw new BadRequestException(
                    "Este colaborador não possui acesso às tarefas"
            );
        }
    }

    private void validarResponsavelDoEvento(
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
                                "Responsável não faz parte da equipe"
                        ));

        if (colaborador.getStatus()
                != EventoColaborador.Status.ACEITO) {

            throw new BadRequestException(
                    "Responsável não é colaborador ativo"
            );
        }
    }

    private void validarHub(Evento evento) {

        if (evento.getHubAte() == null ||
                evento.getHubAte()
                        .isBefore(LocalDateTime.now())) {

            throw new BadRequestException(
                    "O Hub deste evento está expirado"
            );
        }
    }

    private Evento buscarEvento(Integer id) {

        return eventoRepository
                .findById(id)
                .orElseThrow(() ->
                        new NotFoundException(
                                "Evento não encontrado"
                        ));
    }

    private TarefaEventoDTO converterParaDTO(
            TarefaEvento tarefa) {

        return new TarefaEventoDTO(
                tarefa.getId(),

                tarefa.getEvento().getId(),

                tarefa.getCriadoPor().getId(),
                usuarioNomeService.obterNome(
                        tarefa.getCriadoPor()
                ),

                tarefa.getResponsavel() != null
                        ? tarefa.getResponsavel().getId()
                        : null,

                tarefa.getResponsavel() != null
                        ? usuarioNomeService.obterNome(
                        tarefa.getResponsavel()
                )
                        : null,

                tarefa.getTitulo(),
                tarefa.getDescricao(),

                tarefa.getPrioridade().name(),
                tarefa.getStatus().name(),

                tarefa.getPrazo(),
                tarefa.getDataCriacao(),
                tarefa.getDataConclusao()
        );
    }
}
