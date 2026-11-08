package com.example.demoApiVersao23082026.service;

import com.example.demoApiVersao23082026.model.Usuario;
import com.example.demoApiVersao23082026.repository.UsuarioRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Service
public class MigracaoEmailService {

    private final UsuarioRepository usuarioRepository;
    private final CriptografiaService criptografiaService;
    private final EmailHashService emailHashService;

    public MigracaoEmailService(
            UsuarioRepository usuarioRepository,
            CriptografiaService criptografiaService,
            EmailHashService emailHashService) {

        this.usuarioRepository = usuarioRepository;
        this.criptografiaService = criptografiaService;
        this.emailHashService = emailHashService;
    }

    @Transactional
    public int migrarEmailsAntigos() {

        List<Usuario> usuarios = usuarioRepository.findAll();

        Set<String> hashes = new HashSet<>();

        // Primeiro valida todos os usuários.
        for (Usuario usuario : usuarios) {

            if (Boolean.TRUE.equals(
                    usuario.getEmailCriptografado())) {

                if (usuario.getEmailHash() == null) {
                    throw new IllegalStateException(
                            "Usuário criptografado sem hash: "
                                    + usuario.getId());
                }

                String emailOriginal =
                        criptografiaService.descriptografar(
                                usuario.getEmail());

                String hashEsperado =
                        emailHashService.gerarHash(emailOriginal);

                if (!hashEsperado.equals(usuario.getEmailHash())) {
                    throw new IllegalStateException(
                            "Hash inválido no usuário: "
                                    + usuario.getId());
                }

                if (!hashes.add(hashEsperado)) {
                    throw new IllegalStateException(
                            "Email duplicado no usuário: "
                                    + usuario.getId());
                }

                continue;
            }

            if (usuario.getEmail() == null ||
                    usuario.getEmail().isBlank()) {
                throw new IllegalStateException(
                        "Email inválido no usuário: "
                                + usuario.getId());
            }

            if (usuario.getEmailHash() != null) {
                throw new IllegalStateException(
                        "Usuário antigo já possui hash: "
                                + usuario.getId());
            }

            String hash =
                    emailHashService.gerarHash(usuario.getEmail());

            if (!hashes.add(hash)) {
                throw new IllegalStateException(
                        "Email duplicado no usuário: "
                                + usuario.getId());
            }
        }

        int migrados = 0;

        // Somente após validar, criptografa os antigos.
        for (Usuario usuario : usuarios) {

            if (Boolean.TRUE.equals(
                    usuario.getEmailCriptografado())) {
                continue;
            }

            String emailOriginal = usuario.getEmail();

            String emailCriptografado =
                    criptografiaService.criptografar(emailOriginal);

            // Confirma que a descriptografia funciona.
            String emailRecuperado =
                    criptografiaService.descriptografar(
                            emailCriptografado);

            if (!emailOriginal.equals(emailRecuperado)) {
                throw new IllegalStateException(
                        "Falha na verificação do usuário: "
                                + usuario.getId());
            }

            usuario.setEmailHash(
                    emailHashService.gerarHash(emailOriginal));

            usuario.setEmail(emailCriptografado);
            usuario.setEmailCriptografado(true);

            migrados++;
        }

        usuarioRepository.saveAllAndFlush(usuarios);

        return migrados;
    }
}
