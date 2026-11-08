package com.example.demoApiVersao23082026.repository;

import com.example.demoApiVersao23082026.model.Presenca;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface PresencaRepository
        extends JpaRepository<Presenca, Integer> {

    boolean existsByInscricaoId(Integer inscricaoId);
    Optional<Presenca> findByInscricaoId(Integer inscricaoId);

    List<Presenca> findByInscricaoEventoId(Integer eventoId);
}