package br.com.bancodigitaljdbc.dto;

import java.math.BigDecimal;

import br.com.bancodigitaljdbc.model.TipoCartao;

public record CartaoDTO(
	    Long id,
	    Long contaId,
	    String numeroCartao,
	    TipoCartao tipoCartao, // "CREDITO" ou "DEBITO"
	    BigDecimal limite,
	    BigDecimal limiteDiario,
	    boolean ativo,
	    String senha
	) {}


