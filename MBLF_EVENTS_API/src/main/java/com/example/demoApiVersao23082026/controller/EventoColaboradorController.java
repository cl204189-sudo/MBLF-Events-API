package com.example.demoApiVersao23082026.controller;

import com.example.demoApiVersao23082026.dto.ConviteColaboradorDTO;
import com.example.demoApiVersao23082026.dto.EventoColaboradorDTO;
import com.example.demoApiVersao23082026.dto.PermissaoColaboradorDTO;
import com.example.demoApiVersao23082026.service.EventoColaboradorService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import com.example.demoApiVersao23082026.dto.ConviteColaboradorEmailDTO;

import java.util.List;

@RestController
@RequestMapping("/colaboradores")
public class EventoColaboradorController {

    @Autowired
    private EventoColaboradorService service;


    @PostMapping("/evento/{eventoId}/convidar")
    public EventoColaboradorDTO convidar(
            @PathVariable Integer eventoId,
            @RequestBody ConviteColaboradorDTO dto) {

        return service.convidar(
                eventoId,
                dto
        );
    }


    @GetMapping("/meus")
    public List<EventoColaboradorDTO> listarMeus() {

        return service.listarMeus();
    }


    @PutMapping("/evento/{eventoId}/aceitar")
    public EventoColaboradorDTO aceitar(
            @PathVariable Integer eventoId) {

        return service.aceitar(
                eventoId
        );
    }

    @PostMapping("/evento/{eventoId}/convidar-email")
    public EventoColaboradorDTO convidarPorEmail(
            @PathVariable Integer eventoId,
            @RequestBody ConviteColaboradorEmailDTO dto) {

        return service.convidarPorEmail(
                eventoId,
                dto
        );
    }


    @PutMapping("/evento/{eventoId}/recusar")
    public EventoColaboradorDTO recusar(
            @PathVariable Integer eventoId) {

        return service.recusar(
                eventoId
        );
    }


    @PutMapping(
            "/evento/{eventoId}/usuario/{usuarioId}/permissoes"
    )
    public EventoColaboradorDTO alterarPermissoes(
            @PathVariable Integer eventoId,
            @PathVariable Integer usuarioId,
            @RequestBody PermissaoColaboradorDTO dto) {

        return service.alterarPermissoes(
                eventoId,
                usuarioId,
                dto
        );
    }


    @PutMapping("/evento/{eventoId}/usuario/{usuarioId}/remover")
    public EventoColaboradorDTO remover(@PathVariable Integer eventoId,
                                        @PathVariable Integer usuarioId) {
        return service.remover(eventoId, usuarioId);
    }

    @GetMapping("/evento/{eventoId}")
    public List<EventoColaboradorDTO> listar(
            @PathVariable Integer eventoId) {

        return service.listarPorEvento(
                eventoId
        );
    }
}