package br.com.bancodigitaljdbc.service;

import java.math.BigDecimal;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import br.com.bancodigitaljdbc.dto.AtualizacaoContaDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import br.com.bancodigitaljdbc.dao.ClienteDAO;
import br.com.bancodigitaljdbc.dao.ContaDAO;
import br.com.bancodigitaljdbc.dto.ContaDTO;
import br.com.bancodigitaljdbc.mapper.ContaMapper;
import br.com.bancodigitaljdbc.model.Cliente;
import br.com.bancodigitaljdbc.model.Conta;

@Service
public class ContaService {

    @Autowired
    private ContaDAO contaDAO;

    @Autowired
    private ClienteDAO clienteDAO;

    public void criarConta(ContaDTO dto) throws SQLException {
        Cliente cliente = clienteDAO.buscarClientePorId(dto.clienteId());
        if (cliente == null) throw new IllegalArgumentException("Cliente não encontrado.");

        Conta conta = new Conta(
                null,
                dto.numero(),
                cliente,
                dto.chavePix(),
                dto.tipo().toUpperCase(),
                dto.limiteEspecial() != null ? dto.limiteEspecial() : BigDecimal.ZERO
        );

        switch (dto.tipo().toUpperCase()) {
            case "CORRENTE" -> conta.setRegraTaxa(new RegraTaxaContaCorrente());
            case "POUPANCA" -> conta.setRegraRendimento(new RegraRendimentoContaPoupanca());
            default -> throw new IllegalArgumentException("Tipo de conta inválido.");
        }

        contaDAO.criarConta(conta);
    }

    public ContaDTO buscarContaPorId(Long id) throws SQLException {
        Conta conta = contaDAO.buscarContaPorId(id);
        return ContaMapper.toDTO(conta);
    }

    public BigDecimal consultarSaldo(Long id) throws SQLException {
        return contaDAO.buscarContaPorId(id).getSaldo();
    }

    public void depositar(Long id, BigDecimal valor) throws SQLException {
        Conta conta = contaDAO.buscarContaPorId(id);
        conta.depositar(valor);
        contaDAO.atualizarConta(conta);
    }

    public void sacar(Long id, BigDecimal valor) throws SQLException {
        Conta conta = contaDAO.buscarContaPorId(id);
        if (!conta.sacar(valor)) {
            throw new IllegalArgumentException("Saldo insuficiente.");
        }
        contaDAO.atualizarConta(conta);
    }

    public void transferir(Long origemId, Long destinoId, BigDecimal valor) throws SQLException {
        Conta origem = contaDAO.buscarContaPorId(origemId);
        Conta destino = contaDAO.buscarContaPorId(destinoId);

        origem.transferirPara(destino, valor);

        contaDAO.atualizarConta(origem);
        contaDAO.atualizarConta(destino);
    }

    public void realizarPix(Long origemId, String chavePixDestino, BigDecimal valor) throws SQLException {
        Conta origem = contaDAO.buscarContaPorId(origemId);
        Conta destino = contaDAO.buscarContaPorChavePix(chavePixDestino);

        if (destino == null) throw new IllegalArgumentException("Chave Pix não encontrada.");

        origem.transferirPara(destino, valor);

        contaDAO.atualizarConta(origem);
        contaDAO.atualizarConta(destino);
    }

    public String gerarExtratosCliente(Long clienteId) throws SQLException {
        List<Conta> contas = contaDAO.buscarContasPorClienteId(clienteId);
        StringBuilder sb = new StringBuilder();

        for (Conta conta : contas) {
            sb.append(conta.gerarExtrato()).append("\n\n");
        }

        return sb.toString();
    }

   
    public void aplicarTaxaManutencao(Long id) throws SQLException {
        Conta conta = contaDAO.buscarContaPorId(id);
        conta.aplicarTaxa();
        contaDAO.atualizarConta(conta);
    }

    public void aplicarRendimento(Long id) throws SQLException {
        Conta conta = contaDAO.buscarContaPorId(id);
        conta.aplicarRendimento();
        contaDAO.atualizarConta(conta);
    }

    public String gerarExtrato(Long id) throws SQLException {
        Conta conta = contaDAO.buscarContaPorId(id);
        return conta.gerarExtrato();
    }

    public void deletarConta(Long id) throws SQLException {
        Conta conta = contaDAO.buscarContaPorId(id); // Verifica se a conta existe
        contaDAO.deletarConta(id);
    }

    public void atualizarConta(Long id, AtualizacaoContaDTO dto) throws SQLException {
        Conta conta = contaDAO.buscarContaPorId(id); // valida existência
        if (dto.tipoConta() != null) conta.setTipoConta(dto.tipoConta());
        if (dto.chavePix() != null) conta.setChavePix(dto.chavePix());
        if (dto.limiteEspecial() != null) conta.setLimiteEspecial(dto.limiteEspecial());

        contaDAO.atualizarDadosConta(conta);
    }

    public List<ContaDTO> buscarContasPorClienteId(Long clienteId) throws SQLException {
        List<Conta> contas = contaDAO.buscarContasPorClienteId(clienteId);
        List<ContaDTO> contasDTO = new ArrayList<>();

        for (Conta conta : contas) {
            contasDTO.add(ContaMapper.toDTO(conta));
        }

        return contasDTO;
    }
}
