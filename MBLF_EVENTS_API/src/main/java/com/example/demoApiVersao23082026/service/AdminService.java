package com.example.demoApiVersao23082026.service;

import com.example.demoApiVersao23082026.dto.FuncionarioPermissaoDTO;
import com.example.demoApiVersao23082026.exception.BadRequestException;
import com.example.demoApiVersao23082026.exception.NotFoundException;

import com.example.demoApiVersao23082026.model.*;
import com.example.demoApiVersao23082026.repository.*;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class AdminService {

    @Autowired
    private UsuarioRepository usuarioRepository;

    @Autowired
    private PerfilRepository perfilRepository;

    @Autowired
    private EventoRepository eventoRepository;

    @Autowired
    private UsuarioAutenticadoService usuarioAutenticadoService;

    @Autowired
    private FuncionarioPermissaoRepository funcionarioRepository;

    @Autowired
    private HistoricoAdministrativoRepository historicoRepository;

    @Transactional
    public void bloquearUsuario(Integer usuarioId) {

        Usuario admin = usuarioAutenticadoService
                .getUsuarioAutenticado();

        validarAdmin(admin);

        if (admin.getId().equals(usuarioId)) {
            throw new BadRequestException(
                    "O administrador não pode bloquear a si mesmo"
            );
        }

        Usuario usuario = buscarUsuario(usuarioId);

        usuario.setAtivo(false);

        usuarioRepository.save(usuario);

        registrarHistorico(
                admin,
                "BLOQUEAR_USUARIO",
                "USUARIO",
                usuarioId,
                "Usuário de ID " + usuarioId + " foi bloqueado"
        );
    }

    @Transactional
    public void desbloquearUsuario(Integer usuarioId) {

        Usuario admin = usuarioAutenticadoService
                .getUsuarioAutenticado();

        validarAdmin(admin);

        Usuario usuario = buscarUsuario(usuarioId);

        usuario.setAtivo(true);

        usuarioRepository.save(usuario);

        registrarHistorico(
                admin,
                "DESBLOQUEAR_USUARIO",
                "USUARIO",
                usuarioId,
                "Usuário de ID " + usuarioId + " foi desbloqueado"
        );
    }

    @Transactional
    public void tornarFuncionario(
            Integer usuarioId,
            FuncionarioPermissaoDTO dto) {

        Usuario admin = usuarioAutenticadoService
                .getUsuarioAutenticado();

        validarAdmin(admin);

        if (dto == null) {
            throw new BadRequestException(
                    "As permissões do funcionário são obrigatórias"
            );
        }

        Usuario usuario = buscarUsuario(usuarioId);

        if (usuario.getPerfil() != null &&
                "ADMIN".equals(usuario.getPerfil().getNome())) {

            throw new BadRequestException(
                    "Não é permitido transformar um administrador em funcionário"
            );
        }

        Perfil perfilFuncionario = perfilRepository
                .findByNome("FUNCIONARIO")
                .orElseThrow(() ->
                        new NotFoundException(
                                "Perfil FUNCIONARIO não encontrado"
                        ));

        usuario.setPerfil(perfilFuncionario);

        usuarioRepository.save(usuario);

        FuncionarioPermissao permissao = funcionarioRepository
                .findById(usuarioId)
                .orElse(new FuncionarioPermissao());

        permissao.setUsuario(usuario);

        permissao.setVisualizarUsuarios(
                Boolean.TRUE.equals(
                        dto.getVisualizarUsuarios()
                )
        );

        permissao.setBloquearUsuarios(
                Boolean.TRUE.equals(
                        dto.getBloquearUsuarios()
                )
        );

        permissao.setVisualizarEventos(
                Boolean.TRUE.equals(
                        dto.getVisualizarEventos()
                )
        );

        permissao.setModerarEventos(
                Boolean.TRUE.equals(
                        dto.getModerarEventos()
                )
        );

        permissao.setVisualizarPagamentos(
                Boolean.TRUE.equals(
                        dto.getVisualizarPagamentos()
                )
        );

        permissao.setGerenciarPlanos(
                Boolean.TRUE.equals(
                        dto.getGerenciarPlanos()
                )
        );

        funcionarioRepository.save(permissao);

        registrarHistorico(
                admin,
                "TORNAR_FUNCIONARIO",
                "USUARIO",
                usuarioId,
                "Usuário de ID " + usuarioId
                        + " tornou-se funcionário"
        );
    }

    @Transactional
    public void removerEvento(
            Integer eventoId,
            String motivo) {

        Usuario admin = usuarioAutenticadoService
                .getUsuarioAutenticado();

        validarAdmin(admin);

        Evento evento = eventoRepository
                .findById(eventoId)
                .orElseThrow(() ->
                        new NotFoundException(
                                "Evento não encontrado"
                        ));

        if (motivo == null || motivo.isBlank()) {
            throw new BadRequestException(
                    "Motivo da remoção é obrigatório"
            );
        }

        evento.setStatus(
                Evento.Status.REMOVIDO_ADMIN
        );

        evento.setInscricoesAbertas(false);

        evento.setMotivoRemocao(motivo);

        eventoRepository.save(evento);

        registrarHistorico(
                admin,
                "REMOVER_EVENTO",
                "EVENTO",
                eventoId,
                motivo
        );
    }

    private void validarAdmin(Usuario usuario) {

        if (usuario.getPerfil() == null ||
                !"ADMIN".equals(
                        usuario.getPerfil().getNome()
                )) {

            throw new BadRequestException(
                    "Acesso permitido somente para administradores"
            );
        }
    }

    private Usuario buscarUsuario(Integer id) {

        return usuarioRepository
                .findById(id)
                .orElseThrow(() ->
                        new NotFoundException(
                                "Usuário não encontrado"
                        ));
    }

    private void registrarHistorico(
            Usuario usuario,
            String acao,
            String entidade,
            Integer entidadeId,
            String descricao) {

        HistoricoAdministrativo historico =
                new HistoricoAdministrativo();

        historico.setUsuario(usuario);

        historico.setAcao(acao);

        historico.setEntidade(entidade);

        historico.setEntidadeId(entidadeId);

        historico.setDescricao(descricao);

        historicoRepository.save(historico);
    }
}
