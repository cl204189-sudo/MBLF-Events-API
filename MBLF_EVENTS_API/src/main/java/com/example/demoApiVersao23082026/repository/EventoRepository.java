package com.example.demoApiVersao23082026.repository;

import com.example.demoApiVersao23082026.model.Evento;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Lock;
import jakarta.persistence.LockModeType;
import org.springframework.data.repository.query.Param;
import org.springframework.data.jpa.repository.Query;
import java.util.Optional;

import java.time.LocalDateTime;
import java.util.List;

public interface EventoRepository extends JpaRepository<Evento, Integer> {

    @Lock(LockModeType.PESSIMISTIC_WRITE)
    @Query("select e from Evento e where e.id = :id")
    Optional<Evento> buscarParaAtualizacao(@Param("id") Integer id);

    List<Evento> findByOrganizadorId(Integer organizadorId);

    List<Evento> findByStatus(Evento.Status status);

    List<Evento> findByStatusAndDataInicioBetween(
            Evento.Status status, LocalDateTime inicio, LocalDateTime fim);

    List<Evento> findByStatusAndDataFimLessThanEqual(
            Evento.Status status, LocalDateTime limite);

    List<Evento> findByStatusAndInscricoesAbertasTrueAndPublicacaoAteAfter(
            Evento.Status status,
            LocalDateTime agora
    );
}