package com.example.demoApiVersao23082026.repository;

import com.example.demoApiVersao23082026.model.Inscricao;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import jakarta.persistence.LockModeType;

import java.util.List;
import java.util.Optional;

public interface InscricaoRepository
        extends JpaRepository<Inscricao, Integer> {

    @Lock(LockModeType.PESSIMISTIC_WRITE)
    @Query("select i from Inscricao i where i.id = :id")
    Optional<Inscricao> buscarPorIdParaAtualizacao(@Param("id") Integer id);

    @Lock(LockModeType.PESSIMISTIC_WRITE)
    @Query("select i from Inscricao i where i.tokenQr = :token")
    Optional<Inscricao> buscarPorTokenParaAtualizacao(@Param("token") String token);

    boolean existsByUsuarioIdAndEventoIdAndStatusIn(Integer usuarioId, Integer eventoId,
            java.util.Collection<Inscricao.Status> statuses);

    boolean existsByUsuarioIdAndEventoId(
            Integer usuarioId,
            Integer eventoId
    );

    Optional<Inscricao> findByTokenQr(
            String tokenQr
    );

    List<Inscricao> findByUsuarioId(
            Integer usuarioId
    );

    List<Inscricao> findByEventoId(
            Integer eventoId
    );
}