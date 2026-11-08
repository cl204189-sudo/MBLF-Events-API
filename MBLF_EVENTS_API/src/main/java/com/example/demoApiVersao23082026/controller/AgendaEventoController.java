package com.example.demoApiVersao23082026.controller;

import com.example.demoApiVersao23082026.dto.AgendaEventoDTO;
import com.example.demoApiVersao23082026.dto.AgendaEventoRequestDTO;
import com.example.demoApiVersao23082026.service.AgendaEventoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/agenda")
public class AgendaEventoController {

    @Autowired
    private AgendaEventoService service;

    @PostMapping("/evento/{eventoId}")
    public AgendaEventoDTO criar(
            @PathVariable Integer eventoId,
            @RequestBody AgendaEventoRequestDTO dto) {

        return service.criar(
                eventoId,
                dto
        );
    }

    @GetMapping("/evento/{eventoId}")
    public List<AgendaEventoDTO> listar(
            @PathVariable Integer eventoId) {

        return service.listar(
                eventoId
        );
    }

    // Programação para participantes inscritos, sem liberar edição da agenda.
    @GetMapping("/evento/{eventoId}/programacao")
    public List<AgendaEventoDTO> programacaoParaInscritos(@PathVariable Integer eventoId) {
        return service.listarParaInscrito(eventoId);
    }

    @DeleteMapping("/{agendaId}")
    public void deletar(
            @PathVariable Integer agendaId) {

        service.deletar(
                agendaId
        );
    }
}