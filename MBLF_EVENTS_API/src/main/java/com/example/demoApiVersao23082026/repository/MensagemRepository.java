package com.example.demoApiVersao23082026.repository;

import com.example.demoApiVersao23082026.model.Mensagem;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface MensagemRepository
        extends JpaRepository<Mensagem, Integer> {

    List<Mensagem> findByEventoIdAndCanalOrderByDataEnvioAsc(Integer eventoId, String canal);

    List<Mensagem> findByEventoIdOrderByDataEnvioAsc(
            Integer eventoId
    );
}