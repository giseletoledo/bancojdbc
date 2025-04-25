package br.com.bancodigitaljdbc.controller;

import java.math.BigDecimal;

public record PixRequest(String chavePix, BigDecimal valor) {}

