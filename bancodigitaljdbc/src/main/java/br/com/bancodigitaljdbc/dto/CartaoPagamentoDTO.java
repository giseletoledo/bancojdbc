package br.com.bancodigitaljdbc.dto;

import java.math.BigDecimal;

public record CartaoPagamentoDTO(
 BigDecimal valor,
 String descricao
) {}

