package com.example.demoApiVersao23082026.repository;

import com.example.demoApiVersao23082026.model.Moderacao;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ModeracaoRepository
        extends JpaRepository<Moderacao, Integer> {

    List<Moderacao> findByEventoIdOrderByDataAcaoDesc(
            Integer eventoId
    );
}