package com.example.demoApiVersao23082026.service;

import com.example.demoApiVersao23082026.model.Usuario;
import com.example.demoApiVersao23082026.repository.UsuarioRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.*;
import org.springframework.stereotype.Service;

@Service
public class CustomUserDetailsService implements UserDetailsService {

    @Autowired
    private UsuarioRepository repository;

    @Autowired
    private EmailHashService emailHashService;

    @Override
    public UserDetails loadUserByUsername(String email)
            throws UsernameNotFoundException {

        if (email == null || email.isBlank()) {
            throw new UsernameNotFoundException(
                    "Usuário não encontrado"
            );
        }

        String emailNormalizado = email.trim();
        String hash = emailHashService.gerarHash(emailNormalizado);

        Usuario usuario = repository.findByEmailHash(hash)
                .or(() -> repository.findByEmail(emailNormalizado))
                .orElseThrow(() ->
                        new UsernameNotFoundException(
                                "Usuário não encontrado"
                        )
                );

        return criarUserDetails(usuario);
    }

    public UserDetails loadUserById(Integer id)
            throws UsernameNotFoundException {

        Usuario usuario = repository.findById(id)
                .orElseThrow(() ->
                        new UsernameNotFoundException(
                                "Usuário não encontrado"
                        )
                );

        return criarUserDetails(usuario);
    }

    private UserDetails criarUserDetails(Usuario usuario) {

        return User.builder()
                .username(usuario.getId().toString())
                .password(usuario.getSenha())
                .authorities(
                        "ROLE_" + usuario.getPerfil().getNome()
                )
                .disabled(
                        !Boolean.TRUE.equals(usuario.getAtivo())
                )
                .build();
    }
}
