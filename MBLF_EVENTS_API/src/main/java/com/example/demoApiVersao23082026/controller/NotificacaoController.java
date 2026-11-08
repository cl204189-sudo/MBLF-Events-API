package com.example.demoApiVersao23082026.controller;

import com.example.demoApiVersao23082026.dto.NotificacaoDTO;
import com.example.demoApiVersao23082026.service.NotificacaoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/notificacoes")
public class NotificacaoController {

    @Autowired
    private NotificacaoService service;

    @GetMapping("/minhas")
    public List<NotificacaoDTO> minhas() {
        return service.minhasNotificacoes();
    }

    @PutMapping("/{id}/lida")
    public NotificacaoDTO marcarComoLida(
            @PathVariable Integer id) {

        return service.marcarComoLida(id);
    }
}