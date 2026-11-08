package com.example.demoApiVersao23082026.service;

import com.example.demoApiVersao23082026.dto.PagamentoDTO;
import com.example.demoApiVersao23082026.dto.PagamentoRequestDTO;
import com.example.demoApiVersao23082026.exception.BadRequestException;
import com.example.demoApiVersao23082026.exception.NotFoundException;
import com.example.demoApiVersao23082026.model.Evento;
import com.example.demoApiVersao23082026.model.Pagamento;
import com.example.demoApiVersao23082026.model.Plano;
import com.example.demoApiVersao23082026.model.Usuario;
import com.example.demoApiVersao23082026.repository.EventoRepository;
import com.example.demoApiVersao23082026.repository.PagamentoRepository;
import com.example.demoApiVersao23082026.repository.PlanoRepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class PagamentoService {

    @Autowired
    private PagamentoRepository repository;

    @Autowired
    private EventoRepository eventoRepository;

    @Autowired
    private UsuarioAutenticadoService usuarioAutenticadoService;

    @Autowired
    private PlanoRepository planoRepository;

    @Autowired
    private UsuarioNomeService usuarioNomeService;

    @Transactional(readOnly = true)
    public List<PagamentoDTO> listarTodos() {

        return repository.findAll()
                .stream()
                .map(this::converterParaDTO)
                .toList();
    }

    @Transactional(readOnly = true)
    public PagamentoDTO buscar(Integer id) {

        Usuario usuarioAutenticado = usuarioAutenticadoService
                .getUsuarioAutenticado();

        Pagamento pagamento = repository.findById(id)
                .orElseThrow(() ->
                        new NotFoundException(
                                "Pagamento não encontrado"
                        ));

        boolean proprietario = pagamento.getUsuario()
                .getId()
                .equals(usuarioAutenticado.getId());

        boolean administrador = usuarioAutenticado.getPerfil() != null
                && "ADMIN".equals(
                usuarioAutenticado.getPerfil().getNome()
        );

        if (!proprietario && !administrador) {
            throw new NotFoundException(
                    "Pagamento não encontrado"
            );
        }

        return converterParaDTO(pagamento);
    }

    @Transactional
    public PagamentoDTO simularPagamento(
            PagamentoRequestDTO dto) {

        if (dto == null ||
                dto.getEventoId() == null ||
                dto.getPlanoId() == null) {

            throw new BadRequestException(
                    "Evento e plano são obrigatórios"
            );
        }

        Usuario usuario = usuarioAutenticadoService
                .getUsuarioAutenticado();

        Evento evento = eventoRepository
                .findById(dto.getEventoId())
                .orElseThrow(() ->
                        new NotFoundException(
                                "Evento não encontrado"
                        ));

        Plano plano = planoRepository
                .findById(dto.getPlanoId())
                .orElseThrow(() ->
                        new NotFoundException(
                                "Plano não encontrado"
                        ));

        if (!Boolean.TRUE.equals(plano.getAtivo())) {
            throw new BadRequestException(
                    "Este plano está desativado"
            );
        }

        if (!evento.getOrganizador()
                .getId()
                .equals(usuario.getId())) {

            throw new BadRequestException(
                    "Somente o organizador pode comprar planos para este evento"
            );
        }

        Pagamento pagamento = new Pagamento();

        pagamento.setUsuario(usuario);
        pagamento.setEvento(evento);
        pagamento.setPlano(plano);

        pagamento.setValorPago(plano.getValor());

        pagamento.setStatus(
                Pagamento.Status.APROVADO
        );

        pagamento.setDataAprovacao(
                LocalDateTime.now()
        );

        aplicarPlano(evento, plano);

        eventoRepository.save(evento);

        Pagamento salvo = repository.save(pagamento);

        return converterParaDTO(salvo);
    }

    private void aplicarPlano(
            Evento evento,
            Plano plano) {

        LocalDateTime agora = LocalDateTime.now();

        if (plano.getTipo() == Plano.Tipo.HUB) {

            LocalDateTime base;

            if (evento.getHubAte() != null &&
                    evento.getHubAte().isAfter(agora)) {

                base = evento.getHubAte();

            } else {
                base = agora;
            }

            evento.setHubAte(
                    base.plusDays(
                            plano.getDuracaoDias()
                    )
            );
        }

        if (plano.getTipo() == Plano.Tipo.PUBLICACAO) {

            LocalDateTime base;

            if (evento.getPublicacaoAte() != null &&
                    evento.getPublicacaoAte().isAfter(agora)) {

                base = evento.getPublicacaoAte();

            } else {
                base = agora;
            }

            evento.setPublicacaoAte(
                    base.plusDays(
                            plano.getDuracaoDias()
                    )
            );
        }
    }

    private PagamentoDTO converterParaDTO(
            Pagamento pagamento) {

        return new PagamentoDTO(
                pagamento.getId(),

                pagamento.getUsuario().getId(),
                usuarioNomeService.obterNome(
                        pagamento.getUsuario()
                ),

                pagamento.getEvento().getId(),
                pagamento.getEvento().getTitulo(),

                pagamento.getPlano().getId(),
                pagamento.getPlano().getNome(),
                pagamento.getPlano()
                        .getTipo()
                        .name(),

                pagamento.getValorPago(),

                pagamento.getStatus().name(),

                pagamento.getDataCriacao(),
                pagamento.getDataAprovacao()
        );
    }
}
