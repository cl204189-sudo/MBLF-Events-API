
package com.example.demoApiVersao23082026.service;

import com.example.demoApiVersao23082026.model.Usuario;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import javax.crypto.SecretKey;
import java.nio.charset.StandardCharsets;
import java.util.Date;

@Service
public class JwtService {

    @Value("${jwt.secret}")
    private String secret;

    @Value("${jwt.expiration}")
    private Long expiration;

    private SecretKey getKey() {
        return Keys.hmacShaKeyFor(
                secret.getBytes(StandardCharsets.UTF_8)
        );
    }

    public String gerarToken(Usuario usuario) {

        Date agora = new Date();

        Date expiracao = new Date(
                agora.getTime() + expiration
        );

        return Jwts.builder()
                // Agora o identificador principal é o ID
                .subject(usuario.getId().toString())

                .claim(
                        "usuarioId",
                        usuario.getId()
                )

                .claim(
                        "perfil",
                        usuario.getPerfil().getNome()
                )

                .issuedAt(agora)
                .expiration(expiracao)
                .signWith(getKey())
                .compact();
    }

    // NOVO: extrai o ID do subject
    public Integer extrairUsuarioId(String token) {
        return Integer.valueOf(
                extrairClaims(token).getSubject()
        );
    }

    public String extrairPerfil(String token) {
        return extrairClaims(token)
                .get("perfil", String.class);
    }

    // ALTERADO: valida pelo ID
    public boolean tokenValido(
            String token,
            Integer usuarioId) {

        Integer idToken = extrairUsuarioId(token);

        return idToken.equals(usuarioId)
                && !tokenExpirado(token);
    }

    private boolean tokenExpirado(String token) {
        return extrairClaims(token)
                .getExpiration()
                .before(new Date());
    }

    private Claims extrairClaims(String token) {
        return Jwts.parser()
                .verifyWith(getKey())
                .build()
                .parseSignedClaims(token)
                .getPayload();
    }
}
