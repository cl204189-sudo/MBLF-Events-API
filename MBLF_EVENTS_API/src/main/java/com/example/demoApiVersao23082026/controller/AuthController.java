package com.example.demoApiVersao23082026.controller;

import com.example.demoApiVersao23082026.dto.LoginDTO;
import com.example.demoApiVersao23082026.dto.LoginResponseDTO;
import com.example.demoApiVersao23082026.service.AuthService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/auth")
@CrossOrigin(origins = "*")
public class AuthController {

    @Autowired
    private AuthService service;

    @PostMapping("/login")
    public LoginResponseDTO login(
            @RequestBody LoginDTO dto) {

        return service.login(dto);
    }
}