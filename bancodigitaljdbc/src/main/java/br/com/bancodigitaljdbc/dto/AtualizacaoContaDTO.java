package br.com.bancodigitaljdbc.dto;

import java.math.BigDecimal;

public record AtualizacaoContaDTO(
        String tipoConta,
        String chavePix,
        BigDecimal limiteEspecial
) {}

