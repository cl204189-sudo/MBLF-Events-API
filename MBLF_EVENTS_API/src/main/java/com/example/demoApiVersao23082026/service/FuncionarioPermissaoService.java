package com.example.demoApiVersao23082026.service;

import com.example.demoApiVersao23082026.dto.FuncionarioPermissaoDTO;
import com.example.demoApiVersao23082026.exception.BadRequestException;
import com.example.demoApiVersao23082026.exception.NotFoundException;
import com.example.demoApiVersao23082026.model.FuncionarioPermissao;
import com.example.demoApiVersao23082026.model.Usuario;
import com.example.demoApiVersao23082026.repository.FuncionarioPermissaoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class FuncionarioPermissaoService {

    @Autowired
    private UsuarioAutenticadoService usuarioAutenticadoService;

    @Autowired
    private FuncionarioPermissaoRepository repository;

    public void exigirPermissao(String nomePermissao) {
        Usuario usuario = usuarioAutenticadoService.getUsuarioAutenticado();
        String perfil = usuario.getPerfil() == null ? "" : usuario.getPerfil().getNome();

        if ("ADMIN".equalsIgnoreCase(perfil)) {
            return;
        }

        if (!"FUNCIONARIO".equalsIgnoreCase(perfil)) {
            throw new BadRequestException("Acesso não autorizado.");
        }

        FuncionarioPermissao permissoes = repository.findById(usuario.getId())
                .orElseThrow(() -> new BadRequestException(
                        "As permissões deste funcionário ainda não foram configuradas."
                ));

        boolean permitido = switch (nomePermissao) {
            case "visualizarUsuarios" -> Boolean.TRUE.equals(permissoes.getVisualizarUsuarios());
            case "bloquearUsuarios" -> Boolean.TRUE.equals(permissoes.getBloquearUsuarios());
            case "visualizarEventos" -> Boolean.TRUE.equals(permissoes.getVisualizarEventos());
            case "moderarEventos" -> Boolean.TRUE.equals(permissoes.getModerarEventos());
            case "visualizarPagamentos" -> Boolean.TRUE.equals(permissoes.getVisualizarPagamentos());
            case "gerenciarPlanos" -> Boolean.TRUE.equals(permissoes.getGerenciarPlanos());
            default -> false;
        };

        if (!permitido) {
            throw new BadRequestException("Você não possui a permissão necessária para esta ação.");
        }
    }

    public FuncionarioPermissaoDTO buscarPermissoes(Integer usuarioId) {
        Usuario autenticado = usuarioAutenticadoService.getUsuarioAutenticado();
        String perfil = autenticado.getPerfil() == null ? "" : autenticado.getPerfil().getNome();

        if (!"ADMIN".equalsIgnoreCase(perfil)
                && !autenticado.getId().equals(usuarioId)) {
            throw new BadRequestException("Você não pode consultar as permissões de outro usuário.");
        }

        FuncionarioPermissao p = repository.findById(usuarioId)
                .orElseThrow(() -> new NotFoundException(
                        "As permissões deste funcionário não foram configuradas."
                ));

        FuncionarioPermissaoDTO dto = new FuncionarioPermissaoDTO();
        dto.setVisualizarUsuarios(Boolean.TRUE.equals(p.getVisualizarUsuarios()));
        dto.setBloquearUsuarios(Boolean.TRUE.equals(p.getBloquearUsuarios()));
        dto.setVisualizarEventos(Boolean.TRUE.equals(p.getVisualizarEventos()));
        dto.setModerarEventos(Boolean.TRUE.equals(p.getModerarEventos()));
        dto.setVisualizarPagamentos(Boolean.TRUE.equals(p.getVisualizarPagamentos()));
        dto.setGerenciarPlanos(Boolean.TRUE.equals(p.getGerenciarPlanos()));
        return dto;
    }
}
