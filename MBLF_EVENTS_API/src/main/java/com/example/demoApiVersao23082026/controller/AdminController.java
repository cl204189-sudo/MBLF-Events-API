package com.example.demoApiVersao23082026.controller;

import com.example.demoApiVersao23082026.dto.FuncionarioPermissaoDTO;
import com.example.demoApiVersao23082026.service.AdminService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/admin")
public class AdminController {

    @Autowired
    private AdminService service;

    @Autowired
    private com.example.demoApiVersao23082026.service.FuncionarioPermissaoService permissaoService;
    @GetMapping("/usuarios/{usuarioId}/permissoes")
    public FuncionarioPermissaoDTO buscarPermissoes(@PathVariable Integer usuarioId) {
        return permissaoService.buscarPermissoes(usuarioId);
    }


    @PutMapping("/usuarios/{usuarioId}/bloquear")
    public void bloquearUsuario(
            @PathVariable Integer usuarioId) {

        service.bloquearUsuario(
                usuarioId
        );
    }

    @PutMapping("/usuarios/{usuarioId}/desbloquear")
    public void desbloquearUsuario(
            @PathVariable Integer usuarioId) {

        service.desbloquearUsuario(
                usuarioId
        );
    }

    @PutMapping("/usuarios/{usuarioId}/funcionario")
    public void tornarFuncionario(
            @PathVariable Integer usuarioId,
            @RequestBody FuncionarioPermissaoDTO dto) {

        service.tornarFuncionario(
                usuarioId,
                dto
        );
    }

    @PutMapping("/eventos/{eventoId}/remover")
    public void removerEvento(
            @PathVariable Integer eventoId,
            @RequestParam String motivo) {

        service.removerEvento(
                eventoId,
                motivo
        );
    }
}