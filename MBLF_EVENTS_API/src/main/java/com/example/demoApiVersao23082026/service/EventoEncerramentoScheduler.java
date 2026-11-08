package com.example.demoApiVersao23082026.service;

import com.example.demoApiVersao23082026.model.Evento;
import com.example.demoApiVersao23082026.repository.EventoRepository;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.time.LocalDateTime;

@Service
public class EventoEncerramentoScheduler {
    private final EventoRepository repository;

    public EventoEncerramentoScheduler(EventoRepository repository) {
        this.repository = repository;
    }

    @Scheduled(fixedDelay = 60000)
    @Transactional
    public void encerrarEventosFinalizados() {
        for (Evento evento : repository.findByStatusAndDataFimLessThanEqual(
                Evento.Status.PUBLICADO, LocalDateTime.now())) {
            evento.setStatus(Evento.Status.ENCERRADO);
            evento.setInscricoesAbertas(false);
        }
    }
}
