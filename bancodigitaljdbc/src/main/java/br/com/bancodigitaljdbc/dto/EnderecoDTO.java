package br.com.bancodigitaljdbc.dto;

public record EnderecoDTO(
	    String rua,
	    String numero,
	    String complemento,
	    String cidade,
	    String estado,
	    String cep
	) {}
