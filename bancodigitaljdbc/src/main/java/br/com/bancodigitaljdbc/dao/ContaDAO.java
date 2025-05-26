package br.com.bancodigitaljdbc.dao;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Repository;

import br.com.bancodigitaljdbc.config.DatabaseConfig;
import br.com.bancodigitaljdbc.mapper.ContaMapper;
import br.com.bancodigitaljdbc.model.Conta;
import br.com.bancodigitaljdbc.model.TipoTransacao;
import br.com.bancodigitaljdbc.model.Transacao;


@Repository
public class ContaDAO {

    public void criarConta(Conta conta) throws SQLException {
        String sql = "INSERT INTO contas (cliente_id, saldo, tipo_conta, chave_pix, numero_conta, limite_especial) VALUES (?, ?, ?, ?, ?, ?)";

        try (Connection connection = DatabaseConfig.conectar();
             PreparedStatement stmt = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {

            stmt.setLong(1, conta.getCliente().getId());
            stmt.setBigDecimal(2, conta.getSaldo());
            stmt.setString(3, conta.getTipoConta());
            stmt.setString(4, conta.getChavePix());
            stmt.setString(5, conta.getNumero());
            stmt.setBigDecimal(6, conta.getLimiteEspecial());

            stmt.executeUpdate();

            try (ResultSet rs = stmt.getGeneratedKeys()) {
                if (rs.next()) {
                    conta.setId(rs.getLong(1));
                }
            }
        }
    }

    public Conta buscarContaPorId(Long id) throws SQLException {
        String sql = "SELECT * FROM contas WHERE id = ?";

        try (Connection connection = DatabaseConfig.conectar();
             PreparedStatement stmt = connection.prepareStatement(sql)) {

            stmt.setLong(1, id);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return ContaMapper.fromResultSet(rs, connection); // Precisa passar a conexão se o mapper usar outras DAOs
                }
            }
        }

        throw new IllegalArgumentException("Conta não encontrada.");
    }
    
    
    public List<Conta> buscarContasPorClienteId(Long clienteId) throws SQLException {
        List<Conta> contas = new ArrayList<>();

        String sql = "SELECT * FROM contas WHERE cliente_id = ?";

        try (Connection connection = DatabaseConfig.conectar();
             PreparedStatement stmt = connection.prepareStatement(sql)) {

            stmt.setLong(1, clienteId);
            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    Conta conta = ContaMapper.fromResultSet(rs, connection);
                    contas.add(conta);
                }
            }
        }

        return contas;
    }

    

    public Conta buscarContaPorChavePix(String chavePix) throws SQLException {
        String sql = "SELECT * FROM contas WHERE chave_pix = ?";

        try (Connection connection = DatabaseConfig.conectar();
             PreparedStatement stmt = connection.prepareStatement(sql)) {

            stmt.setString(1, chavePix);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return ContaMapper.fromResultSet(rs, connection);
                }
            }
        }

        return null;
    }

    public void atualizarConta(Conta conta) throws SQLException {
        String sql = "UPDATE contas SET saldo = ?, limite_especial = ? WHERE id = ?";

        try (Connection connection = DatabaseConfig.conectar();
             PreparedStatement stmt = connection.prepareStatement(sql)) {

            stmt.setBigDecimal(1, conta.getSaldo());
            stmt.setBigDecimal(2, conta.getLimiteEspecial());
            stmt.setLong(3, conta.getId());
            stmt.executeUpdate();

            for (Transacao t : conta.getTransacoesNaoPersistidas()) {
                salvarTransacao(connection, t, conta.getId());
            }
            conta.limparTransacoesNaoPersistidas();
        }
    }

    public void atualizarDadosConta(Conta conta) throws SQLException {
        String sql = "UPDATE contas SET tipo_conta = ?, chave_pix = ?, limite_especial = ? WHERE id = ?";

        try (Connection connection = DatabaseConfig.conectar();
             PreparedStatement stmt = connection.prepareStatement(sql)) {

            stmt.setString(1, conta.getTipoConta());
            stmt.setString(2, conta.getChavePix());
            stmt.setBigDecimal(3, conta.getLimiteEspecial());
            stmt.setLong(4, conta.getId());

            stmt.executeUpdate();
        }
    }


    private void salvarTransacao(Connection connection, Transacao t, Long contaId) throws SQLException {
        String sql = "INSERT INTO transacoes (conta_id, valor, tipo_transacao, descricao) VALUES (?, ?, ?, ?)";

        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setLong(1, contaId);
            stmt.setBigDecimal(2, t.getValor());
            stmt.setString(3, t.getTipo().name());
            stmt.setString(4, t.getDescricao());
            stmt.executeUpdate();
        }
    }

    public List<Transacao> buscarTransacoesPorContaId(Long contaId, Connection connection) throws SQLException {
        List<Transacao> transacoes = new ArrayList<>();

        String sql = "SELECT * FROM transacoes WHERE conta_id = ? ORDER BY data_transacao";

        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setLong(1, contaId);

            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    Transacao transacao = new Transacao(
                        rs.getString("descricao"),
                        rs.getBigDecimal("valor"),
                        TipoTransacao.valueOf(rs.getString("tipo_transacao"))
                    );

                    // Se sua classe Transacao tem setData(LocalDateTime), define a data da transação
                    Timestamp data = rs.getTimestamp("data_transacao");
                    if (data != null) {
                        transacao.setData(data.toLocalDateTime()); // método opcional
                    }

                    transacoes.add(transacao);
                }
            }
        }

        return transacoes;
    }

    public void deletarConta(Long id) throws SQLException {
        String sql = "DELETE FROM contas WHERE id = ?";

        try (Connection connection = DatabaseConfig.conectar();
             PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setLong(1, id);
            int rowsAffected = stmt.executeUpdate();

            if (rowsAffected == 0) {
                throw new IllegalArgumentException("Conta não encontrada para exclusão.");
            }
        }
    }
}
