package com.example.demoApiVersao23082026.service;

import com.example.demoApiVersao23082026.dto.AgendaEventoDTO;
import com.example.demoApiVersao23082026.dto.AgendaEventoRequestDTO;
import com.example.demoApiVersao23082026.exception.BadRequestException;
import com.example.demoApiVersao23082026.exception.NotFoundException;

import com.example.demoApiVersao23082026.model.AgendaEvento;
import com.example.demoApiVersao23082026.model.Evento;
import com.example.demoApiVersao23082026.model.EventoColaborador;
import com.example.demoApiVersao23082026.model.Usuario;
import com.example.demoApiVersao23082026.model.Inscricao;
import com.example.demoApiVersao23082026.repository.InscricaoRepository;
import java.util.EnumSet;

import com.example.demoApiVersao23082026.repository.AgendaEventoRepository;
import com.example.demoApiVersao23082026.repository.EventoColaboradorRepository;
import com.example.demoApiVersao23082026.repository.EventoRepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class AgendaEventoService {

    @Autowired
    private AgendaEventoRepository repository;

    @Autowired
    private EventoRepository eventoRepository;

    @Autowired
    private UsuarioAutenticadoService usuarioAutenticadoService;

    @Autowired
    private EventoColaboradorRepository colaboradorRepository;

    @Autowired
    private UsuarioNomeService usuarioNomeService;

    @Autowired
    private InscricaoRepository inscricaoRepository;

    @Transactional
    public AgendaEventoDTO criar(
            Integer eventoId,
            AgendaEventoRequestDTO dto) {

        Evento evento = buscarEvento(eventoId);

        validarHub(evento);

        Usuario usuario = usuarioAutenticadoService
                .getUsuarioAutenticado();

        validarPermissao(
                evento,
                usuario.getId()
        );

        if (dto == null ||
                dto.getTitulo() == null ||
                dto.getTitulo().isBlank()) {

            throw new BadRequestException(
                    "Título é obrigatório"
            );
        }

        if (dto.getDataInicio() == null) {

            throw new BadRequestException(
                    "Data de início é obrigatória"
            );
        }

        if (dto.getDataFim() != null &&
                dto.getDataFim()
                        .isBefore(dto.getDataInicio())) {

            throw new BadRequestException(
                    "Data de fim não pode ser anterior à data de início"
            );
        }

        AgendaEvento agenda = new AgendaEvento();

        agenda.setEvento(evento);
        agenda.setCriadoPor(usuario);

        agenda.setTitulo(dto.getTitulo());
        agenda.setDescricao(dto.getDescricao());

        agenda.setDataInicio(dto.getDataInicio());
        agenda.setDataFim(dto.getDataFim());

        agenda.setTipo(
                dto.getTipo() != null
                        ? dto.getTipo()
                        : AgendaEvento.Tipo.OUTRO
        );

        return converterParaDTO(
                repository.save(agenda)
        );
    }

    @Transactional(readOnly = true)
    public List<AgendaEventoDTO> listar(
            Integer eventoId) {

        Evento evento = buscarEvento(eventoId);

        validarHub(evento);

        Usuario usuario = usuarioAutenticadoService
                .getUsuarioAutenticado();

        validarPermissao(
                evento,
                usuario.getId()
        );

        return repository
                .findByEventoIdOrderByDataInicioAsc(eventoId)
                .stream()
                .map(this::converterParaDTO)
                .toList();
    }

    // Leitura disponível somente para participantes com inscrição ativa.
    @Transactional(readOnly = true)
    public List<AgendaEventoDTO> listarParaInscrito(Integer eventoId) {
        buscarEvento(eventoId);
        Usuario usuario = usuarioAutenticadoService.getUsuarioAutenticado();
        boolean inscrito = inscricaoRepository.existsByUsuarioIdAndEventoIdAndStatusIn(
                usuario.getId(), eventoId,
                EnumSet.of(Inscricao.Status.INSCRITO, Inscricao.Status.PRESENTE));
        if (!inscrito) {
            throw new BadRequestException("É necessário estar inscrito no evento para ver a programação");
        }
        return repository.findByEventoIdOrderByDataInicioAsc(eventoId)
                .stream().map(this::converterParaDTO).toList();
    }

    @Transactional
    public void deletar(Integer agendaId) {

        AgendaEvento agenda = repository
                .findById(agendaId)
                .orElseThrow(() ->
                        new NotFoundException(
                                "Item da agenda não encontrado"
                        ));

        Evento evento = agenda.getEvento();

        validarHub(evento);

        Usuario usuario = usuarioAutenticadoService
                .getUsuarioAutenticado();

        validarPermissao(
                evento,
                usuario.getId()
        );

        repository.delete(agenda);
    }

    private void validarPermissao(
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
                                "Usuário não faz parte da equipe deste evento"
                        ));

        if (colaborador.getStatus()
                != EventoColaborador.Status.ACEITO) {

            throw new BadRequestException(
                    "Usuário não é colaborador ativo"
            );
        }

        if (!Boolean.TRUE.equals(
                colaborador.getPodeCalendario())) {

            throw new BadRequestException(
                    "Este colaborador não possui acesso ao calendário"
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

    private AgendaEventoDTO converterParaDTO(
            AgendaEvento agenda) {

        return new AgendaEventoDTO(
                agenda.getId(),

                agenda.getEvento().getId(),

                agenda.getCriadoPor().getId(),

                usuarioNomeService.obterNome(
                        agenda.getCriadoPor()
                ),

                agenda.getTitulo(),
                agenda.getDescricao(),

                agenda.getDataInicio(),
                agenda.getDataFim(),

                agenda.getTipo().name()
        );
    }
}
