package com.example.demoApiVersao23082026.repository;

import com.example.demoApiVersao23082026.model.AgendaEvento;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface AgendaEventoRepository
        extends JpaRepository<AgendaEvento, Integer> {

    List<AgendaEvento>
    findByEventoIdOrderByDataInicioAsc(Integer eventoId);
}