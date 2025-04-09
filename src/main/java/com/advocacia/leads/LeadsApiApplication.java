package com.advocacia.leads;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication(scanBasePackages = "com.advocacia.leads")
@EntityScan(basePackages = { "com.advocacia.leads.domain", "com.advocacia.leads.infrastructure.auditing" })
@EnableJpaRepositories(basePackages = { "com.advocacia.leads.infrastructure.repositories", "com.advocacia.leads.infrastructure.auditing" })
@EnableScheduling
public class LeadsApiApplication {
	public static void main(String[] args) {
		SpringApplication.run(LeadsApiApplication.class, args);
	}
}
