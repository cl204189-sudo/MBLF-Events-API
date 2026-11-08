package com.example.demoApiVersao23082026.service;

import com.example.demoApiVersao23082026.exception.BadRequestException;
import com.example.demoApiVersao23082026.exception.NotFoundException;
import com.example.demoApiVersao23082026.model.Perfil;
import com.example.demoApiVersao23082026.repository.PerfilRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class PerfilService {

    @Autowired
    private PerfilRepository repository;

    public List<Perfil> listarTodos() {
        return repository.findAll();
    }

    public Perfil buscar(Integer id) {
        return repository.findById(id)
                .orElseThrow(() ->
                        new NotFoundException("Perfil não encontrado"));
    }

    public Perfil criar(Perfil perfil) {

        if (repository.existsByNome(perfil.getNome())) {
            throw new BadRequestException(
                    "Já existe um perfil com esse nome"
            );
        }

        return repository.save(perfil);
    }

    public Perfil atualizar(Integer id, Perfil perfil) {

        Perfil existente = repository.findById(id)
                .orElseThrow(() ->
                        new NotFoundException("Perfil não encontrado"));

        if (!existente.getNome().equals(perfil.getNome())
                && repository.existsByNome(perfil.getNome())) {

            throw new BadRequestException(
                    "Já existe um perfil com esse nome"
            );
        }

        existente.setNome(perfil.getNome());

        return repository.save(existente);
    }

    public void deletar(Integer id) {

        Perfil perfil = repository.findById(id)
                .orElseThrow(() ->
                        new NotFoundException("Perfil não encontrado"));

        repository.delete(perfil);
    }
}