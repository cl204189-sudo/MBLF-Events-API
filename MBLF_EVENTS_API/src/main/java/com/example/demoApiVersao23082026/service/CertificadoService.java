
package com.example.demoApiVersao23082026.service;

import com.example.demoApiVersao23082026.dto.CertificadoDTO;
import com.example.demoApiVersao23082026.exception.BadRequestException;
import com.example.demoApiVersao23082026.exception.NotFoundException;
import com.example.demoApiVersao23082026.model.*;
import com.example.demoApiVersao23082026.repository.*;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Service
public class CertificadoService {

    @Autowired
    private CertificadoRepository repository;

    @Autowired
    private InscricaoRepository inscricaoRepository;

    @Autowired
    private UsuarioRepository usuarioRepository;

    @Autowired
    private UsuarioAutenticadoService usuarioAutenticadoService;

    @Autowired
    private EventoRepository eventoRepository;

    @Autowired
    private EventoColaboradorRepository colaboradorRepository;

    @Autowired
    private UsuarioNomeService usuarioNomeService;

    public CertificadoDTO validar(String codigo) {

        if (codigo == null ||
                codigo.isBlank() ||
                codigo.length() > 100) {

            throw new BadRequestException(
                    "Código de validação inválido"
            );
        }

        Certificado certificado = repository
                .findByCodigo(codigo.trim())
                .orElseThrow(() ->
                        new NotFoundException(
                                "Certificado não encontrado"
                        )
                );

        Evento eventoValidacao = certificado
                .getInscricao()
                .getEvento();

        if (eventoValidacao.getStatus()
                == Evento.Status.CANCELADO ||
                eventoValidacao.getStatus()
                        == Evento.Status.REMOVIDO_ADMIN) {

            throw new NotFoundException(
                    "Certificado sem validade: evento cancelado ou removido"
            );
        }

        if (certificado.getStatus()
                != Certificado.Status.LIBERADO) {

            throw new NotFoundException(
                    "Certificado não encontrado ou ainda não liberado"
            );
        }

        return converterParaDTO(certificado);
    }

    public CertificadoDTO gerar(Integer inscricaoId) {

        Inscricao inscricao = inscricaoRepository
                .findById(inscricaoId)
                .orElseThrow(() ->
                        new NotFoundException(
                                "Inscrição não encontrada"
                        )
                );

        Usuario solicitante = usuarioAutenticadoService
                .getUsuarioAutenticado();

        Evento eventoInscricao = inscricao.getEvento();

        boolean ehOrganizador = eventoInscricao
                .getOrganizador()
                .getId()
                .equals(solicitante.getId());

        if (!ehOrganizador) {

            EventoColaborador vinculo = colaboradorRepository
                    .findByEventoIdAndUsuarioId(
                            eventoInscricao.getId(),
                            solicitante.getId()
                    )
                    .orElseThrow(() ->
                            new BadRequestException(
                                    "Usuário não possui autorização para gerar certificados"
                            )
                    );

            if (vinculo.getStatus()
                    != EventoColaborador.Status.ACEITO ||
                    !Boolean.TRUE.equals(
                            vinculo.getPodeLiberarCertificados()
                    )) {

                throw new BadRequestException(
                        "Usuário não possui permissão para gerar certificados"
                );
            }
        }

        if (eventoInscricao.getStatus()
                == Evento.Status.CANCELADO ||
                eventoInscricao.getStatus()
                        == Evento.Status.REMOVIDO_ADMIN) {

            throw new BadRequestException(
                    "Não é possível gerar certificado de evento cancelado ou removido"
            );
        }

        if (eventoInscricao.getDataFim() == null ||
                eventoInscricao.getDataFim()
                        .isAfter(LocalDateTime.now())) {

            throw new BadRequestException(
                    "O evento precisa ter terminado para gerar certificados"
            );
        }

        if (inscricao.getStatus()
                != Inscricao.Status.PRESENTE) {

            throw new BadRequestException(
                    "O participante precisa ter presença confirmada"
            );
        }

        if (repository.existsByInscricaoId(inscricaoId)) {

            throw new BadRequestException(
                    "Já existe certificado para esta inscrição"
            );
        }

        Certificado certificado = new Certificado();

        certificado.setInscricao(inscricao);

        certificado.setCodigo(
                UUID.randomUUID().toString()
        );

        certificado.setTituloEvento(
                inscricao.getEvento().getTitulo()
        );

        certificado.setNomeParticipante(
                usuarioNomeService.obterNome(
                        inscricao.getUsuario()
                )
        );

        certificado.setCargaHoraria(
                inscricao.getEvento().getCargaHoraria()
        );

        certificado.setStatus(
                Certificado.Status.PENDENTE
        );

        Certificado salvo = repository.save(certificado);

        return converterParaDTO(salvo);
    }

