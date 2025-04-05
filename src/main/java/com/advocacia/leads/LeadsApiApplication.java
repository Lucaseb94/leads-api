package com.advocacia.leads;
import io.github.cdimascio.dotenv.DotenvEntry;

import io.github.cdimascio.dotenv.Dotenv;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class LeadsApiApplication {

	public static void main(String[] args) {
		Dotenv dotenv = Dotenv.configure().ignoreIfMissing().load();
		dotenv.entries().forEach(entry -> System.setProperty(entry.getKey(), entry.getValue()));

		// Log para confirmar que a variável foi carregada
		System.out.println("JWT_SECRET: " + System.getProperty("JWT_SECRET"));

		SpringApplication.run(LeadsApiApplication.class, args);
	}

}
