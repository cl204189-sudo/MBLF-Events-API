package com.example.demoApiVersao23082026.repository;

import com.example.demoApiVersao23082026.model.Certificado;
import com.example.demoApiVersao23082026.model.Inscricao;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface CertificadoRepository
        extends JpaRepository<Certificado, Integer> {

    Optional<Certificado> findByCodigo(String codigo);

    boolean existsByInscricaoId(Integer inscricaoId);

    Optional<Certificado> findByInscricaoId(Integer inscricaoId);

    List<Certificado> findByInscricaoUsuarioIdAndStatus(
            Integer usuarioId,
            Certificado.Status status
    );

    List<Certificado> findByInscricaoEventoId(
            Integer eventoId
    );
}