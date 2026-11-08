package com.example.demoApiVersao23082026.service;

import com.example.demoApiVersao23082026.exception.NotFoundException;
import com.example.demoApiVersao23082026.model.Usuario;
import com.example.demoApiVersao23082026.repository.UsuarioRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

@Service
public class UsuarioAutenticadoService {

    @Autowired
    private UsuarioRepository usuarioRepository;

    public Usuario getUsuarioAutenticado() {

        Authentication authentication =
                SecurityContextHolder
                        .getContext()
                        .getAuthentication();

        if (authentication == null ||
                !authentication.isAuthenticated() ||
                "anonymousUser".equals(authentication.getPrincipal())) {

            throw new org.springframework.security.authentication
                    .AuthenticationCredentialsNotFoundException(
                    "Usuário não autenticado"
            );
        }

        // Agora o nome da autenticação contém o ID
        Integer usuarioId;

        try {
            usuarioId = Integer.valueOf(authentication.getName());
        } catch (NumberFormatException e) {
            throw new org.springframework.security.authentication
                    .AuthenticationCredentialsNotFoundException(
                    "Identificador de autenticação inválido"
            );
        }

        return usuarioRepository
                .findById(usuarioId)
                .orElseThrow(() ->
                        new NotFoundException(
                                "Usuário autenticado não encontrado"
                        ));
    }

    public Integer getUsuarioId() {
        return getUsuarioAutenticado().getId();
    }
}
