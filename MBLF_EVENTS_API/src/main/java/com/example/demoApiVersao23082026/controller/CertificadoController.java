package com.example.demoApiVersao23082026.controller;

import com.example.demoApiVersao23082026.dto.CertificadoDTO;
import com.example.demoApiVersao23082026.service.CertificadoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/certificados")
public class CertificadoController {

    @Autowired
    private CertificadoService service;

    @GetMapping("/validar/{codigo}")
    public CertificadoDTO validar(@PathVariable String codigo) {
        return service.validar(codigo);
    }

    @PostMapping("/gerar/{inscricaoId}")
    public CertificadoDTO gerar(
            @PathVariable Integer inscricaoId) {

        return service.gerar(inscricaoId);
    }

    @PutMapping("/{certificadoId}/liberar")
    public CertificadoDTO liberar(
            @PathVariable Integer certificadoId) {

        return service.liberar(
                certificadoId
        );
    }

    @GetMapping("/meus")
    public List<CertificadoDTO> meusCertificados() {

        return service.meusCertificados();
    }

    @GetMapping("/evento/{eventoId}")
    public List<CertificadoDTO> listarPorEvento(
            @PathVariable Integer eventoId) {

        return service.listarPorEvento(
                eventoId
        );
    }
}