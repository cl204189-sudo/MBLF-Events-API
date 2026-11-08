package com.example.demoApiVersao23082026.controller;

import com.example.demoApiVersao23082026.dto.TarefaEventoDTO;
import com.example.demoApiVersao23082026.dto.TarefaEventoRequestDTO;
import com.example.demoApiVersao23082026.model.TarefaEvento;
import com.example.demoApiVersao23082026.service.TarefaEventoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/tarefas")
public class TarefaEventoController {

    @Autowired
    private TarefaEventoService service;

    @PostMapping("/evento/{eventoId}")
    public TarefaEventoDTO criar(
            @PathVariable Integer eventoId,
            @RequestBody TarefaEventoRequestDTO dto) {

        return service.criar(
                eventoId,
                dto
        );
    }

    @GetMapping("/evento/{eventoId}")
    public List<TarefaEventoDTO> listar(
            @PathVariable Integer eventoId) {

        return service.listarPorEvento(
                eventoId
        );
    }

    @PutMapping("/{tarefaId}/status")
    public TarefaEventoDTO alterarStatus(
            @PathVariable Integer tarefaId,
            @RequestParam TarefaEvento.Status status) {

        return service.alterarStatus(
                tarefaId,
                status
        );
    }

    @DeleteMapping("/{tarefaId}")
    public void deletar(
            @PathVariable Integer tarefaId) {

        service.deletar(
                tarefaId
        );
    }
}