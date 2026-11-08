-- MBLF Events
-- ------------------------------------------------------

/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */;
/*!50503 SET NAMES utf8mb4 */;
/*!40103 SET @OLD_TIME_ZONE=@@TIME_ZONE */;
/*!40103 SET TIME_ZONE='+00:00' */;
/*!40014 SET @OLD_UNIQUE_CHECKS=@@UNIQUE_CHECKS, UNIQUE_CHECKS=0 */;
/*!40014 SET @OLD_FOREIGN_KEY_CHECKS=@@FOREIGN_KEY_CHECKS, FOREIGN_KEY_CHECKS=0 */;
/*!40101 SET @OLD_SQL_MODE=@@SQL_MODE, SQL_MODE='NO_AUTO_VALUE_ON_ZERO' */;
/*!40111 SET @OLD_SQL_NOTES=@@SQL_NOTES, SQL_NOTES=0 */;

--
-- Table structure for table `agenda_evento`
--

/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `agenda_evento` (
  `id` int NOT NULL AUTO_INCREMENT,
  `evento_id` int NOT NULL,
  `criado_por` int NOT NULL,
  `titulo` varchar(200) NOT NULL,
  `descricao` text,
  `data_inicio` datetime NOT NULL,
  `data_fim` datetime DEFAULT NULL,
  `tipo` enum('REUNIAO','ATIVIDADE','LEMBRETE','PRAZO','OUTRO') DEFAULT 'OUTRO',
  `data_criacao` datetime DEFAULT CURRENT_TIMESTAMP,
  PRIMARY KEY (`id`),
  KEY `evento_id` (`evento_id`),
  KEY `criado_por` (`criado_por`),
  CONSTRAINT `agenda_evento_ibfk_1` FOREIGN KEY (`evento_id`) REFERENCES `eventos` (`id`) ON DELETE CASCADE,
  CONSTRAINT `agenda_evento_ibfk_2` FOREIGN KEY (`criado_por`) REFERENCES `usuarios` (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=6 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `auditoria_presencas`
--

/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `auditoria_presencas` (
  `id` int NOT NULL AUTO_INCREMENT,
  `inscricao_id` int NOT NULL,
  `operador_id` int NOT NULL,
  `acao` varchar(30) NOT NULL,
  `motivo` varchar(500) NOT NULL,
  `data_acao` datetime(6) NOT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `certificados`
--

/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `certificados` (
  `id` int NOT NULL AUTO_INCREMENT,
  `inscricao_id` int NOT NULL,
  `codigo` varchar(100) NOT NULL,
  `titulo_evento` varchar(200) NOT NULL,
  `nome_participante` varchar(150) NOT NULL,
  `carga_horaria` int DEFAULT NULL,
  `status` enum('PENDENTE','LIBERADO') DEFAULT 'PENDENTE',
  `data_geracao` datetime DEFAULT CURRENT_TIMESTAMP,
  `data_liberacao` datetime DEFAULT NULL,
  `liberado_por` int DEFAULT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `inscricao_id` (`inscricao_id`),
  UNIQUE KEY `codigo` (`codigo`),
  KEY `liberado_por` (`liberado_por`),
  CONSTRAINT `certificados_ibfk_1` FOREIGN KEY (`inscricao_id`) REFERENCES `inscricoes` (`id`) ON DELETE CASCADE,
  CONSTRAINT `certificados_ibfk_2` FOREIGN KEY (`liberado_por`) REFERENCES `usuarios` (`id`) ON DELETE SET NULL
) ENGINE=InnoDB AUTO_INCREMENT=3 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `evento_colaboradores`
--

/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `evento_colaboradores` (
  `id` int NOT NULL AUTO_INCREMENT,
  `evento_id` int NOT NULL,
  `usuario_id` int NOT NULL,
  `funcao` varchar(100) DEFAULT NULL,
  `status` enum('PENDENTE','ACEITO','RECUSADO','EXPULSO') NOT NULL DEFAULT 'PENDENTE',
  `pode_chat` tinyint(1) DEFAULT '1',
  `pode_calendario` tinyint(1) DEFAULT '1',
  `pode_tarefas` tinyint(1) DEFAULT '1',
  `pode_ver_inscritos` tinyint(1) DEFAULT '0',
  `pode_registrar_presencas` tinyint(1) DEFAULT '0',
  `pode_ver_relatorios` tinyint(1) DEFAULT '0',
  `pode_silenciar` tinyint(1) DEFAULT '0',
  `pode_expulsar` tinyint(1) DEFAULT '0',
  `pode_liberar_certificados` tinyint(1) DEFAULT '0',
  `silenciado` tinyint(1) DEFAULT '0',
  `silenciado_ate` datetime DEFAULT NULL,
  `data_convite` datetime DEFAULT CURRENT_TIMESTAMP,
  `data_resposta` datetime DEFAULT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `evento_id` (`evento_id`,`usuario_id`),
  KEY `usuario_id` (`usuario_id`),
  CONSTRAINT `evento_colaboradores_ibfk_1` FOREIGN KEY (`evento_id`) REFERENCES `eventos` (`id`) ON DELETE CASCADE,
  CONSTRAINT `evento_colaboradores_ibfk_2` FOREIGN KEY (`usuario_id`) REFERENCES `usuarios` (`id`) ON DELETE CASCADE
) ENGINE=InnoDB AUTO_INCREMENT=12 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `eventos`
--

/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `eventos` (
  `id` int NOT NULL AUTO_INCREMENT,
  `organizador_id` int NOT NULL,
  `titulo` varchar(200) NOT NULL,
  `descricao` text,
  `tipo` varchar(100) DEFAULT NULL,
  `endereco` varchar(255) DEFAULT NULL,
  `cidade` varchar(100) DEFAULT NULL,
  `data_inicio` datetime NOT NULL,
  `data_fim` datetime NOT NULL,
  `carga_horaria` int DEFAULT NULL,
  `vagas_total` int DEFAULT NULL,
  `vagas_disponiveis` int DEFAULT NULL,
  `hub_ate` datetime DEFAULT NULL,
  `publicacao_ate` datetime DEFAULT NULL,
  `inscricoes_abertas` tinyint(1) DEFAULT '0',
  `chat_somente_organizador` tinyint(1) DEFAULT '0',
  `status` enum('RASCUNHO','PUBLICADO','ENCERRADO','CANCELADO','REMOVIDO_ADMIN') DEFAULT 'RASCUNHO',
  `motivo_remocao` text,
  `data_criacao` datetime DEFAULT CURRENT_TIMESTAMP,
  `imagem_nome` varchar(100) DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `organizador_id` (`organizador_id`),
  CONSTRAINT `eventos_ibfk_1` FOREIGN KEY (`organizador_id`) REFERENCES `usuarios` (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=11 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `flyway_schema_history`
--

/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `flyway_schema_history` (
  `installed_rank` int NOT NULL,
  `version` varchar(50) DEFAULT NULL,
  `description` varchar(200) NOT NULL,
  `type` varchar(20) NOT NULL,
  `script` varchar(1000) NOT NULL,
  `checksum` int DEFAULT NULL,
  `installed_by` varchar(100) NOT NULL,
  `installed_on` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `execution_time` int NOT NULL,
  `success` tinyint(1) NOT NULL,
  PRIMARY KEY (`installed_rank`),
  KEY `flyway_schema_history_s_idx` (`success`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `funcionario_permissoes`
--

/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `funcionario_permissoes` (
  `usuario_id` int NOT NULL,
  `visualizar_usuarios` tinyint(1) DEFAULT '0',
  `bloquear_usuarios` tinyint(1) DEFAULT '0',
  `visualizar_eventos` tinyint(1) DEFAULT '1',
  `moderar_eventos` tinyint(1) DEFAULT '0',
  `visualizar_pagamentos` tinyint(1) DEFAULT '0',
  `gerenciar_planos` tinyint(1) DEFAULT '0',
  PRIMARY KEY (`usuario_id`),
  CONSTRAINT `funcionario_permissoes_ibfk_1` FOREIGN KEY (`usuario_id`) REFERENCES `usuarios` (`id`) ON DELETE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `historico_administrativo`
--

/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `historico_administrativo` (
  `id` int NOT NULL AUTO_INCREMENT,
  `usuario_id` int NOT NULL,
  `acao` varchar(100) NOT NULL,
  `entidade` varchar(100) DEFAULT NULL,
  `entidade_id` int DEFAULT NULL,
  `descricao` text,
  `data_acao` datetime DEFAULT CURRENT_TIMESTAMP,
  PRIMARY KEY (`id`),
  KEY `usuario_id` (`usuario_id`),
  CONSTRAINT `historico_administrativo_ibfk_1` FOREIGN KEY (`usuario_id`) REFERENCES `usuarios` (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=6 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `inscricoes`
--

/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `inscricoes` (
  `id` int NOT NULL AUTO_INCREMENT,
  `usuario_id` int NOT NULL,
  `evento_id` int NOT NULL,
  `data_inscricao` datetime DEFAULT CURRENT_TIMESTAMP,
  `status` enum('INSCRITO','CANCELADO','PRESENTE') DEFAULT 'INSCRITO',
  `token_qr` varchar(100) NOT NULL,
  `data_cancelamento` datetime DEFAULT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `token_qr` (`token_qr`),
  UNIQUE KEY `usuario_id` (`usuario_id`,`evento_id`),
  KEY `evento_id` (`evento_id`),
  CONSTRAINT `inscricoes_ibfk_1` FOREIGN KEY (`usuario_id`) REFERENCES `usuarios` (`id`) ON DELETE CASCADE,
  CONSTRAINT `inscricoes_ibfk_2` FOREIGN KEY (`evento_id`) REFERENCES `eventos` (`id`) ON DELETE CASCADE
) ENGINE=InnoDB AUTO_INCREMENT=12 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `mensagens`
--

/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `mensagens` (
  `id` int NOT NULL AUTO_INCREMENT,
  `evento_id` int NOT NULL,
  `usuario_id` int NOT NULL,
  `mensagem` text NOT NULL,
  `data_envio` datetime DEFAULT CURRENT_TIMESTAMP,
  `canal` varchar(20) NOT NULL DEFAULT 'ORGANIZACAO',
  PRIMARY KEY (`id`),
  KEY `evento_id` (`evento_id`),
  KEY `usuario_id` (`usuario_id`),
  CONSTRAINT `mensagens_ibfk_1` FOREIGN KEY (`evento_id`) REFERENCES `eventos` (`id`) ON DELETE CASCADE,
  CONSTRAINT `mensagens_ibfk_2` FOREIGN KEY (`usuario_id`) REFERENCES `usuarios` (`id`) ON DELETE CASCADE
) ENGINE=InnoDB AUTO_INCREMENT=9 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `moderacoes`
--

/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `moderacoes` (
  `id` int NOT NULL AUTO_INCREMENT,
  `evento_id` int NOT NULL,
  `executor_id` int NOT NULL,
  `alvo_id` int DEFAULT NULL,
  `acao` enum('SILENCIAR','DESSILENCIAR','EXPULSAR','SILENCIAR_TODOS','LIBERAR_TODOS','DAR_PERMISSAO','REMOVER_PERMISSAO') NOT NULL,
  `motivo` varchar(255) DEFAULT NULL,
  `data_acao` datetime DEFAULT CURRENT_TIMESTAMP,
  PRIMARY KEY (`id`),
  KEY `evento_id` (`evento_id`),
  KEY `executor_id` (`executor_id`),
  KEY `alvo_id` (`alvo_id`),
  CONSTRAINT `moderacoes_ibfk_1` FOREIGN KEY (`evento_id`) REFERENCES `eventos` (`id`) ON DELETE CASCADE,
  CONSTRAINT `moderacoes_ibfk_2` FOREIGN KEY (`executor_id`) REFERENCES `usuarios` (`id`),
  CONSTRAINT `moderacoes_ibfk_3` FOREIGN KEY (`alvo_id`) REFERENCES `usuarios` (`id`) ON DELETE SET NULL
) ENGINE=InnoDB AUTO_INCREMENT=11 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `notificacoes`
--

/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `notificacoes` (
  `id` int NOT NULL AUTO_INCREMENT,
  `usuario_id` int NOT NULL,
  `evento_id` int DEFAULT NULL,
  `tipo` enum('CONVITE_COLABORADOR','CONVITE_ACEITO','CONVITE_RECUSADO','NOVA_TAREFA','EVENTO_ATUALIZADO','EVENTO_CANCELADO','PAGAMENTO_APROVADO','CERTIFICADO_LIBERADO','SISTEMA','CHAT_GERAL','CHAT_ORGANIZACAO','EVENTO_PROXIMO','INSCRICAO_CONFIRMADA','NOVA_INSCRICAO') NOT NULL,
  `titulo` varchar(200) NOT NULL,
  `mensagem` text NOT NULL,
  `lida` tinyint(1) DEFAULT '0',
  `data_criacao` datetime DEFAULT CURRENT_TIMESTAMP,
  PRIMARY KEY (`id`),
  KEY `usuario_id` (`usuario_id`),
  KEY `evento_id` (`evento_id`),
  CONSTRAINT `notificacoes_ibfk_1` FOREIGN KEY (`usuario_id`) REFERENCES `usuarios` (`id`) ON DELETE CASCADE,
  CONSTRAINT `notificacoes_ibfk_2` FOREIGN KEY (`evento_id`) REFERENCES `eventos` (`id`) ON DELETE CASCADE
) ENGINE=InnoDB AUTO_INCREMENT=23 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `pagamentos`
--

/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `pagamentos` (
  `id` int NOT NULL AUTO_INCREMENT,
  `usuario_id` int NOT NULL,
  `evento_id` int NOT NULL,
  `plano_id` int NOT NULL,
  `valor_pago` decimal(10,2) NOT NULL,
  `status` enum('PENDENTE','APROVADO','RECUSADO','CANCELADO') DEFAULT 'PENDENTE',
  `data_criacao` datetime DEFAULT CURRENT_TIMESTAMP,
  `data_aprovacao` datetime DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `usuario_id` (`usuario_id`),
  KEY `evento_id` (`evento_id`),
  KEY `plano_id` (`plano_id`),
  CONSTRAINT `pagamentos_ibfk_1` FOREIGN KEY (`usuario_id`) REFERENCES `usuarios` (`id`),
  CONSTRAINT `pagamentos_ibfk_2` FOREIGN KEY (`evento_id`) REFERENCES `eventos` (`id`) ON DELETE CASCADE,
  CONSTRAINT `pagamentos_ibfk_3` FOREIGN KEY (`plano_id`) REFERENCES `planos` (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=23 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `perfis`
--

/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `perfis` (
  `id` int NOT NULL AUTO_INCREMENT,
  `nome` varchar(50) NOT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `nome` (`nome`)
) ENGINE=InnoDB AUTO_INCREMENT=4 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

INSERT INTO `perfis` (`id`, `nome`) VALUES
(3, 'ADMIN'),
(2, 'FUNCIONARIO'),
(1, 'USUARIO');

--
-- Table structure for table `planos`
--

/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `planos` (
  `id` int NOT NULL AUTO_INCREMENT,
  `nome` varchar(100) NOT NULL,
  `tipo` enum('HUB','PUBLICACAO') NOT NULL,
  `duracao_dias` int NOT NULL,
  `valor` decimal(10,2) NOT NULL,
  `ativo` tinyint(1) DEFAULT '1',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=7 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

INSERT INTO `planos` (`id`, `nome`, `tipo`, `duracao_dias`, `valor`, `ativo`) VALUES
(1, 'Hub - 30 dias', 'HUB', 30, 30.00, 1),
(2, 'Hub - 60 dias', 'HUB', 60, 55.00, 1),
(3, 'Hub - 90 dias', 'HUB', 90, 75.00, 1),
(4, 'Publicação - 30 dias', 'PUBLICACAO', 30, 30.00, 1),
(5, 'Publicação - 60 dias', 'PUBLICACAO', 60, 55.00, 1),
(6, 'Publicação - 90 dias', 'PUBLICACAO', 90, 75.00, 1);

--
-- Table structure for table `presencas`
--

/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `presencas` (
  `id` int NOT NULL AUTO_INCREMENT,
  `inscricao_id` int NOT NULL,
  `data_leitura` datetime DEFAULT CURRENT_TIMESTAMP,
  PRIMARY KEY (`id`),
  UNIQUE KEY `inscricao_id` (`inscricao_id`),
  CONSTRAINT `presencas_ibfk_1` FOREIGN KEY (`inscricao_id`) REFERENCES `inscricoes` (`id`) ON DELETE CASCADE
) ENGINE=InnoDB AUTO_INCREMENT=3 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `tarefas_evento`
--

/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `tarefas_evento` (
  `id` int NOT NULL AUTO_INCREMENT,
  `evento_id` int NOT NULL,
  `criado_por` int NOT NULL,
  `responsavel_id` int DEFAULT NULL,
  `titulo` varchar(200) NOT NULL,
  `descricao` text,
  `prioridade` enum('BAIXA','MEDIA','ALTA','URGENTE') DEFAULT 'MEDIA',
  `status` enum('PENDENTE','EM_ANDAMENTO','CONCLUIDA','CANCELADA') DEFAULT 'PENDENTE',
  `prazo` datetime DEFAULT NULL,
  `data_criacao` datetime DEFAULT CURRENT_TIMESTAMP,
  `data_conclusao` datetime DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `evento_id` (`evento_id`),
  KEY `criado_por` (`criado_por`),
  KEY `responsavel_id` (`responsavel_id`),
  CONSTRAINT `tarefas_evento_ibfk_1` FOREIGN KEY (`evento_id`) REFERENCES `eventos` (`id`) ON DELETE CASCADE,
  CONSTRAINT `tarefas_evento_ibfk_2` FOREIGN KEY (`criado_por`) REFERENCES `usuarios` (`id`),
  CONSTRAINT `tarefas_evento_ibfk_3` FOREIGN KEY (`responsavel_id`) REFERENCES `usuarios` (`id`) ON DELETE SET NULL
) ENGINE=InnoDB AUTO_INCREMENT=3 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `usuarios`
--

/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `usuarios` (
  `id` int NOT NULL AUTO_INCREMENT,
  `nome` varchar(512) NOT NULL,
  `email` varchar(512) NOT NULL,
  `senha` varchar(255) NOT NULL,
  `telefone` varchar(255) DEFAULT NULL,
  `perfil_id` int NOT NULL,
  `ativo` tinyint(1) DEFAULT '1',
  `data_cadastro` datetime DEFAULT CURRENT_TIMESTAMP,
  `telefone_criptografado` tinyint(1) NOT NULL DEFAULT '0',
  `email_hash` char(64) DEFAULT NULL,
  `email_criptografado` tinyint(1) NOT NULL DEFAULT '0',
  `nome_criptografado` tinyint(1) NOT NULL DEFAULT '0',
  PRIMARY KEY (`id`),
  UNIQUE KEY `email` (`email`),
  UNIQUE KEY `uk_usuarios_email_hash` (`email_hash`),
  KEY `perfil_id` (`perfil_id`),
  CONSTRAINT `usuarios_ibfk_1` FOREIGN KEY (`perfil_id`) REFERENCES `perfis` (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=15 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;
/*!40103 SET TIME_ZONE=@OLD_TIME_ZONE */;

/*!40101 SET SQL_MODE=@OLD_SQL_MODE */;
/*!40014 SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS */;
/*!40014 SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS */;
/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
/*!40111 SET SQL_NOTES=@OLD_SQL_NOTES */;

-- Dump completed on 2026-11-07 16:54:55
