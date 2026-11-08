package com.example.demoApiVersao23082026.service;

import com.example.demoApiVersao23082026.dto.RelatorioEventoDTO;
import com.example.demoApiVersao23082026.exception.BadRequestException;
import com.example.demoApiVersao23082026.exception.NotFoundException;
import com.example.demoApiVersao23082026.model.Evento;
import com.example.demoApiVersao23082026.model.EventoColaborador;
import com.example.demoApiVersao23082026.model.Inscricao;
import com.example.demoApiVersao23082026.model.Usuario;
import com.example.demoApiVersao23082026.repository.EventoColaboradorRepository;
import com.example.demoApiVersao23082026.repository.EventoRepository;
import com.example.demoApiVersao23082026.repository.InscricaoRepository;
import com.example.demoApiVersao23082026.repository.ModeracaoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class RelatorioEventoService {
    @Autowired private EventoRepository eventoRepository;
    @Autowired private InscricaoRepository inscricaoRepository;
    @Autowired private EventoColaboradorRepository colaboradorRepository;
    @Autowired private ModeracaoRepository moderacaoRepository;
    @Autowired private UsuarioAutenticadoService usuarioAutenticadoService;

    public RelatorioEventoDTO gerar(Integer eventoId) {
        Evento evento = eventoRepository.findById(eventoId)
                .orElseThrow(() -> new NotFoundException("Evento não encontrado"));
        Usuario atual = usuarioAutenticadoService.getUsuarioAutenticado();
        if (!evento.getOrganizador().getId().equals(atual.getId())) {
            EventoColaborador colaborador = colaboradorRepository
                    .findByEventoIdAndUsuarioId(eventoId, atual.getId())
                    .orElseThrow(() -> new BadRequestException("Sem permissão para ver relatórios"));
            if (colaborador.getStatus() != EventoColaborador.Status.ACEITO
                    || !Boolean.TRUE.equals(colaborador.getPodeVerRelatorios())) {
                throw new BadRequestException("Este colaborador não possui permissão para ver relatórios");
            }
        }

        var inscricoes = inscricaoRepository.findByEventoId(eventoId);
        long total = inscricoes.size();
        long ativas = inscricoes.stream().filter(i -> i.getStatus() == Inscricao.Status.INSCRITO
                || i.getStatus() == Inscricao.Status.PRESENTE).count();
        long canceladas = inscricoes.stream().filter(i -> i.getStatus() == Inscricao.Status.CANCELADO).count();
        long presentes = inscricoes.stream().filter(i -> i.getStatus() == Inscricao.Status.PRESENTE).count();
        long moderacoes = moderacaoRepository.findByEventoIdOrderByDataAcaoDesc(eventoId).size();

        return new RelatorioEventoDTO(evento.getId(), evento.getTitulo(), total, ativas,
                canceladas, presentes, 0L, moderacoes);
    }
}
