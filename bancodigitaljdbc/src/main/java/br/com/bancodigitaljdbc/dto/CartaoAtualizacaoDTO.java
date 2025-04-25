package br.com.bancodigitaljdbc.dto;

import java.math.BigDecimal;

public record CartaoAtualizacaoDTO(
 BigDecimal limite,
 String status,
 String senha,
 BigDecimal limiteDiario
) {}
