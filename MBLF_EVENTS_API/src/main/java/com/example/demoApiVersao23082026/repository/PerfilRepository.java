package com.example.demoApiVersao23082026.repository;

import com.example.demoApiVersao23082026.model.Perfil;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface PerfilRepository extends JpaRepository<Perfil, Integer> {

    boolean existsByNome(String nome);
    Optional<Perfil> findByNome(String nome);
}