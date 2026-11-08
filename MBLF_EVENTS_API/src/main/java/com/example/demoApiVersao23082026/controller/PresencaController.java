package com.example.demoApiVersao23082026.controller;

import com.example.demoApiVersao23082026.dto.PresencaDTO;
import com.example.demoApiVersao23082026.dto.PresencaRequestDTO;
import com.example.demoApiVersao23082026.service.PresencaService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/presencas")
public class PresencaController {

    @Autowired
    private PresencaService service;

    @PostMapping("/registrar")
    public PresencaDTO registrar(
            @RequestBody PresencaRequestDTO dto) {

        return service.registrar(
                dto.getToken(), dto.getEventoId()
        );
    }

    @PutMapping("/evento/{eventoId}/inscricao/{inscricaoId}/manual")
    public void alterarManualmente(@PathVariable Integer eventoId, @PathVariable Integer inscricaoId,
                                   @RequestBody java.util.Map<String,Object> body) {
        if (!(body.get("presente") instanceof Boolean presente))
            throw new com.example.demoApiVersao23082026.exception.BadRequestException("Campo presente obrigatório");
        service.alterarManualmente(eventoId, inscricaoId, presente,
            body.get("motivo") instanceof String m ? m : null);
    }

    @GetMapping("/evento/{eventoId}")
    public List<PresencaDTO> listarPorEvento(
            @PathVariable Integer eventoId) {

        return service.listarPorEvento(
                eventoId
        );
    }
}