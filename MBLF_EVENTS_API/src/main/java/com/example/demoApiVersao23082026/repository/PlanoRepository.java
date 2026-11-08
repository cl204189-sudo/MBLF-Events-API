package com.example.demoApiVersao23082026.repository;

import com.example.demoApiVersao23082026.model.Plano;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface PlanoRepository extends JpaRepository<Plano, Integer> {

    List<Plano> findByAtivoTrue();

    List<Plano> findByTipoAndAtivoTrue(Plano.Tipo tipo);
}