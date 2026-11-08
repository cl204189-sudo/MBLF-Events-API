package com.example.demoApiVersao23082026.repository;

import com.example.demoApiVersao23082026.model.EventoColaborador;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface EventoColaboradorRepository
        extends JpaRepository<EventoColaborador, Integer> {

    long countByEventoIdAndPodeLiberarCertificadosTrue(
            Integer eventoId
    );

    Optional<EventoColaborador>
    findByEventoIdAndUsuarioId(
            Integer eventoId,
            Integer usuarioId
    );

    boolean existsByEventoIdAndUsuarioId(
            Integer eventoId,
            Integer usuarioId
    );

    List<EventoColaborador>
    findByEventoId(Integer eventoId);

    List<EventoColaborador>
    findByUsuarioId(Integer usuarioId);
}