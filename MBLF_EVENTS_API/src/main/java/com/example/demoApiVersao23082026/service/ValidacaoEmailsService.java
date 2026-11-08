
package com.example.demoApiVersao23082026.service;

import com.example.demoApiVersao23082026.model.Usuario;
import com.example.demoApiVersao23082026.repository.UsuarioRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class ValidacaoEmailsService {

    private final UsuarioRepository repository;
    private final CriptografiaService criptografiaService;
    private final EmailHashService emailHashService;

    public ValidacaoEmailsService(
            UsuarioRepository repository,
            CriptografiaService criptografiaService,
            EmailHashService emailHashService) {

        this.repository = repository;
        this.criptografiaService = criptografiaService;
        this.emailHashService = emailHashService;
    }

    @Transactional(readOnly = true)
    public int validarTodos() {

        int validados = 0;

        for (Usuario usuario : repository.findAll()) {

            if (!Boolean.TRUE.equals(
                    usuario.getEmailCriptografado())) {
                throw new IllegalStateException(
                        "Email não criptografado: ID "
                                + usuario.getId());
            }

            String emailOriginal =
                    criptografiaService.descriptografar(
                            usuario.getEmail());

            String hashCalculado =
                    emailHashService.gerarHash(emailOriginal);

            if (!hashCalculado.equals(usuario.getEmailHash())) {
                throw new IllegalStateException(
                        "Hash incorreto: ID "
                                + usuario.getId());
            }

            validados++;
        }

        return validados;
    }
}
