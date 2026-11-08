package com.example.demoApiVersao23082026.service;

import com.example.demoApiVersao23082026.exception.BadRequestException;
import com.example.demoApiVersao23082026.exception.NotFoundException;
import com.example.demoApiVersao23082026.model.*;
import com.example.demoApiVersao23082026.repository.*;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class FuncionarioOperacaoService {

    private final UsuarioAutenticadoService autenticado;
    private final FuncionarioPermissaoService permissoes;
    private final UsuarioRepository usuarios;
    private final EventoRepository eventos;
    private final HistoricoAdministrativoRepository historico;

    public FuncionarioOperacaoService(
            UsuarioAutenticadoService autenticado,
            FuncionarioPermissaoService permissoes,
            UsuarioRepository usuarios,
            EventoRepository eventos,
            HistoricoAdministrativoRepository historico) {

        this.autenticado = autenticado;
        this.permissoes = permissoes;
        this.usuarios = usuarios;
        this.eventos = eventos;
        this.historico = historico;
    }

    private Usuario validarFuncionario(String permissao) {
        Usuario funcionario = autenticado.getUsuarioAutenticado();

        if (!Boolean.TRUE.equals(funcionario.getAtivo())
                || funcionario.getPerfil() == null
                || !"FUNCIONARIO".equals(
                funcionario.getPerfil().getNome())) {

            throw new org.springframework.security.access
                    .AccessDeniedException(
                    "Acesso exclusivo para funcionário ativo");
        }

        permissoes.exigirPermissao(permissao);
        return funcionario;
    }

    private void validarUsuarioComum(
            Usuario funcionario, Usuario alvo) {

        if (funcionario.getId().equals(alvo.getId())) {
            throw new BadRequestException(
                    "Você não pode alterar sua própria conta");
        }

        if (alvo.getPerfil() == null
                || "ADMIN".equalsIgnoreCase(
                alvo.getPerfil().getNome())
                || "FUNCIONARIO".equalsIgnoreCase(
                alvo.getPerfil().getNome())) {

            throw new BadRequestException(
                    "Funcionários não podem alterar contas administrativas");
        }
    }

    @Transactional
    public void bloquearUsuario(Integer id) {
        Usuario funcionario = validarFuncionario("bloquearUsuarios");

        Usuario alvo = usuarios.findById(id)
                .orElseThrow(() ->
                        new NotFoundException("Usuário não encontrado"));

        validarUsuarioComum(funcionario, alvo);

        alvo.setAtivo(false);
        usuarios.save(alvo);

        registrar(funcionario, "BLOQUEAR_USUARIO",
                "USUARIO", id,
                "Funcionário bloqueou o usuário de ID " + id);
    }

    @Transactional
    public void desbloquearUsuario(Integer id) {
        Usuario funcionario = validarFuncionario("bloquearUsuarios");

        Usuario alvo = usuarios.findById(id)
                .orElseThrow(() ->
                        new NotFoundException("Usuário não encontrado"));

        validarUsuarioComum(funcionario, alvo);

        alvo.setAtivo(true);
        usuarios.save(alvo);

        registrar(funcionario, "DESBLOQUEAR_USUARIO",
                "USUARIO", id,
                "Funcionário desbloqueou o usuário de ID " + id);
    }

    @Transactional
    public void removerEvento(Integer id, String motivo) {
        Usuario funcionario = validarFuncionario("moderarEventos");

        if (motivo == null || motivo.isBlank()) {
            throw new BadRequestException(
                    "Motivo da remoção é obrigatório");
        }

        Evento evento = eventos.findById(id)
                .orElseThrow(() ->
                        new NotFoundException("Evento não encontrado"));

        Usuario organizador = evento.getOrganizador();

        if (organizador == null || organizador.getPerfil() == null
                || "ADMIN".equalsIgnoreCase(
                organizador.getPerfil().getNome())
                || "FUNCIONARIO".equalsIgnoreCase(
                organizador.getPerfil().getNome())) {

            throw new BadRequestException(
                    "Não é permitido remover eventos de administradores ou funcionários");
        }

        if (evento.getStatus() == Evento.Status.REMOVIDO_ADMIN) {
            throw new BadRequestException(
                    "Este evento já foi removido");
        }

        evento.setStatus(Evento.Status.REMOVIDO_ADMIN);
        evento.setInscricoesAbertas(false);
        evento.setMotivoRemocao(motivo.trim());

        eventos.save(evento);

        registrar(funcionario, "REMOVER_EVENTO",
                "EVENTO", id, motivo.trim());
    }

    private void registrar(
            Usuario autor,
            String acao,
            String entidade,
            Integer entidadeId,
            String descricao) {

        HistoricoAdministrativo registro =
                new HistoricoAdministrativo();

        registro.setUsuario(autor);
        registro.setAcao(acao);
        registro.setEntidade(entidade);
        registro.setEntidadeId(entidadeId);
        registro.setDescricao(descricao);

        historico.save(registro);
    }
}