    public CertificadoDTO liberar(Integer certificadoId) {

        Certificado certificado = repository
                .findById(certificadoId)
                .orElseThrow(() ->
                        new NotFoundException(
                                "Certificado não encontrado"
                        )
                );

        if (certificado.getStatus()
                == Certificado.Status.LIBERADO) {

            throw new BadRequestException(
                    "Este certificado já foi liberado"
            );
        }

        Usuario liberador = usuarioAutenticadoService
                .getUsuarioAutenticado();

        Evento evento = certificado
                .getInscricao()
                .getEvento();

        boolean organizador = evento
                .getOrganizador()
                .getId()
                .equals(liberador.getId());

        if (!organizador) {

            EventoColaborador colaborador = colaboradorRepository
                    .findByEventoIdAndUsuarioId(
                            evento.getId(),
                            liberador.getId()
                    )
                    .orElseThrow(() ->
                            new BadRequestException(
                                    "Usuário não possui autorização para liberar certificados"
                            )
                    );

            if (colaborador.getStatus()
                    != EventoColaborador.Status.ACEITO) {

                throw new BadRequestException(
                        "Usuário não é colaborador ativo"
                );
            }

            if (!Boolean.TRUE.equals(
                    colaborador.getPodeLiberarCertificados()
            )) {

                throw new BadRequestException(
                        "Este colaborador não possui permissão para liberar certificados"
                );
            }
        }

        if (evento.getStatus()
                == Evento.Status.CANCELADO ||
                evento.getStatus()
                        == Evento.Status.REMOVIDO_ADMIN) {

            throw new BadRequestException(
                    "Não é possível liberar certificados de evento cancelado ou removido"
            );
        }

        if (evento.getDataFim() == null ||
                evento.getDataFim().isAfter(LocalDateTime.now())) {

            throw new BadRequestException(
                    "O evento precisa ter terminado para liberar certificados"
            );
        }

        if (certificado.getInscricao().getStatus()
                != Inscricao.Status.PRESENTE) {

            throw new BadRequestException(
                    "A inscrição não possui presença confirmada"
            );
        }

        certificado.setStatus(
                Certificado.Status.LIBERADO
        );

        certificado.setLiberadoPor(liberador);

        certificado.setDataLiberacao(
                LocalDateTime.now()
        );

        return converterParaDTO(
                repository.save(certificado)
        );
    }

    public List<CertificadoDTO> meusCertificados() {

        Usuario usuario = usuarioAutenticadoService
                .getUsuarioAutenticado();

        return repository
                .findByInscricaoUsuarioIdAndStatus(
                        usuario.getId(),
                        Certificado.Status.LIBERADO
                )
                .stream()
                .map(this::converterParaDTO)
                .toList();
    }

    public List<CertificadoDTO> listarPorEvento(
            Integer eventoId) {

        Usuario usuario = usuarioAutenticadoService
                .getUsuarioAutenticado();

        Evento evento = eventoRepository
                .findById(eventoId)
                .orElseThrow(() ->
                        new NotFoundException(
                                "Evento não encontrado"
                        )
                );

        boolean organizador = evento
                .getOrganizador()
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
                                    "Usuário não possui permissão para visualizar certificados"
                            )
                    );

            if (colaborador.getStatus()
                    != EventoColaborador.Status.ACEITO) {

                throw new BadRequestException(
                        "Usuário não é colaborador ativo"
                );
            }

            if (!Boolean.TRUE.equals(
                    colaborador.getPodeLiberarCertificados()
            )) {

                throw new BadRequestException(
                        "Este colaborador não possui permissão para visualizar certificados"
                );
            }
        }

        return repository
                .findByInscricaoEventoId(eventoId)
                .stream()
                .map(this::converterParaDTO)
                .toList();
    }

    private CertificadoDTO converterParaDTO(
            Certificado certificado) {

        Inscricao inscricao = certificado.getInscricao();

        return new CertificadoDTO(
                certificado.getId(),

                inscricao.getId(),

                inscricao.getUsuario().getId(),
                certificado.getNomeParticipante(),

                inscricao.getEvento().getId(),
                certificado.getTituloEvento(),

                certificado.getCodigo(),
                certificado.getCargaHoraria(),

                certificado.getStatus().name(),

                certificado.getDataGeracao(),
                certificado.getDataLiberacao(),

                certificado.getLiberadoPor() != null
                        ? certificado.getLiberadoPor().getId()
                        : null,

                certificado.getLiberadoPor() != null
                        ? usuarioNomeService.obterNome(
                        certificado.getLiberadoPor()
                )
                        : null
        );
    }
}
