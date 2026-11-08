package com.example.demoApiVersao23082026.controller;

import com.example.demoApiVersao23082026.dto.PagamentoDTO;
import com.example.demoApiVersao23082026.dto.PagamentoRequestDTO;
import com.example.demoApiVersao23082026.service.PagamentoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/pagamentos")
public class PagamentoController {

    @Autowired
    private PagamentoService service;

    @GetMapping("/todos")
    public List<PagamentoDTO> listarTodos() {
        return service.listarTodos();
    }

    @GetMapping("/{id}")
    public PagamentoDTO buscar(
            @PathVariable Integer id) {

        return service.buscar(id);
    }

    @PostMapping("/simular")
    public PagamentoDTO simular(
            @RequestBody PagamentoRequestDTO dto) {

        return service.simularPagamento(dto);
    }
}