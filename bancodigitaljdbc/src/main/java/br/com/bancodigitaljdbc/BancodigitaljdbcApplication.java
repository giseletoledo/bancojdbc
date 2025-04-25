package br.com.bancodigitaljdbc;


import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import br.com.bancodigitaljdbc.config.DatabaseConfig;

@SpringBootApplication
public class BancodigitaljdbcApplication {

	public static void main(String[] args) {
		SpringApplication.run(BancodigitaljdbcApplication.class, args);
		
		 // Criar tabelas no banco ao iniciar
        DatabaseConfig.criarTabelas();
	}

}
