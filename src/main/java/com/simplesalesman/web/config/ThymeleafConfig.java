package com.simplesalesman.web.config;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.ViewResolver;
import org.thymeleaf.spring6.SpringTemplateEngine;
import org.thymeleaf.spring6.view.ThymeleafViewResolver;
import org.thymeleaf.templatemode.TemplateMode;
import org.thymeleaf.templateresolver.ClassLoaderTemplateResolver;

/**
 * Thymeleaf-spezifische Konfiguration für die Web-Oberfläche.
 * 
 * Konfiguriert Template-Engine, View-Resolver und weitere
 * Thymeleaf-Einstellungen für optimale Performance und Entwicklung.
 */
@Configuration
public class ThymeleafConfig {

    private static final Logger log = LoggerFactory.getLogger(ThymeleafConfig.class);

    /**
     * Konfiguriert den Template-Resolver für Thymeleaf.
     * 
     * @return Konfigurierter ClassLoaderTemplateResolver
     */
    @Bean
    public ClassLoaderTemplateResolver templateResolver() {
        log.info("Configuring Thymeleaf template resolver");
        
        ClassLoaderTemplateResolver templateResolver = new ClassLoaderTemplateResolver();
        
        // Template-Pfad und -Suffix
        templateResolver.setPrefix("templates/");
        templateResolver.setSuffix(".html");
        templateResolver.setTemplateMode(TemplateMode.HTML);
        
        // Encoding
        templateResolver.setCharacterEncoding("UTF-8");
        
        // Cache-Einstellungen (abhängig von Umgebung)
        templateResolver.setCacheable(true);
        templateResolver.setCacheTTLMs(3600000L); // 1 Stunde
        
        // Template-Resolver-Reihenfolge
        templateResolver.setOrder(1);
        
        log.debug("Template resolver configured: prefix='templates/', suffix='.html'");
        return templateResolver;
    }

    /**
     * Konfiguriert die Thymeleaf Template Engine.
     * 
     * @return Konfigurierte SpringTemplateEngine
     */
    @Bean
    public SpringTemplateEngine templateEngine() {
        log.info("Configuring Thymeleaf template engine");
        
        SpringTemplateEngine templateEngine = new SpringTemplateEngine();
        templateEngine.setTemplateResolver(templateResolver());
        
        // Zusätzliche Dialekte können hier hinzugefügt werden
        // templateEngine.addDialect(new LayoutDialect());
        
        log.debug("Template engine configured successfully");
        return templateEngine;
    }

    /**
     * Konfiguriert den Thymeleaf View Resolver.
     * 
     * @return Konfigurierter ThymeleafViewResolver
     */
    @Bean
    public ViewResolver viewResolver() {
        log.info("Configuring Thymeleaf view resolver");
        
        ThymeleafViewResolver viewResolver = new ThymeleafViewResolver();
        viewResolver.setTemplateEngine(templateEngine());
        viewResolver.setCharacterEncoding("UTF-8");
        
        // Content-Type
        viewResolver.setContentType("text/html");
        
        // View-Namen-Präfixe für bessere Organisation
        viewResolver.setViewNames(new String[]{"*"});
        
        // Reihenfolge (niedrigere Zahlen haben höhere Priorität)
        viewResolver.setOrder(1);
        
        log.debug("View resolver configured successfully");
        return viewResolver;
    }
}