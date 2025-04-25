package br.com.bancodigitaljdbc.dto;

import java.math.BigDecimal;

public record TransferenciaDTO(Long destinoId, BigDecimal valor) {}

