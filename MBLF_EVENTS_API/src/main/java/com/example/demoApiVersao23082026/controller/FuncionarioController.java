package com.example.demoApiVersao23082026.controller;

import com.example.demoApiVersao23082026.model.*;
import com.example.demoApiVersao23082026.repository.FuncionarioPermissaoRepository;
import com.example.demoApiVersao23082026.service.UsuarioAutenticadoService;
import com.example.demoApiVersao23082026.exception.BadRequestException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import java.util.*;
import com.example.demoApiVersao23082026.dto.UsuarioDTO;
import com.example.demoApiVersao23082026.dto.EventoDTO;
import com.example.demoApiVersao23082026.service.UsuarioService;
import com.example.demoApiVersao23082026.service.EventoService;
import com.example.demoApiVersao23082026.service.FuncionarioOperacaoService;

@RestController
@RequestMapping("/funcionario")
public class FuncionarioController {
    private final UsuarioAutenticadoService autenticado;
    private final FuncionarioPermissaoRepository permissoes;
    public FuncionarioController(UsuarioAutenticadoService autenticado, FuncionarioPermissaoRepository permissoes) {
        this.autenticado = autenticado;
        this.permissoes = permissoes;
    }
    @org.springframework.beans.factory.annotation.Autowired private UsuarioService usuarios;
    @org.springframework.beans.factory.annotation.Autowired private EventoService eventos;

    private void exigir(String permissao) {
        if (!Boolean.TRUE.equals(minhasPermissoes().get(permissao))) {
            throw new org.springframework.security.access.AccessDeniedException("Permissão insuficiente: " + permissao);
        }
    }

    @GetMapping("/usuarios")
    public List<UsuarioDTO> listarUsuarios() {
        exigir("visualizarUsuarios");
        return usuarios.listarTodos();
    }

    @GetMapping("/eventos")
    public List<EventoDTO> listarEventos() {
        exigir("visualizarEventos");
        return eventos.listarTodos();
    }

    @GetMapping("/minhas-permissoes")
    public Map<String, Boolean> minhasPermissoes() {
        Usuario usuario = autenticado.getUsuarioAutenticado();
        if (!"FUNCIONARIO".equals(usuario.getPerfil().getNome()) || !Boolean.TRUE.equals(usuario.getAtivo())) {
            throw new BadRequestException("Acesso exclusivo para funcionário ativo");
        }
        FuncionarioPermissao p = permissoes.findById(usuario.getId()).orElse(null);
        Map<String, Boolean> r = new LinkedHashMap<>();
        r.put("visualizarUsuarios", p != null && Boolean.TRUE.equals(p.getVisualizarUsuarios()));
        r.put("bloquearUsuarios", p != null && Boolean.TRUE.equals(p.getBloquearUsuarios()));
        r.put("visualizarEventos", p != null && Boolean.TRUE.equals(p.getVisualizarEventos()));
        r.put("moderarEventos", p != null && Boolean.TRUE.equals(p.getModerarEventos()));
        r.put("visualizarPagamentos", p != null && Boolean.TRUE.equals(p.getVisualizarPagamentos()));
        r.put("gerenciarPlanos", p != null && Boolean.TRUE.equals(p.getGerenciarPlanos()));
        return r;
    }

    @Autowired
    private FuncionarioOperacaoService operacoes;

    @PutMapping("/usuarios/{id}/bloquear")
    public void bloquearUsuario(@PathVariable Integer id) {
        operacoes.bloquearUsuario(id);
    }

    @PutMapping("/usuarios/{id}/desbloquear")
    public void desbloquearUsuario(@PathVariable Integer id) {
        operacoes.desbloquearUsuario(id);
    }

    @PutMapping("/eventos/{id}/remover")
    public void removerEvento(
            @PathVariable Integer id,
            @RequestParam String motivo) {

        operacoes.removerEvento(id, motivo);
    }

}
