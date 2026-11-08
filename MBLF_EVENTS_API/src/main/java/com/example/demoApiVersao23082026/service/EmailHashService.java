
package com.example.demoApiVersao23082026.service;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;
import java.nio.charset.StandardCharsets;
import java.util.Base64;
import java.util.HexFormat;
import java.util.Locale;

@Service
public class EmailHashService {

    private final SecretKeySpec chave;

    public EmailHashService(
            @Value("${app.crypto.hmac-key}") String chaveBase64) {

        byte[] bytes = Base64.getDecoder().decode(chaveBase64);

        if (bytes.length < 32) {
            throw new IllegalArgumentException(
                    "A chave HMAC deve possuir pelo menos 256 bits"
            );
        }

        this.chave = new SecretKeySpec(bytes, "HmacSHA256");
    }

    public String gerarHash(String email) {

        if (email == null || email.isBlank()) {
            throw new IllegalArgumentException(
                    "Email é obrigatório"
            );
        }

        try {
            String normalizado = email.trim()
                    .toLowerCase(Locale.ROOT);

            Mac mac = Mac.getInstance("HmacSHA256");
            mac.init(chave);

            byte[] resultado = mac.doFinal(
                    normalizado.getBytes(StandardCharsets.UTF_8)
            );

            return HexFormat.of().formatHex(resultado);

        } catch (Exception e) {
            throw new IllegalStateException(
                    "Erro ao gerar HMAC do email", e
            );
        }
    }
}
