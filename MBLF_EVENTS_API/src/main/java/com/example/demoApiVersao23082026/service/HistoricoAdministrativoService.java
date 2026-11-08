package com.example.demoApiVersao23082026.service;

import com.example.demoApiVersao23082026.dto.HistoricoAdministrativoDTO;
import com.example.demoApiVersao23082026.exception.BadRequestException;
import com.example.demoApiVersao23082026.model.HistoricoAdministrativo;
import com.example.demoApiVersao23082026.model.Usuario;
import com.example.demoApiVersao23082026.repository.HistoricoAdministrativoRepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class HistoricoAdministrativoService {

    @Autowired
    private HistoricoAdministrativoRepository repository;

    @Autowired
    private UsuarioAutenticadoService usuarioAutenticadoService;

    @Autowired
    private UsuarioNomeService usuarioNomeService;

    @Transactional(readOnly = true)
    public List<HistoricoAdministrativoDTO> listar() {

        Usuario usuario = usuarioAutenticadoService
                .getUsuarioAutenticado();

        validarAdmin(usuario);

        return repository
                .findAllByOrderByDataAcaoDesc()
                .stream()
                .map(this::converterParaDTO)
                .toList();
    }

    private void validarAdmin(Usuario usuario) {

        if (usuario.getPerfil() == null ||
                !"ADMIN".equals(usuario.getPerfil().getNome())) {

            throw new BadRequestException(
                    "Acesso permitido somente para administradores"
            );
        }
    }

    private HistoricoAdministrativoDTO converterParaDTO(
            HistoricoAdministrativo historico) {

        return new HistoricoAdministrativoDTO(
                historico.getId(),

                historico.getUsuario().getId(),

                usuarioNomeService.obterNome(
                        historico.getUsuario()
                ),

                historico.getAcao(),
                historico.getEntidade(),
                historico.getEntidadeId(),

                historico.getDescricao(),

                historico.getDataAcao()
        );
    }
}
