package com.jlcb.gestaopessoasweb;

import java.util.Arrays;

import javax.annotation.PostConstruct;

import org.flywaydb.core.Flyway;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.core.annotation.Order;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Component;

import com.jlcb.gestaopessoasweb.service.CargaInicialService;

@Component
@Order(1) // Define a ordem de execução
public class FlywayMigrationRunner implements CommandLineRunner {

	@Autowired
	private Environment environment;

	@Autowired
	private Flyway flyway;

	@Autowired
	private CargaInicialService cargaInicialService;

	@PostConstruct
	public void postConstruct() {
		System.out.println("Bean initialized: FlywayMigrationRunner");
	}

	@Override
	public void run(String... args) throws Exception {
		if (Arrays.asList(environment.getActiveProfiles()).contains("prod")) {
			flyway.migrate();
			cargaInicialService.realizarCargaInicial();
		}
	}

}
