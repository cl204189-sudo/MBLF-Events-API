package com.example.demoApiVersao23082026.service;

import com.example.demoApiVersao23082026.exception.BadRequestException;
import com.example.demoApiVersao23082026.exception.NotFoundException;
import com.example.demoApiVersao23082026.model.Plano;
import com.example.demoApiVersao23082026.repository.PlanoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class PlanoService {

    @Autowired
    private PlanoRepository repository;

    public List<Plano> listarTodos() {
        return repository.findAll();
    }

    public List<Plano> listarAtivos() {
        return repository.findByAtivoTrue();
    }

    public Plano buscar(Integer id) {
        return repository.findById(id)
                .orElseThrow(() ->
                        new NotFoundException("Plano não encontrado"));
    }

    public Plano criar(Plano plano) {

        if (plano.getNome() == null ||
                plano.getNome().isBlank()) {

            throw new BadRequestException(
                    "Nome do plano é obrigatório"
            );
        }

        if (plano.getTipo() == null) {
            throw new BadRequestException(
                    "Tipo do plano é obrigatório"
            );
        }

        if (plano.getDuracaoDias() == null ||
                plano.getDuracaoDias() <= 0) {

            throw new BadRequestException(
                    "Duração do plano deve ser maior que zero"
            );
        }

        if (plano.getValor() == null ||
                plano.getValor().signum() < 0) {

            throw new BadRequestException(
                    "Valor do plano inválido"
            );
        }

        if (plano.getAtivo() == null) {
            plano.setAtivo(true);
        }

        return repository.save(plano);
    }

    public Plano atualizar(
            Integer id,
            Plano novo) {

        Plano existente = buscar(id);

        existente.setNome(novo.getNome());
        existente.setTipo(novo.getTipo());
        existente.setDuracaoDias(
                novo.getDuracaoDias()
        );
        existente.setValor(novo.getValor());
        existente.setAtivo(novo.getAtivo());

        return repository.save(existente);
    }

    public void desativar(Integer id) {

        Plano plano = buscar(id);

        plano.setAtivo(false);

        repository.save(plano);
    }
}