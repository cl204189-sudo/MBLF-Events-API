package com.example.demoApiVersao23082026.controller;

import com.example.demoApiVersao23082026.dto.ModeracaoDTO;
import com.example.demoApiVersao23082026.dto.ModeracaoRequestDTO;
import com.example.demoApiVersao23082026.service.ModeracaoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/moderacoes")
public class ModeracaoController {

    @Autowired
    private ModeracaoService service;

    @PostMapping("/evento/{eventoId}/silenciar")
    public ModeracaoDTO silenciar(
            @PathVariable Integer eventoId,
            @RequestBody ModeracaoRequestDTO dto) {

        return service.silenciar(
                eventoId,
                dto.getAlvoId(),
                dto.getMotivo()
        );
    }

    @PostMapping("/evento/{eventoId}/dessilenciar")
    public ModeracaoDTO dessilenciar(
            @PathVariable Integer eventoId,
            @RequestBody ModeracaoRequestDTO dto) {

        return service.dessilenciar(
                eventoId,
                dto.getAlvoId()
        );
    }

    @PostMapping("/evento/{eventoId}/expulsar")
    public ModeracaoDTO expulsar(
            @PathVariable Integer eventoId,
            @RequestBody ModeracaoRequestDTO dto) {

        return service.expulsar(
                eventoId,
                dto.getAlvoId(),
                dto.getMotivo()
        );
    }

    @PostMapping("/evento/{eventoId}/silenciar-todos")
    public ModeracaoDTO silenciarTodos(
            @PathVariable Integer eventoId) {

        return service.silenciarTodos(eventoId);
    }

    @PostMapping("/evento/{eventoId}/liberar-todos")
    public ModeracaoDTO liberarTodos(
            @PathVariable Integer eventoId) {

        return service.liberarTodos(eventoId);
    }

    @GetMapping("/evento/{eventoId}")
    public List<ModeracaoDTO> historico(
            @PathVariable Integer eventoId) {

        return service.listarHistorico(eventoId);
    }
}