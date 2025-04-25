package br.com.bancodigitaljdbc.config;


import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;

public class DatabaseConfig {
    private static final String URL = "jdbc:h2:~/banco-digital"; // Caminho do banco
    private static final String USER = "sa"; // Usuário padrão do H2
    private static final String PASSWORD = ""; // Senha vazia por padrão

    public static Connection conectar() throws SQLException {
        return DriverManager.getConnection(URL, USER, PASSWORD);
    }

    public static void criarTabelas() {
        try (Connection conn = conectar();
             Statement stmt = conn.createStatement()) {
        	
        	//Criar tabelas do cliente
        	    stmt.execute("""
        		    CREATE TABLE IF NOT EXISTS clientes (
        		        id BIGINT AUTO_INCREMENT PRIMARY KEY,
        		        nome VARCHAR(100) NOT NULL,
        		        cpf VARCHAR(11) UNIQUE NOT NULL,
        		        data_nascimento DATE NOT NULL,
        		        tipo_cliente VARCHAR(20) NOT NULL
        		    );
        		""");
        	    
        		stmt.execute("""
        		    CREATE TABLE IF NOT EXISTS enderecos (
        		        id BIGINT AUTO_INCREMENT PRIMARY KEY,
        		        cliente_id BIGINT NOT NULL,
        		        rua VARCHAR(100) NOT NULL,
        		        numero VARCHAR(10) NOT NULL,
        		        complemento VARCHAR(50),
        		        cidade VARCHAR(100) NOT NULL,
        		        estado VARCHAR(2) NOT NULL,
        		        cep VARCHAR(9) NOT NULL, -- Formato com hífen: 12345-678
        		        FOREIGN KEY (cliente_id) REFERENCES clientes(id) ON DELETE CASCADE
        		    );
        		""");

           
            // Criar a tabela Contas
            stmt.execute("""
                CREATE TABLE IF NOT EXISTS contas (
            		id BIGINT AUTO_INCREMENT PRIMARY KEY,
            		numero_conta VARCHAR(20) NOT NULL UNIQUE,  -- <-- Novo campo
            		cliente_id BIGINT NOT NULL,
            		saldo DECIMAL(15,2) DEFAULT 0.00,
            		tipo_conta VARCHAR(20) NOT NULL,
            		chave_pix VARCHAR(100),
            		limite_especial DECIMAL(15,2) DEFAULT 0.00,
            		FOREIGN KEY (cliente_id) REFERENCES clientes(id) ON DELETE CASCADE
);

            """);
            

            // Criar a tabela Cartões
            stmt.execute("""
                CREATE TABLE IF NOT EXISTS cartoes (
            		id BIGINT AUTO_INCREMENT PRIMARY KEY,
            		conta_id BIGINT NOT NULL,
            		tipo_cartao VARCHAR(20) NOT NULL, -- CREDITO ou DEBITO
            		numero_cartao VARCHAR(16) UNIQUE NOT NULL,
            		limite DECIMAL(15,2) DEFAULT 0.00,
            		status VARCHAR(10) DEFAULT 'ATIVO',
            		senha VARCHAR(10),
            		limite_diario DECIMAL(15,2), -- apenas para débito
            		FOREIGN KEY (conta_id) REFERENCES contas(id) ON DELETE CASCADE
            		);
            """);

            // Criar a tabela Transações
            stmt.execute("""
                CREATE TABLE IF NOT EXISTS transacoes (
                    id BIGINT AUTO_INCREMENT PRIMARY KEY,
                    conta_id BIGINT NOT NULL,
                    valor DECIMAL(15,2) NOT NULL,
                    tipo_transacao VARCHAR(20) NOT NULL,
                    descricao VARCHAR(255),
                    data_transacao TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
                    FOREIGN KEY (conta_id) REFERENCES contas(id) ON DELETE CASCADE
                );
            """);

            // Criar a tabela Transações de Cartão
            stmt.execute("""
                CREATE TABLE IF NOT EXISTS cartao_transacoes (
                    id BIGINT AUTO_INCREMENT PRIMARY KEY,
                    cartao_id BIGINT NOT NULL,
                    valor DECIMAL(15,2) NOT NULL,
                    descricao VARCHAR(255) NOT NULL,
                    data_transacao TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
                    FOREIGN KEY (cartao_id) REFERENCES cartoes(id) ON DELETE CASCADE
                );
            """);

            System.out.println("Tabelas criadas com sucesso!");
        } catch (SQLException e) {
            e.printStackTrace();
            throw new RuntimeException("Erro ao criar tabelas no banco de dados.");
        }
    }
}

