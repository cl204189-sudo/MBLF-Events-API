package com.example.demoApiVersao23082026.controller;

import com.example.demoApiVersao23082026.model.Perfil;
import com.example.demoApiVersao23082026.service.PerfilService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/perfis")
public class PerfilController {

    @Autowired
    private PerfilService service;

    @GetMapping("/todos")
    public List<Perfil> listar() {
        return service.listarTodos();
    }

    @GetMapping("/{id}")
    public Perfil buscar(@PathVariable Integer id) {
        return service.buscar(id);
    }

    @PostMapping("/criar")
    public Perfil criar(@RequestBody Perfil perfil) {
        return service.criar(perfil);
    }

    @PutMapping("/{id}")
    public Perfil atualizar(
            @PathVariable Integer id,
            @RequestBody Perfil perfil) {

        return service.atualizar(id, perfil);
    }

    @DeleteMapping("/{id}")
    public void deletar(@PathVariable Integer id) {
        service.deletar(id);
    }
}