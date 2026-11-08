
package com.example.demoApiVersao23082026.service;

import com.example.demoApiVersao23082026.dto.LoginDTO;
import com.example.demoApiVersao23082026.dto.LoginResponseDTO;
import com.example.demoApiVersao23082026.exception.BadRequestException;
import com.example.demoApiVersao23082026.exception.NotFoundException;
import com.example.demoApiVersao23082026.model.Usuario;
import com.example.demoApiVersao23082026.repository.UsuarioRepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class AuthService {

    @Autowired
    private UsuarioRepository usuarioRepository;

    @Autowired
    private BCryptPasswordEncoder passwordEncoder;

    @Autowired
    private JwtService jwtService;

    @Autowired
    private EmailHashService emailHashService;

    @Autowired
    private CriptografiaService criptografiaService;

    @Autowired
    private UsuarioNomeService usuarioNomeService;

    public LoginResponseDTO login(LoginDTO dto) {

        if (dto.getEmail() == null ||
                dto.getEmail().isBlank()) {
            throw new BadRequestException("Email é obrigatório");
        }

        if (dto.getSenha() == null ||
                dto.getSenha().isBlank()) {
            throw new BadRequestException("Senha é obrigatória");
        }

        String email = dto.getEmail().trim();
        String hash = emailHashService.gerarHash(email);

        Usuario usuario = usuarioRepository.findByEmailHash(hash)
                .or(() -> usuarioRepository.findByEmail(email))
                .orElseThrow(() ->
                        new NotFoundException("Usuário não encontrado")
                );

        if (!Boolean.TRUE.equals(usuario.getAtivo())) {
            throw new BadRequestException("Usuário está desativado");
        }

        if (!passwordEncoder.matches(
                dto.getSenha(),
                usuario.getSenha())) {
            throw new BadRequestException("Senha inválida");
        }

        String token = jwtService.gerarToken(usuario);

        String emailOriginal = usuario.getEmail();

        if (Boolean.TRUE.equals(usuario.getEmailCriptografado())) {
            emailOriginal = criptografiaService.descriptografar(
                    usuario.getEmail()
            );
        }

        String nomeOriginal = usuarioNomeService.obterNome(usuario);

        return new LoginResponseDTO(
                usuario.getId(),
                nomeOriginal,
                emailOriginal,
                usuario.getPerfil().getNome(),
                token
        );
    }
}
