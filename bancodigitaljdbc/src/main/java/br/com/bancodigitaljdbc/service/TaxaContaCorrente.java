package br.com.bancodigitaljdbc.service;

import br.com.bancodigitaljdbc.model.Conta;
import br.com.bancodigitaljdbc.model.TipoTransacao;
import br.com.bancodigitaljdbc.model.Transacao;

import java.math.BigDecimal;

public class TaxaContaCorrente implements RegraTaxa {

    @Override
    public void aplicarTaxa(Conta conta) {
        if (!"CORRENTE".equalsIgnoreCase(conta.getTipoConta())) return;

        BigDecimal taxa = switch (conta.getCliente().getTipo()) {
            case COMUM -> BigDecimal.valueOf(12.0);
            case SUPER -> BigDecimal.valueOf(8.0);
            case PREMIUM -> BigDecimal.ZERO;
        };

        BigDecimal saldoTotal = conta.getSaldo().add(conta.getLimiteEspecial());

        if (saldoTotal.compareTo(taxa) >= 0) {
            conta.sacar(taxa);
            conta.adicionarTransacao(new Transacao("Taxa de manutenção", taxa, TipoTransacao.TAXA));
        }
    }
}
