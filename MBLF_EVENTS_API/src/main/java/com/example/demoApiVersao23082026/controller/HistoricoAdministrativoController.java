package com.example.demoApiVersao23082026.controller;

import com.example.demoApiVersao23082026.dto.HistoricoAdministrativoDTO;
import com.example.demoApiVersao23082026.service.HistoricoAdministrativoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/historico-admin")
public class HistoricoAdministrativoController {

    @Autowired
    private HistoricoAdministrativoService service;

    @GetMapping
    public List<HistoricoAdministrativoDTO> listar() {

        return service.listar();
    }
}