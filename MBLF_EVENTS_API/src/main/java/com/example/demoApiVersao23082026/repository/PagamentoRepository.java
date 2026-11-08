package com.example.demoApiVersao23082026.repository;

import com.example.demoApiVersao23082026.model.Pagamento;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface PagamentoRepository
        extends JpaRepository<Pagamento, Integer> {

    List<Pagamento> findByUsuarioId(Integer usuarioId);

    List<Pagamento> findByEventoId(Integer eventoId);
}