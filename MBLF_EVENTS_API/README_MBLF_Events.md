# MBLF Events — Configuração das variáveis

Antes de executar a API, configure os dados do banco e as chaves de segurança.

## 1. Banco de dados

Abra `src/main/resources/application.properties` e substitua os valores de exemplo pelos dados do seu MySQL:

```properties
spring.datasource.url=jdbc:mysql://localhost:3306/NOME_DO_BANCO
spring.datasource.username=USUARIO_MYSQL
spring.datasource.password=SENHA_MYSQL
```

O banco pode ter qualquer nome, desde que exista e tenha as tabelas necessárias.

## 2. Variáveis de ambiente

O `application.properties` deve conter:

```properties
jwt.secret=${JWT_SECRET}
app.crypto.key=${APP_CRYPTO_KEY}
app.crypto.hmac-key=${APP_CRYPTO_HMAC_KEY}
```

No **Windows**:

1. Pressione `Win + R`, digite `sysdm.cpl` e pressione Enter.
2. Acesse **Avançado > Variáveis de Ambiente**.
3. Em **Variáveis de usuário**, clique em **Novo** e cadastre cada uma:

| Nome | Valor |
| --- | --- |
| `JWT_SECRET` | Sua chave secreta JWT |
| `APP_CRYPTO_KEY` | Sua chave de criptografia AES |
| `APP_CRYPTO_HMAC_KEY` | Sua chave HMAC |

4. Feche e abra novamente a IDE antes de iniciar a API.

**Importante:** as chaves devem seguir o formato e o tamanho exigidos pelo código. Para ler dados já criptografados de outro banco, use as chaves originais, compartilhadas de forma privada.

Depois de configurar, execute o projeto normalmente.
