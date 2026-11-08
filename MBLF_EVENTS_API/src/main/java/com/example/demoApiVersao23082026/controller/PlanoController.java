package com.example.demoApiVersao23082026.controller;

import com.example.demoApiVersao23082026.model.Plano;
import com.example.demoApiVersao23082026.service.PlanoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/planos")
public class PlanoController {

    @Autowired
    private PlanoService service;

    @GetMapping("/todos")
    public List<Plano> listarTodos() {
        return service.listarTodos();
    }

    @GetMapping("/ativos")
    public List<Plano> listarAtivos() {
        return service.listarAtivos();
    }

    @GetMapping("/{id}")
    public Plano buscar(
            @PathVariable Integer id) {

        return service.buscar(id);
    }

    @PostMapping("/criar")
    public Plano criar(
            @RequestBody Plano plano) {

        return service.criar(plano);
    }

    @PutMapping("/{id}")
    public Plano atualizar(
            @PathVariable Integer id,
            @RequestBody Plano plano) {

        return service.atualizar(id, plano);
    }

    @PutMapping("/{id}/desativar")
    public void desativar(
            @PathVariable Integer id) {

        service.desativar(id);
    }
}