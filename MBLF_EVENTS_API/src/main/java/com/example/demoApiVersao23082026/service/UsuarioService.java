package com.example.demoApiVersao23082026.service;

import com.example.demoApiVersao23082026.dto.UsuarioDTO;
import com.example.demoApiVersao23082026.dto.AlterarSenhaDTO;
import com.example.demoApiVersao23082026.exception.BadRequestException;
import com.example.demoApiVersao23082026.exception.NotFoundException;
import com.example.demoApiVersao23082026.model.Perfil;
import com.example.demoApiVersao23082026.model.Usuario;
import com.example.demoApiVersao23082026.repository.PerfilRepository;
import com.example.demoApiVersao23082026.repository.UsuarioRepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class UsuarioService {

    @Autowired
    private UsuarioRepository repository;

    @Autowired
    private PerfilRepository perfilRepository;

    @Autowired
    private UsuarioAutenticadoService usuarioAutenticadoService;

    @Autowired
    private BCryptPasswordEncoder passwordEncoder;

    @Autowired
    private CriptografiaService criptografiaService;

    @Autowired
    private EmailHashService emailHashService;

    @Autowired
    private UsuarioNomeService usuarioNomeService;

    @Transactional(readOnly = true)
    public List<UsuarioDTO> listarTodos() {
        return repository.findAll()
                .stream()
                .map(this::converterParaDTO)
                .toList();
    }

    @Transactional(readOnly = true)
    public UsuarioDTO buscar(Integer id) {
        Usuario usuario = repository.findById(id)
                .orElseThrow(() ->
                        new NotFoundException("Usuário não encontrado"));

        return converterParaDTO(usuario);
    }

    @Transactional
    public UsuarioDTO criar(Usuario usuario) {

        if (usuario == null) {
            throw new BadRequestException(
                    "Dados do usuário são obrigatórios"
            );
        }

        validarNome(usuario.getNome());
        validarEmail(usuario.getEmail());

        if (usuario.getSenha() == null ||
                usuario.getSenha().isBlank()) {
            throw new BadRequestException("Senha é obrigatória");
        }

        String nome = usuario.getNome().trim();
        String email = usuario.getEmail().trim();

        verificarEmailDisponivel(email, null);

        Perfil perfilUsuario = perfilRepository
                .findByNome("USUARIO")
                .orElseThrow(() ->
                        new NotFoundException(
                                "Perfil USUARIO não encontrado"));

        usuario.setPerfil(perfilUsuario);
        usuario.setAtivo(true);

        usuario.setSenha(
                passwordEncoder.encode(usuario.getSenha())
        );

        // Protege os dados antes de salvar.
        protegerNome(usuario, nome);
        protegerEmail(usuario, email);
        criptografarTelefone(usuario);

        Usuario salvo = salvarUsuario(usuario);

        return converterParaDTO(salvo);
    }

    @Transactional(readOnly = true)
    public UsuarioDTO buscarMeuPerfil() {

        Usuario usuario = usuarioAutenticadoService
                .getUsuarioAutenticado();

        return converterParaDTO(usuario);
    }

    @Transactional
    public void alterarMinhaSenha(AlterarSenhaDTO dto) {

        if (dto == null) {
            throw new BadRequestException(
                    "Dados da alteração de senha são obrigatórios"
            );
        }

        Usuario usuario = usuarioAutenticadoService
                .getUsuarioAutenticado();

        if (dto.getSenhaAtual() == null ||
                dto.getSenhaAtual().isBlank()) {
            throw new BadRequestException(
                    "Senha atual é obrigatória");
        }

        if (dto.getNovaSenha() == null ||
                dto.getNovaSenha().isBlank()) {
            throw new BadRequestException(
                    "Nova senha é obrigatória");
        }

        if (!passwordEncoder.matches(
                dto.getSenhaAtual(),
                usuario.getSenha())) {
            throw new BadRequestException(
                    "Senha atual incorreta");
        }

        if (dto.getNovaSenha().length() < 6) {
            throw new BadRequestException(
                    "A nova senha deve possuir pelo menos 6 caracteres");
        }

        usuario.setSenha(
                passwordEncoder.encode(dto.getNovaSenha())
        );

        repository.save(usuario);
    }

    @Transactional
    public UsuarioDTO atualizarMeuPerfil(Usuario usuario) {

        Usuario existente = usuarioAutenticadoService
                .getUsuarioAutenticado();

        return atualizarDados(existente, usuario);
    }

    @Transactional
    public UsuarioDTO atualizar(Integer id, Usuario usuario) {

        Usuario existente = repository.findById(id)
                .orElseThrow(() ->
                        new NotFoundException(
                                "Usuário não encontrado"));

        return atualizarDados(existente, usuario);
    }

    private UsuarioDTO atualizarDados(
            Usuario existente,
            Usuario dados) {

        if (dados == null) {
            throw new BadRequestException(
                    "Dados do usuário são obrigatórios"
            );
        }

        validarNome(dados.getNome());
        validarEmail(dados.getEmail());

        String nome = dados.getNome().trim();
        String email = dados.getEmail().trim();

        verificarEmailDisponivel(email, existente.getId());

        protegerNome(existente, nome);
        protegerEmail(existente, email);

        existente.setTelefone(dados.getTelefone());
        criptografarTelefone(existente);

        Usuario atualizado = salvarUsuario(existente);

        return converterParaDTO(atualizado);
    }

    @Transactional
    public void deletar(Integer id) {

        Usuario usuario = repository.findById(id)
                .orElseThrow(() ->
                        new NotFoundException(
                                "Usuário não encontrado"));

        repository.delete(usuario);
    }

    private void validarNome(String nome) {
        if (nome == null || nome.isBlank()) {
            throw new BadRequestException("Nome é obrigatório");
        }
    }

    private void validarEmail(String email) {
        if (email == null || email.isBlank()) {
            throw new BadRequestException("Email é obrigatório");
        }
    }

    private void verificarEmailDisponivel(
            String email,
            Integer idAtual) {

        String hash = emailHashService.gerarHash(email);

        boolean duplicadoHash = repository
                .findByEmailHash(hash)
                .filter(u -> !u.getId().equals(idAtual))
                .isPresent();

        boolean duplicadoAntigo = repository
                .findByEmail(email)
                .filter(u -> !u.getId().equals(idAtual))
                .isPresent();

        if (duplicadoHash || duplicadoAntigo) {
            throw new BadRequestException(
                    "Já existe um usuário com esse email");
        }
    }

    // CRIPTOGRAFIA DOS NOMES

    private void protegerNome(
            Usuario usuario,
            String nome) {

        if (Boolean.TRUE.equals(
                usuario.getNomeCriptografado())) {

            String nomeAtual = usuarioNomeService.obterNome(usuario);

            if (nome.equals(nomeAtual)) {
                return;
            }
        }

        usuario.setNome(
                criptografiaService.criptografar(nome)
        );

        usuario.setNomeCriptografado(true);
    }

    // CRIPTOGRAFIA DOS EMAILS

    private void protegerEmail(
            Usuario usuario,
            String email) {

        String hash = emailHashService.gerarHash(email);

        if (Boolean.TRUE.equals(
                usuario.getEmailCriptografado())
                && hash.equals(usuario.getEmailHash())) {
            return;
        }

        usuario.setEmail(
                criptografiaService.criptografar(email)
        );

        usuario.setEmailHash(hash);
        usuario.setEmailCriptografado(true);
    }

    private String obterEmail(Usuario usuario) {

        if (usuario.getEmail() == null) {
            return null;
        }

        if (Boolean.TRUE.equals(
                usuario.getEmailCriptografado())) {

            return criptografiaService.descriptografar(
                    usuario.getEmail()
            );
        }

        return usuario.getEmail();
    }

    // CRIPTOGRAFIA DOS TELEFONES

    private void criptografarTelefone(Usuario usuario) {

        if (usuario.getTelefone() == null) {
            usuario.setTelefoneCriptografado(false);
            return;
        }

        usuario.setTelefone(
                criptografiaService.criptografar(
                        usuario.getTelefone()
                )
        );

        usuario.setTelefoneCriptografado(true);
    }

    private String obterTelefone(Usuario usuario) {

        if (usuario.getTelefone() == null) {
            return null;
        }

        if (Boolean.TRUE.equals(
                usuario.getTelefoneCriptografado())) {

            return criptografiaService.descriptografar(
                    usuario.getTelefone()
            );
        }

        return usuario.getTelefone();
    }

    private Usuario salvarUsuario(Usuario usuario) {
        try {
            return repository.saveAndFlush(usuario);
        } catch (DataIntegrityViolationException e) {
            throw new BadRequestException(
                    "Não foi possível salvar: verifique se o email já existe"
            );
        }
    }

    private UsuarioDTO converterParaDTO(Usuario usuario) {

        return new UsuarioDTO(
                usuario.getId(),
                usuarioNomeService.obterNome(usuario),
                obterEmail(usuario),
                obterTelefone(usuario),
                usuario.getPerfil().getNome(),
                usuario.getAtivo(),
                usuario.getDataCadastro()
        );
    }
}
