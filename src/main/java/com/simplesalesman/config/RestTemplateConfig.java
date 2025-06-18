package com.simplesalesman.config;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestTemplate;
import org.springframework.http.client.SimpleClientHttpRequestFactory;

/**
 * Configuration class for providing a RestTemplate bean in the SimpleSalesman application.
 *
 * The {@link RestTemplate} is a synchronous HTTP client used to make calls to external APIs,
 * such as the public weather service or internal system endpoints.
 *
 * This configuration is necessary because Spring Boot (since 2.4+) no longer registers
 * a default RestTemplate bean automatically.
 *
 * Usage:
 * - Inject RestTemplate via @Autowired or constructor injection
 * - Used primarily in WeatherClient or other outbound API services
 *
 * Logging:
 * - A log entry is created upon bean initialization
 *
 * @author SimpleSalesman Team
 * @version 0.0.6
 * @since 0.0.5
 */
@Configuration
public class RestTemplateConfig {

    private static final Logger logger = LoggerFactory.getLogger(RestTemplateConfig.class);

    @Bean
    public RestTemplate restTemplate() {
        logger.info("RestTemplate bean initialized with basic timeout settings");

        SimpleClientHttpRequestFactory factory = new SimpleClientHttpRequestFactory();
        factory.setConnectTimeout(5000); // 5 seconds
        factory.setReadTimeout(5000);    // 5 seconds

        return new RestTemplate(factory);
    }
}
