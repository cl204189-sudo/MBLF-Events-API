package com.example.demoApiVersao23082026.controller;

import com.example.demoApiVersao23082026.dto.RelatorioEventoDTO;
import com.example.demoApiVersao23082026.dto.EventoDTO;
import com.example.demoApiVersao23082026.service.EventoService;
import java.util.List;
import com.example.demoApiVersao23082026.service.RelatorioEventoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/relatorios")
public class RelatorioEventoController {
    @Autowired private RelatorioEventoService service;
    @Autowired private EventoService eventoService;

    // Consulta administrativa para o visualizador de relatórios.
    @GetMapping("/eventos")
    public List<EventoDTO> listarEventos() {
        return eventoService.listarTodos();
    }

    @GetMapping("/evento/{eventoId}")
    public RelatorioEventoDTO gerar(@PathVariable Integer eventoId) {
        return service.gerar(eventoId);
    }
}
