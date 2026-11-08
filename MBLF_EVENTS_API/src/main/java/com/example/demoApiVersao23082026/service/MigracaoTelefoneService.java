
package com.example.demoApiVersao23082026.service;

import com.example.demoApiVersao23082026.model.Usuario;
import com.example.demoApiVersao23082026.repository.UsuarioRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class MigracaoTelefoneService {

    private final UsuarioRepository usuarioRepository;
    private final CriptografiaService criptografiaService;

    public MigracaoTelefoneService(
            UsuarioRepository usuarioRepository,
            CriptografiaService criptografiaService) {

        this.usuarioRepository = usuarioRepository;
        this.criptografiaService = criptografiaService;
    }

    @Transactional
    public int migrarTelefones() {

        List<Usuario> usuarios = usuarioRepository.findAll();
        int migrados = 0;

        for (Usuario usuario : usuarios) {

            if (usuario.getTelefone() == null ||
                    Boolean.TRUE.equals(
                            usuario.getTelefoneCriptografado())) {
                continue;
            }

            String telefoneCriptografado =
                    criptografiaService.criptografar(
                            usuario.getTelefone()
                    );

            usuario.setTelefone(telefoneCriptografado);
            usuario.setTelefoneCriptografado(true);

            migrados++;
        }

        usuarioRepository.saveAll(usuarios);

        return migrados;
    }
}
