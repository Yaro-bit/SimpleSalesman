package com.simplesalesman.web.config;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.ViewControllerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;

/**
 * Web MVC Konfiguration für die Thymeleaf-Oberfläche.
 * 
 * Konfiguriert View-Controller, statische Ressourcen und andere
 * web-spezifische Einstellungen für die SimpleSalesman Anwendung.
 */
@Configuration
public class WebMvcConfig implements WebMvcConfigurer {

    private static final Logger log = LoggerFactory.getLogger(WebMvcConfig.class);

    /**
     * Konfiguriert einfache View-Controller ohne Backend-Logik.
     */
    @Override
    public void addViewControllers(ViewControllerRegistry registry) {
        log.info("Configuring view controllers");
        
        // Root-Redirect zur Dashboard
        registry.addRedirectViewController("/", "/web/dashboard");
        
        // Direkte View-Mappings für statische Seiten
        registry.addViewController("/web/help").setViewName("help");
        registry.addViewController("/web/about").setViewName("about");
        
        log.debug("View controllers configured successfully");
    }

    /**
     * Konfiguriert statische Ressourcen (CSS, JS, Bilder).
     */
    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        log.info("Configuring static resource handlers");
        
        // Standard Spring Boot statische Ressourcen
        registry.addResourceHandler("/css/**")
                .addResourceLocations("classpath:/static/css/")
                .setCachePeriod(3600); // 1 Stunde Cache

        registry.addResourceHandler("/js/**")
                .addResourceLocations("classpath:/static/js/")
                .setCachePeriod(3600);

        registry.addResourceHandler("/images/**")
                .addResourceLocations("classpath:/static/images/")
                .setCachePeriod(86400); // 24 Stunden Cache für Bilder

        // Favicon
        registry.addResourceHandler("/favicon.ico")
                .addResourceLocations("classpath:/static/favicon.ico")
                .setCachePeriod(86400);
        
        log.debug("Static resource handlers configured successfully");
    }
}