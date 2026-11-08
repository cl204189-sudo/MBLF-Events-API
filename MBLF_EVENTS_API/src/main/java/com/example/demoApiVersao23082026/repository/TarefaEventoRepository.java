package com.example.demoApiVersao23082026.repository;

import com.example.demoApiVersao23082026.model.TarefaEvento;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface TarefaEventoRepository
        extends JpaRepository<TarefaEvento, Integer> {

    List<TarefaEvento> findByEventoIdOrderByPrazoAsc(Integer eventoId);

    List<TarefaEvento> findByResponsavelIdOrderByPrazoAsc(Integer responsavelId);
}