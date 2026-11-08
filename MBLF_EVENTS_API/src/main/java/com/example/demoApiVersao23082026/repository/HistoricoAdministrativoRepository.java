package com.example.demoApiVersao23082026.repository;

import com.example.demoApiVersao23082026.model.HistoricoAdministrativo;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface HistoricoAdministrativoRepository
        extends JpaRepository<HistoricoAdministrativo, Integer> {

    List<HistoricoAdministrativo>
    findAllByOrderByDataAcaoDesc();
}