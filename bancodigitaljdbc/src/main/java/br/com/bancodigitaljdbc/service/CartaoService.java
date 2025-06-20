package br.com.bancodigitaljdbc.service;

import java.math.BigDecimal;
import java.sql.SQLException;
import java.util.List;
import java.util.Random;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import br.com.bancodigitaljdbc.dao.CartaoDAO;
import br.com.bancodigitaljdbc.dao.ContaDAO;
import br.com.bancodigitaljdbc.dto.CartaoDTO;
import br.com.bancodigitaljdbc.mapper.CartaoMapper;
import br.com.bancodigitaljdbc.model.Cartao;
import br.com.bancodigitaljdbc.model.CartaoTransacao;
import br.com.bancodigitaljdbc.model.Conta;
import br.com.bancodigitaljdbc.model.TipoCartao;

@Service
public class CartaoService {

    @Autowired
    private CartaoDAO cartaoDAO;

    @Autowired
    private ContaDAO contaDAO;

    // POST /cartoes - Criar cartão
    public void criarCartao(CartaoDTO dto) throws SQLException {
        Conta conta = contaDAO.buscarContaPorId(dto.contaId());
        TipoCartao tipo = dto.tipoCartao();

        BigDecimal limite = switch (conta.getCliente().getTipo()) {
            case COMUM -> BigDecimal.valueOf(1000);
            case SUPER -> BigDecimal.valueOf(5000);
            case PREMIUM -> BigDecimal.valueOf(10000);
        };

        Cartao cartao = new Cartao();
        cartao.setContaId(conta.getId());
        cartao.setNumeroCartao(gerarNumeroCartao());
        cartao.setTipoCartao(tipo);
        cartao.setLimite(limite);
        cartao.setAtivo(true);
        cartao.setSenha(dto.senha());

        if (tipo == TipoCartao.DEBITO) {
            cartao.setLimiteDiario(BigDecimal.valueOf(1000));
        }

        cartaoDAO.criarCartao(cartao);
    }

    public Cartao buscarCartao(Long id) throws SQLException {
        return cartaoDAO.buscarCartaoPorId(id);
    }

    public CartaoDTO buscarCartaoPorId(Long id) throws SQLException {
        return CartaoMapper.toDTO(cartaoDAO.buscarCartaoPorId(id));
    }

    public void registrarPagamento(Long id, BigDecimal valor, String descricao) throws SQLException {
        cartaoDAO.registrarPagamento(id, valor, descricao);
    }

    public void alterarLimite(Long id, BigDecimal novoLimite) throws SQLException {
        cartaoDAO.atualizarLimite(id, novoLimite);
    }

    public void alterarStatus(Long id, String status) throws SQLException {
        boolean ativo = status.equalsIgnoreCase("ATIVO");
        cartaoDAO.atualizarStatus(id, ativo);
    }

    public void alterarSenha(Long id, String novaSenha) throws SQLException {
        cartaoDAO.atualizarSenha(id, novaSenha);
    }

    public void alterarLimiteDiario(Long id, BigDecimal limiteDiario) throws SQLException {
        cartaoDAO.atualizarLimiteDiario(id, limiteDiario);
    }

    public List<CartaoTransacao> consultarFatura(Long id) throws SQLException {
        return cartaoDAO.buscarTransacoesPorCartao(id);
    }

    private String gerarNumeroCartao() {
        Random random = new Random();
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < 16; i++) {
            sb.append(random.nextInt(10));
        }
        return sb.toString();
    }

    public void atualizarCartao(Long id, CartaoDTO dto) throws SQLException {
        Cartao cartaoExistente = cartaoDAO.buscarCartaoPorId(id);
        if (cartaoExistente == null) {
            throw new IllegalArgumentException("Cartão não encontrado.");
        }

        // Atualiza campos que forem necessários (exemplo)
        cartaoExistente.setSenha(dto.senha());
        cartaoExistente.setTipoCartao(dto.tipoCartao());
        cartaoExistente.setLimite(dto.limite());

        if (dto.tipoCartao() == TipoCartao.DEBITO && dto.limiteDiario() != null) {
            cartaoExistente.setLimiteDiario(dto.limiteDiario());
        }

        cartaoDAO.atualizarCartao(cartaoExistente);
    }

    public void deletarCartao(Long id) throws SQLException {
        Cartao cartao = cartaoDAO.buscarCartaoPorId(id);
        if (cartao == null) {
            throw new IllegalArgumentException("Cartão não encontrado.");
        }

        cartaoDAO.atualizarStatus(id, false); // Exclusão lógica
    }
}
