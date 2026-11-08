package com.example.demoApiVersao23082026.controller;

import com.example.demoApiVersao23082026.dto.UsuarioDTO;
import com.example.demoApiVersao23082026.model.Usuario;
import com.example.demoApiVersao23082026.service.UsuarioService;
import org.springframework.beans.factory.annotation.Autowired;
import com.example.demoApiVersao23082026.dto.AlterarSenhaDTO;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/usuarios")
@CrossOrigin(origins = "*")
public class UsuarioController {

    @Autowired
    private UsuarioService service;

    @GetMapping("/todos")
    public List<UsuarioDTO> listar() {
        return service.listarTodos();
    }

    @GetMapping("/{id}")
    public UsuarioDTO buscar(
            @PathVariable Integer id) {

        return service.buscar(id);
    }

    @PutMapping("/me/senha")
    public void alterarMinhaSenha(
            @RequestBody AlterarSenhaDTO dto) {

        service.alterarMinhaSenha(dto);
    }

    @GetMapping("/me")
    public UsuarioDTO buscarMeuPerfil() {

        return service.buscarMeuPerfil();
    }

    @PutMapping("/me")
    public UsuarioDTO atualizarMeuPerfil(
            @RequestBody Usuario usuario) {

        return service.atualizarMeuPerfil(
                usuario
        );
    }

    @PostMapping("/criar")
    public UsuarioDTO criar(
            @RequestBody Usuario usuario) {

        return service.criar(usuario);
    }

    @PutMapping("/{id}")
    public UsuarioDTO atualizar(
            @PathVariable Integer id,
            @RequestBody Usuario usuario) {

        return service.atualizar(id, usuario);
    }

    @DeleteMapping("/{id}")
    public void deletar(
            @PathVariable Integer id) {

        service.deletar(id);
    }
}