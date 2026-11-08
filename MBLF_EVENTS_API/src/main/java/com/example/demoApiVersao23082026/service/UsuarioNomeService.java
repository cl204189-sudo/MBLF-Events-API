
package com.example.demoApiVersao23082026.service;

import com.example.demoApiVersao23082026.model.Usuario;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class UsuarioNomeService {

    @Autowired
    private CriptografiaService criptografiaService;

    public String obterNome(Usuario usuario) {

        if (usuario == null || usuario.getNome() == null) {
            return null;
        }

        if (Boolean.TRUE.equals(usuario.getNomeCriptografado())) {
            return criptografiaService.descriptografar(
                    usuario.getNome()
            );
        }

        return usuario.getNome();
    }
}
