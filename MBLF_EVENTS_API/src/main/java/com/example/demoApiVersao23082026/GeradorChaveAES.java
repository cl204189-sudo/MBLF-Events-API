package com.example.demoApiVersao23082026;
import javax.crypto.KeyGenerator;
import javax.crypto.SecretKey;
import java.util.Base64;

public class GeradorChaveAES {

    public static void main(String[] args) throws Exception {

        KeyGenerator gerador = KeyGenerator.getInstance("AES");
        gerador.init(256);

        SecretKey chave = gerador.generateKey();

        String chaveBase64 = Base64.getEncoder()
                .encodeToString(chave.getEncoded());

        System.out.println(chaveBase64);
    }
}
