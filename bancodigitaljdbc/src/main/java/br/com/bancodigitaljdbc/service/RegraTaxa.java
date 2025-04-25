package br.com.bancodigitaljdbc.service;

import br.com.bancodigitaljdbc.model.Conta;

public interface RegraTaxa {
    void aplicarTaxa(Conta conta);
}
