package com.example.demoApiVersao23082026.service;

import com.example.demoApiVersao23082026.dto.PresencaDTO;
import com.example.demoApiVersao23082026.exception.BadRequestException;
import com.example.demoApiVersao23082026.exception.NotFoundException;

import com.example.demoApiVersao23082026.model.Evento;
import com.example.demoApiVersao23082026.model.AuditoriaPresenca;
import com.example.demoApiVersao23082026.model.EventoColaborador;
import com.example.demoApiVersao23082026.model.Inscricao;
import com.example.demoApiVersao23082026.model.Presenca;
import com.example.demoApiVersao23082026.model.Usuario;

import com.example.demoApiVersao23082026.repository.AuditoriaPresencaRepository;
import com.example.demoApiVersao23082026.repository.CertificadoRepository;
import com.example.demoApiVersao23082026.repository.EventoColaboradorRepository;
import com.example.demoApiVersao23082026.repository.EventoRepository;
import com.example.demoApiVersao23082026.repository.InscricaoRepository;
import com.example.demoApiVersao23082026.repository.PresencaRepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class PresencaService {

    @Autowired
    private PresencaRepository repository;

    @Autowired
    private InscricaoRepository inscricaoRepository;

    @Autowired
    private UsuarioAutenticadoService usuarioAutenticadoService;

    @Autowired
    private EventoColaboradorRepository colaboradorRepository;

    @Autowired
    private EventoRepository eventoRepository;

    @Autowired
    private AuditoriaPresencaRepository auditoriaRepository;

    @Autowired
    private CertificadoRepository certificadoRepository;

    @Autowired
    private UsuarioNomeService usuarioNomeService;

    @Transactional
    public void alterarManualmente(
            Integer eventoId,
            Integer inscricaoId,
            boolean presente,
            String motivo) {

        if (motivo == null ||
                motivo.trim().length() < 5 ||
                motivo.length() > 500) {

            throw new BadRequestException(
                    "Informe um motivo de 5 a 500 caracteres"
            );
        }

        Inscricao inscricao = inscricaoRepository
                .buscarPorIdParaAtualizacao(inscricaoId)
                .orElseThrow(() ->
                        new NotFoundException(
                                "Inscrição não encontrada"
                        ));

        Evento evento = inscricao.getEvento();

        Usuario operador = usuarioAutenticadoService
                .getUsuarioAutenticado();

        if (!evento.getId().equals(eventoId)) {

            throw new BadRequestException(
                    "Inscrição pertence a outro evento"
            );
        }

        if (!evento.getOrganizador()
                .getId()
                .equals(operador.getId())) {

            throw new BadRequestException(
                    "Somente o organizador pode alterar presenças manualmente"
            );
        }

        if (evento.getStatus() == Evento.Status.CANCELADO ||
                evento.getStatus() == Evento.Status.REMOVIDO_ADMIN) {

            throw new BadRequestException(
                    "Evento cancelado ou removido"
            );
        }

        if (inscricao.getStatus() == Inscricao.Status.CANCELADO) {

            throw new BadRequestException(
                    "Inscrição cancelada"
            );
        }

        boolean jaPresente =
                inscricao.getStatus() == Inscricao.Status.PRESENTE ||
                        repository.existsByInscricaoId(inscricaoId);

        if (presente == jaPresente) {

            throw new BadRequestException(
                    "A presença já está nesta situação"
            );
        }

        if (!presente &&
                certificadoRepository.existsByInscricaoId(inscricaoId)) {

            throw new BadRequestException(
                    "Não é possível remover presença com certificado emitido"
            );
        }

        if (presente) {

            Presenca nova = new Presenca();

            nova.setInscricao(inscricao);

            repository.save(nova);

            inscricao.setStatus(
                    Inscricao.Status.PRESENTE
            );

        } else {

            Presenca existente = repository
                    .findByInscricaoId(inscricaoId)
                    .orElseThrow(() ->
                            new BadRequestException(
                                    "Registro de presença não encontrado"
                            ));

            repository.delete(existente);

            inscricao.setStatus(
                    Inscricao.Status.INSCRITO
            );
        }

        inscricaoRepository.save(inscricao);

        AuditoriaPresenca auditoria = new AuditoriaPresenca();

        auditoria.setInscricaoId(inscricaoId);
        auditoria.setOperadorId(operador.getId());

        auditoria.setAcao(
                presente ? "REGISTRAR" : "REMOVER"
        );

        auditoria.setMotivo(motivo.trim());

        auditoriaRepository.save(auditoria);
    }

    @Transactional
    public PresencaDTO registrar(
            String token,
            Integer eventoId) {

        if (eventoId == null) {

            throw new BadRequestException(
                    "Evento do check-in é obrigatório"
            );
        }

        if (token == null || token.isBlank()) {

            throw new BadRequestException(
                    "Token do QR Code é obrigatório"
            );
        }

        Inscricao inscricao = inscricaoRepository
                .buscarPorTokenParaAtualizacao(token)
                .orElseThrow(() ->
                        new NotFoundException(
                                "QR Code inválido"
                        ));

        Usuario usuarioAutenticado = usuarioAutenticadoService
                .getUsuarioAutenticado();

        Evento evento = inscricao.getEvento();

        if (evento.getStatus() == Evento.Status.CANCELADO ||
                evento.getStatus() == Evento.Status.REMOVIDO_ADMIN) {

            throw new BadRequestException(
                    "Check-in indisponível para evento cancelado ou removido"
            );
        }

        if (!evento.getId().equals(eventoId)) {

            throw new BadRequestException(
                    "Este QR Code pertence a outro evento"
            );
        }

        boolean organizador = evento.getOrganizador()
                .getId()
                .equals(usuarioAutenticado.getId());

        if (!organizador) {

            EventoColaborador colaborador = colaboradorRepository
                    .findByEventoIdAndUsuarioId(
                            evento.getId(),
                            usuarioAutenticado.getId()
                    )
                    .orElseThrow(() ->
                            new BadRequestException(
                                    "Usuário não possui permissão para registrar presença"
                            ));

            if (colaborador.getStatus()
                    != EventoColaborador.Status.ACEITO) {

                throw new BadRequestException(
                        "Usuário não é colaborador ativo"
                );
            }

            if (!Boolean.TRUE.equals(
                    colaborador.getPodeRegistrarPresencas())) {

                throw new BadRequestException(
                        "Este colaborador não possui permissão para registrar presenças"
                );
            }
        }

        if (inscricao.getStatus()
                == Inscricao.Status.CANCELADO) {

            throw new BadRequestException(
                    "Esta inscrição está cancelada"
            );
        }

        if (inscricao.getStatus()
                == Inscricao.Status.PRESENTE ||
                repository.existsByInscricaoId(inscricao.getId())) {

            throw new BadRequestException(
                    "Presença já registrada"
            );
        }

        Presenca presenca = new Presenca();

        presenca.setInscricao(inscricao);

        Presenca salva = repository.save(presenca);

        inscricao.setStatus(
                Inscricao.Status.PRESENTE
        );

        inscricaoRepository.save(inscricao);

        return converterParaDTO(salva);
    }

    @Transactional(readOnly = true)
    public List<PresencaDTO> listarPorEvento(
            Integer eventoId) {

        Usuario usuario = usuarioAutenticadoService
                .getUsuarioAutenticado();

        Evento evento = eventoRepository
                .findById(eventoId)
                .orElseThrow(() ->
                        new NotFoundException(
                                "Evento não encontrado"
                        ));

        boolean organizador = evento.getOrganizador()
                .getId()
                .equals(usuario.getId());

        if (!organizador) {

            EventoColaborador colaborador = colaboradorRepository
                    .findByEventoIdAndUsuarioId(
                            eventoId,
                            usuario.getId()
                    )
                    .orElseThrow(() ->
                            new BadRequestException(
                                    "Usuário não possui permissão para visualizar presenças"
                            ));

            if (colaborador.getStatus()
                    != EventoColaborador.Status.ACEITO) {

                throw new BadRequestException(
                        "Usuário não é colaborador ativo"
                );
            }

            if (!Boolean.TRUE.equals(
                    colaborador.getPodeRegistrarPresencas())) {

                throw new BadRequestException(
                        "Este colaborador não possui permissão para visualizar presenças"
                );
            }
        }

        return repository
                .findByInscricaoEventoId(eventoId)
                .stream()
                .map(this::converterParaDTO)
                .toList();
    }

    private PresencaDTO converterParaDTO(
            Presenca presenca) {

        Inscricao inscricao = presenca.getInscricao();

        return new PresencaDTO(
                presenca.getId(),

                inscricao.getId(),

                inscricao.getUsuario().getId(),

                usuarioNomeService.obterNome(
                        inscricao.getUsuario()
                ),

                inscricao.getEvento().getId(),
                inscricao.getEvento().getTitulo(),

                presenca.getDataLeitura()
        );
    }
}
