package com.example.demoApiVersao23082026.migration;

import com.example.demoApiVersao23082026.model.Usuario;
import com.example.demoApiVersao23082026.repository.UsuarioRepository;
import com.example.demoApiVersao23082026.service.CriptografiaService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.CommandLineRunner;
import org.springframework.jdbc.datasource.DataSourceUtils;
import org.springframework.stereotype.Component;
import org.springframework.transaction.support.TransactionTemplate;

import javax.sql.DataSource;
import java.sql.Connection;
import java.util.ArrayList;
import java.util.List;

@Component
public class MigracaoNomesUsuarios implements CommandLineRunner {

    @Autowired
    private UsuarioRepository usuarioRepository;

    @Autowired
    private CriptografiaService criptografiaService;

    @Autowired
    private DataSource dataSource;

    @Autowired
    private TransactionTemplate transactionTemplate;

    @Value("${app.migracao.nomes.executar:false}")
    private boolean executarMigracao;

    @Override
    public void run(String... args) {

        // Por padrão, a migração fica desativada.
        if (!executarMigracao) {
            return;
        }

        verificarBancoDeTestes();

        Integer total = transactionTemplate.execute(status -> {

            verificarBancoDeTestes();

            List<Usuario> usuarios = usuarioRepository.findAll();
            List<Usuario> alterados = new ArrayList<>();

            for (Usuario usuario : usuarios) {

                // Não criptografa novamente nomes já protegidos.
                if (Boolean.TRUE.equals(
                        usuario.getNomeCriptografado())) {
                    continue;
                }

                String nomeOriginal = usuario.getNome();

                if (nomeOriginal == null ||
                        nomeOriginal.isBlank()) {
                    throw new IllegalStateException(
                            "Nome inválido no usuário ID "
                                    + usuario.getId()
                    );
                }

                String nomeCriptografado =
                        criptografiaService.criptografar(nomeOriginal);

                // Verifica se é possível recuperar o nome.
                String nomeRecuperado =
                        criptografiaService.descriptografar(
                                nomeCriptografado
                        );

                if (!nomeOriginal.equals(nomeRecuperado)) {
                    throw new IllegalStateException(
                            "Falha na verificação do usuário ID "
                                    + usuario.getId()
                    );
                }

                usuario.setNome(nomeCriptografado);
                usuario.setNomeCriptografado(true);

                alterados.add(usuario);
            }

            usuarioRepository.saveAllAndFlush(alterados);

            return alterados.size();
        });

        System.out.println(
                "Migração dos nomes concluída. Usuários atualizados: "
                        + total
        );
    }

    private void verificarBancoDeTestes() {

        Connection conexao =
                DataSourceUtils.getConnection(dataSource);

        try {

            String banco = conexao.getCatalog();

            if (!"cl204193".equals(banco)) {
                throw new IllegalStateException(
                        "Migração bloqueada! Banco atual: "
                                + banco
                );
            }

        } catch (Exception e) {
            throw new IllegalStateException(
                    "Não foi possível confirmar o banco de testes", e
            );

        } finally {
            DataSourceUtils.releaseConnection(
                    conexao, dataSource
            );
        }
    }
}
