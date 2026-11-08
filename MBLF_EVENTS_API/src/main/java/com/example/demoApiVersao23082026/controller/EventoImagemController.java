package com.example.demoApiVersao23082026.controller;

import com.example.demoApiVersao23082026.dto.EventoDTO;
import com.example.demoApiVersao23082026.service.EventoService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.FileSystemResource;
import org.springframework.core.io.Resource;
import org.springframework.http.*;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.server.ResponseStatusException;
import java.io.IOException;
import java.nio.file.*;
import java.util.UUID;

@RestController
@RequestMapping("/eventos")
public class EventoImagemController {
    private final EventoService service;
    private final Path pasta;

    public EventoImagemController(EventoService service,
            @Value("${mblf.upload-dir:uploads/eventos}") String uploadDir) {
        this.service = service;
        this.pasta = Paths.get(uploadDir).toAbsolutePath().normalize();
    }

    @PostMapping(value = "/{id}/imagem", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public EventoDTO enviar(@PathVariable Integer id, @RequestParam("arquivo") MultipartFile arquivo)
            throws IOException {
        // Confere autorização ANTES de salvar o arquivo no disco.
        service.validarPermissaoImagem(id);
        if (arquivo.isEmpty() || arquivo.getSize() > 5 * 1024 * 1024) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Imagem deve ter até 5 MB");
        }
        byte[] bytes = arquivo.getBytes();
        boolean jpeg = bytes.length > 3 && (bytes[0] & 255) == 255
                && (bytes[1] & 255) == 216 && (bytes[2] & 255) == 255;
        boolean png = bytes.length > 8 && (bytes[0] & 255) == 137
                && bytes[1] == 80 && bytes[2] == 78 && bytes[3] == 71;
        if (!jpeg && !png) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Use JPG ou PNG");
        }
        Files.createDirectories(pasta);
        String nome = UUID.randomUUID() + (png ? ".png" : ".jpg");
        Path destino = pasta.resolve(nome);
        Files.write(destino, bytes, StandardOpenOption.CREATE_NEW);
        try {
            return service.atualizarImagem(id, nome);
        } catch (RuntimeException ex) {
            Files.deleteIfExists(destino);
            throw ex;
        }
    }

    @GetMapping("/imagens/{nome}")
    public ResponseEntity<Resource> imagem(@PathVariable String nome) {
        if (!nome.matches("[a-f0-9\\-]{36}\\.(png|jpg)")) {
            return ResponseEntity.notFound().build();
        }
        Path arquivo = pasta.resolve(nome).normalize();
        if (!arquivo.startsWith(pasta) || !Files.isRegularFile(arquivo)) {
            return ResponseEntity.notFound().build();
        }
        MediaType tipo = nome.endsWith(".png") ? MediaType.IMAGE_PNG : MediaType.IMAGE_JPEG;
        return ResponseEntity.ok().contentType(tipo)
            .cacheControl(CacheControl.noCache()).body(new FileSystemResource(arquivo));
    }
}
