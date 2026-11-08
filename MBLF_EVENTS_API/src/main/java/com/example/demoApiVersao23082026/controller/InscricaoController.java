package com.example.demoApiVersao23082026.controller;

import com.example.demoApiVersao23082026.dto.InscricaoDTO;
import com.example.demoApiVersao23082026.dto.InscricaoRequestDTO;
import com.example.demoApiVersao23082026.service.InscricaoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/inscricoes")
public class InscricaoController {

    @Autowired
    private InscricaoService service;

    @PostMapping
    public InscricaoDTO criar(
            @RequestBody InscricaoRequestDTO dto) {

        return service.criar(dto);
    }

    @GetMapping("/{id}")
    public InscricaoDTO buscar(
            @PathVariable Integer id) {

        return service.buscar(id);
    }

    @GetMapping("/minhas")
    public List<InscricaoDTO> minhas() {

        return service.minhasInscricoes();
    }

    @GetMapping("/evento/{eventoId}")
    public List<InscricaoDTO> listarPorEvento(
            @PathVariable Integer eventoId) {

        return service.listarPorEvento(
                eventoId
        );
    }

    @PutMapping("/{id}/cancelar")
    public InscricaoDTO cancelar(
            @PathVariable Integer id) {

        return service.cancelar(id);
    }
}