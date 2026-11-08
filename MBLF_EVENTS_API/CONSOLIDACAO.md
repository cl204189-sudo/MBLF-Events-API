# MBLF Events — API consolidada

## Origem
Base: `demoApiVersaoMBLFevents07112026.zip`. Recursos adaptados das outras três APIs.

## Alterações e finalidade

1. `controller/AgendaEventoController.java`: adicionada a rota GET `/agenda/evento/{eventoId}/programacao` para exibir a programação aos inscritos.
2. `service/AgendaEventoService.java`: adicionada `listarParaInscrito`, que verifica inscrição ativa (`INSCRITO` ou `PRESENTE`) antes de consultar a agenda. Mantém a edição restrita aos responsáveis.
3. `controller/RelatorioEventoController.java`: adicionada GET `/relatorios/eventos`, que reutiliza `EventoService.listarTodos()` para listar eventos no visualizador de relatórios.
4. `config/SecurityConfig.java`: restringe GET `/relatorios/eventos` ao papel `ADMIN`.
5. `controller/MensagemController.java`: adicionadas GET/POST `/mensagens/evento/{eventoId}/global`, como aliases para o canal `GERAL` existente. Não altera o modelo do chat nem o canal `ORGANIZACAO`.
6. `src/main/resources/application.properties.example`: modelo de configuração sem credenciais. Configure um `application.properties` local antes de executar.

## Escolhas de integração
- Não foram substituídos serviços e entidades recentes por arquivos antigos: isso poderia remover funcionalidades e quebrar compatibilidade.
- A implementação antiga de funcionários não foi copiada porque a base já contém essas rotas e delega a lógica a `FuncionarioOperacaoService`.
- A API não foi conectada a um MySQL nem submetida a testes de integração; valide autenticação, agenda, relatórios, chat, permissões e Flutter no seu ambiente.
- Nenhum projeto Flutter foi modificado.

## Executar
Use Java 21 e Maven. Configure banco de dados e demais propriedades no arquivo `src/main/resources/application.properties`, depois execute `mvn spring-boot:run`.
