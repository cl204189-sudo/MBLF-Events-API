package com.example.demoApiVersao23082026.controller;

import com.example.demoApiVersao23082026.dto.MensagemDTO;
import com.example.demoApiVersao23082026.dto.MensagemRequestDTO;
import com.example.demoApiVersao23082026.service.MensagemService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/mensagens")
public class MensagemController {

    @Autowired
    private MensagemService service;

    @PostMapping("/evento/{eventoId}")
    public MensagemDTO enviar(
            @PathVariable Integer eventoId,
            @RequestBody MensagemRequestDTO dto) {

        return service.enviar(
                eventoId,
                dto
        );
    }

    @GetMapping("/evento/{eventoId}")
    public List<MensagemDTO> listar(
            @PathVariable Integer eventoId) {

        return service.listarPorEvento(
                eventoId
        );
    }
    // Compatibilidade com clientes antigos: /global equivale ao canal GERAL.
    @PostMapping("/evento/{eventoId}/global")
    public MensagemDTO enviarGlobal(@PathVariable Integer eventoId,
                                    @RequestBody MensagemRequestDTO dto) {
        return service.enviarCanal(eventoId, "GERAL", dto);
    }

    @GetMapping("/evento/{eventoId}/global")
    public List<MensagemDTO> listarGlobal(@PathVariable Integer eventoId) {
        return service.listarCanal(eventoId, "GERAL");
    }

    @PostMapping("/evento/{eventoId}/canal/{canal}")
    public MensagemDTO enviarCanal(@PathVariable Integer eventoId,
            @PathVariable String canal, @RequestBody MensagemRequestDTO dto) {
        return service.enviarCanal(eventoId, canal.toUpperCase(java.util.Locale.ROOT), dto);
    }

    @GetMapping("/evento/{eventoId}/canal/{canal}")
    public List<MensagemDTO> listarCanal(@PathVariable Integer eventoId, @PathVariable String canal) {
        return service.listarCanal(eventoId, canal.toUpperCase(java.util.Locale.ROOT));
    }
}