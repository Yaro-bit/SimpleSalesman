package com.simplesalesman;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.core.env.Environment;

@SpringBootApplication
public class SimpleSalesmanApplication {

	private static final Logger log = LoggerFactory.getLogger(SimpleSalesmanApplication.class);

	public static void main(String[] args) {

		var context = SpringApplication.run(SimpleSalesmanApplication.class, args);
		Environment env = context.getEnvironment();

		String appName = env.getProperty("spring.application.name", "SimpleSalesman");
		String port = env.getProperty("server.port", "8081");
		String profile = String.join(", ", env.getActiveProfiles());

		log.info("Anwendung '{}' gestartet", appName);
		log.info("Läuft auf: http://localhost:{}", port);
		log.info("Aktives Profil: {}", profile.isBlank() ? "default" : profile);
	}
}
