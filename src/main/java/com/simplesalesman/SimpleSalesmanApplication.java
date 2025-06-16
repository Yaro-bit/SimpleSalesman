package com.simplesalesman;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.core.env.Environment;

/**
 * Main application class for the Simple Salesman Spring Boot application.
 * 
 * This class serves as the entry point for the Spring Boot application and handles
 * application startup configuration and logging.
 * 
 * @SpringBootApplication enables:
 * - @Configuration: Allows bean definitions
 * - @EnableAutoConfiguration: Enables Spring Boot's auto-configuration
 * - @ComponentScan: Enables component scanning in the current package and sub-packages
 * 
 * @author SimpleSalesman Team
 * @version 0.0.5
 * @since 0.0,5
 */
@SpringBootApplication
public class SimpleSalesmanApplication {

	/**
	 * Logger instance for this class using SLF4J.
	 * Used to log application startup information and debugging messages.
	 */
	private static final Logger log = LoggerFactory.getLogger(SimpleSalesmanApplication.class);

	/**
	 * Main method - entry point of the Spring Boot application.
	 * 
	 * This method:
	 * 1. Starts the Spring Boot application context
	 * 2. Retrieves environment configuration
	 * 3. Logs startup information including app name, port, and active profiles
	 * 
	 * @param args Command line arguments passed to the application.
	 *             Common Spring Boot arguments include:
	 *             --server.port=8080 (to change port)
	 *             --spring.profiles.active=dev (to set active profile)
	 */
	public static void main(String[] args) {
		// Start the Spring Boot application and get the application context
		var context = SpringApplication.run(SimpleSalesmanApplication.class, args);
		
		// Get the Spring Environment to access configuration properties
		Environment env = context.getEnvironment();
		
		// Retrieve application configuration with fallback defaults
		String appName = env.getProperty("spring.application.name", "SimpleSalesman");
		String port = env.getProperty("server.port", "8081");
		
		// Get active profiles as comma-separated string
		String profile = String.join(", ", env.getActiveProfiles());
		
		// Log startup information in German (matching existing log messages)
		log.info("Anwendung '{}' gestartet", appName);
		log.info("Läuft auf: http://localhost:{}", port);
		log.info("Aktives Profil: {}", profile.isBlank() ? "default" : profile);
		
		// Additional system and runtime information for better support and debugging
		logSystemInformation();
		logRuntimeInformation();
		logApplicationConfiguration(env);
	}
	
	/**
	 * Logs system-level information useful for debugging and support.
	 * This includes OS details, Java version, and system properties.
	 */
	private static void logSystemInformation() {
		log.info("=== System Information ===");
		log.info("Java Version: {} ({})", 
			System.getProperty("java.version"), 
			System.getProperty("java.vendor"));
		log.info("Operating System: {} {} ({})", 
			System.getProperty("os.name"),
			System.getProperty("os.version"),
			System.getProperty("os.arch"));
		log.info("User Directory: {}", System.getProperty("user.dir"));
		log.info("Java Home: {}", System.getProperty("java.home"));
	}
	
	/**
	 * Logs runtime and memory information for performance monitoring.
	 * Helps identify potential memory issues during startup.
	 */
	private static void logRuntimeInformation() {
		Runtime runtime = Runtime.getRuntime();
		long maxMemory = runtime.maxMemory();
		long totalMemory = runtime.totalMemory();
		long freeMemory = runtime.freeMemory();
		long usedMemory = totalMemory - freeMemory;
		
		log.info("=== Runtime Information ===");
		log.info("Available Processors: {}", runtime.availableProcessors());
		log.info("Max Memory: {} MB", maxMemory / (1024 * 1024));
		log.info("Total Memory: {} MB", totalMemory / (1024 * 1024));
		log.info("Used Memory: {} MB", usedMemory / (1024 * 1024));
		log.info("Free Memory: {} MB", freeMemory / (1024 * 1024));
	}
	
	/**
	 * Logs important application configuration properties.
	 * Helps with troubleshooting configuration issues.
	 * 
	 * @param env Spring Environment containing configuration properties
	 */
	private static void logApplicationConfiguration(Environment env) {
		log.info("=== Application Configuration ===");
		
		// Common Spring Boot properties
		logProperty(env, "spring.application.name", "Application Name");
		logProperty(env, "server.port", "Server Port");
		logProperty(env, "server.servlet.context-path", "Context Path");
		logProperty(env, "logging.level.root", "Root Log Level");
		logProperty(env, "logging.level.com.simplesalesman", "App Log Level");
		
		// Database configuration (if applicable)
		logProperty(env, "spring.datasource.url", "Database URL");
		logProperty(env, "spring.datasource.driver-class-name", "Database Driver");
		logProperty(env, "spring.jpa.hibernate.ddl-auto", "Hibernate DDL Auto");
		
		// Security configuration (if applicable)
		logProperty(env, "management.endpoints.web.exposure.include", "Actuator Endpoints");
		logProperty(env, "management.endpoint.health.show-details", "Health Details");
		
		// Custom application properties
		logProperty(env, "app.version", "Application Version");
		logProperty(env, "app.environment", "Environment");
		
		log.info("=== Startup Complete ===");
	}
	
	/**
	 * Helper method to safely log a property value.
	 * Handles cases where properties might not be set.
	 * 
	 * @param env Spring Environment
	 * @param propertyName Name of the property to log
	 * @param displayName Human-readable name for the property
	 */
	private static void logProperty(Environment env, String propertyName, String displayName) {
		String value = env.getProperty(propertyName);
		if (value != null && !value.trim().isEmpty()) {
			// Mask sensitive information (passwords, keys, etc.)
			if (propertyName.toLowerCase().contains("password") || 
				propertyName.toLowerCase().contains("secret") ||
				propertyName.toLowerCase().contains("key")) {
				log.info("{}: [MASKED]", displayName);
			} else {
				log.info("{}: {}", displayName, value);
			}
		} else {
			log.debug("{}: [NOT SET]", displayName);
		}
	}
}