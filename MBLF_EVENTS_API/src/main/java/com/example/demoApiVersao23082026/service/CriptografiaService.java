package com.example.demoApiVersao23082026.service;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import javax.crypto.Cipher;
import javax.crypto.SecretKey;
import javax.crypto.spec.GCMParameterSpec;
import javax.crypto.spec.SecretKeySpec;
import java.nio.charset.StandardCharsets;
import java.security.SecureRandom;
import java.util.Base64;

@Service
public class CriptografiaService {

    private static final String ALGORITMO = "AES/GCM/NoPadding";
    private static final int TAMANHO_IV = 12;
    private static final int TAMANHO_TAG = 128;

    private final SecretKey chave;
    private final SecureRandom random = new SecureRandom();

    public CriptografiaService(
            @Value("${app.crypto.key}") String chaveBase64) {

        byte[] bytes = Base64.getDecoder().decode(chaveBase64);

        if (bytes.length != 32) {
            throw new IllegalArgumentException(
                    "A chave AES deve possuir 256 bits"
            );
        }

        this.chave = new SecretKeySpec(bytes, "AES");
    }

    public String criptografar(String texto) {

        if (texto == null) {
            return null;
        }

        try {
            byte[] iv = new byte[TAMANHO_IV];
            random.nextBytes(iv);

            Cipher cipher = Cipher.getInstance(ALGORITMO);

            cipher.init(
                    Cipher.ENCRYPT_MODE,
                    chave,
                    new GCMParameterSpec(TAMANHO_TAG, iv)
            );

            byte[] criptografado = cipher.doFinal(
                    texto.getBytes(StandardCharsets.UTF_8)
            );

            byte[] resultado =
                    new byte[iv.length + criptografado.length];

            System.arraycopy(
                    iv, 0, resultado, 0, iv.length
            );

            System.arraycopy(
                    criptografado, 0,
                    resultado, iv.length,
                    criptografado.length
            );

            return Base64.getEncoder()
                    .encodeToString(resultado);

        } catch (Exception e) {
            throw new IllegalStateException(
                    "Erro ao criptografar dados", e
            );
        }
    }

    public String descriptografar(String texto) {

        if (texto == null) {
            return null;
        }

        try {
            byte[] dados = Base64.getDecoder()
                    .decode(texto);

            if (dados.length < TAMANHO_IV + 16) {
                throw new IllegalArgumentException(
                        "Dados criptografados inválidos"
                );
            }

            byte[] iv = new byte[TAMANHO_IV];

            byte[] criptografado =
                    new byte[dados.length - TAMANHO_IV];

            System.arraycopy(
                    dados, 0, iv, 0, TAMANHO_IV
            );

            System.arraycopy(
                    dados, TAMANHO_IV,
                    criptografado, 0,
                    criptografado.length
            );

            Cipher cipher = Cipher.getInstance(ALGORITMO);

            cipher.init(
                    Cipher.DECRYPT_MODE,
                    chave,
                    new GCMParameterSpec(TAMANHO_TAG, iv)
            );

            byte[] original =
                    cipher.doFinal(criptografado);

            return new String(
                    original, StandardCharsets.UTF_8
            );

        } catch (Exception e) {
            throw new IllegalStateException(
                    "Erro ao descriptografar dados", e
            );
        }
    }
}

