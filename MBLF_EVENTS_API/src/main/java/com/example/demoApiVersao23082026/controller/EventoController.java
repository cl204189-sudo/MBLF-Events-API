package com.example.demoApiVersao23082026.controller;

import com.example.demoApiVersao23082026.dto.EventoDTO;
import com.example.demoApiVersao23082026.model.Evento;
import com.example.demoApiVersao23082026.service.EventoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/eventos")
public class EventoController {

    @Autowired
    private EventoService service;

    @GetMapping("/todos")
    public List<EventoDTO> listarTodos() {
        return service.listarTodos();
    }

    @GetMapping("/{id}")
    public EventoDTO buscar(
            @PathVariable Integer id) {

        return service.buscar(id);
    }

    @GetMapping("/organizador/{organizadorId}")
    public List<EventoDTO> listarPorOrganizador(
            @PathVariable Integer organizadorId) {

        return service.listarPorOrganizador(
                organizadorId
        );
    }

    @PostMapping("/criar")
    public EventoDTO criar(
            @RequestBody Evento evento) {

        return service.criar(evento);
    }

    @PutMapping("/{id}")
    public EventoDTO atualizar(
            @PathVariable Integer id,
            @RequestBody Evento evento) {

        return service.atualizar(id, evento);
    }

    @PutMapping("/{id}/abrir-inscricoes")
    public EventoDTO abrirInscricoes(
            @PathVariable Integer id) {

        return service.abrirInscricoes(id);
    }

    @PutMapping("/{id}/fechar-inscricoes")
    public EventoDTO fecharInscricoes(
            @PathVariable Integer id) {

        return service.fecharInscricoes(id);
    }

    @GetMapping("/publicos")
    public List<EventoDTO> listarPublicos() {
        return service.listarPublicos();
    }

    @PutMapping("/{id}/cancelar")
    public EventoDTO cancelar(@PathVariable Integer id) {
        return service.cancelar(id);
    }

    @DeleteMapping("/{id}")
    public void deletar(
            @PathVariable Integer id) {

        service.deletar(id);
    }
}