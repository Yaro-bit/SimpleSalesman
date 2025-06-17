package com.simplesalesman.web.controller;

import com.simplesalesman.dto.AddressDto;
import com.simplesalesman.dto.ProjectDto;
import com.simplesalesman.service.AddressService;
import com.simplesalesman.service.ProjectService;
import com.simplesalesman.service.WeatherService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * MVC Controller für das Dashboard der Web-Oberfläche.
 * 
 * Zeigt eine Übersicht über Statistiken und wichtige Informationen.
 */
@Controller
@RequestMapping("/web")
public class DashboardWebController {

    private static final Logger log = LoggerFactory.getLogger(DashboardWebController.class);
    
    private final AddressService addressService;
    private final ProjectService projectService;
    private final WeatherService weatherService;

    public DashboardWebController(AddressService addressService, 
                                 ProjectService projectService,
                                 WeatherService weatherService) {
        this.addressService = addressService;
        this.projectService = projectService;
        this.weatherService = weatherService;
    }

    /**
     * Hauptseite der Web-Anwendung mit Dashboard-Übersicht.
     */
    @GetMapping({"", "/", "/dashboard"})
    public String dashboard(Model model, @RequestParam(required = false) String region) {
        log.info("Loading dashboard view");
        
        try {
            // Grundlegende Statistiken
            List<AddressDto> addresses = addressService.getAllAddresses();
            List<ProjectDto> projects = projectService.getAllProjects();
            
            model.addAttribute("totalAddresses", addresses.size());
            model.addAttribute("totalProjects", projects.size());
            
            // Projekt-Status-Verteilung
            Map<String, Long> projectStatusCounts = projects.stream()
                .collect(Collectors.groupingBy(
                    p -> p.getStatus() != null ? p.getStatus() : "UNKNOWN",
                    Collectors.counting()
                ));
            model.addAttribute("projectStatusCounts", projectStatusCounts);
            
            // Neueste Adressen (max 5)
            List<AddressDto> recentAddresses = addresses.stream()
                .limit(5)
                .collect(Collectors.toList());
            model.addAttribute("recentAddresses", recentAddresses);
            
            // Aktuelle Projekte (max 5)
            List<ProjectDto> activeProjects = projects.stream()
                .filter(p -> !"COMPLETED".equals(p.getStatus()) && !"CANCELLED".equals(p.getStatus()))
                .limit(5)
                .collect(Collectors.toList());
            model.addAttribute("activeProjects", activeProjects);
            
            // Wetter-Information (falls Region angegeben)
            if (region != null && !region.trim().isEmpty()) {
                try {
                    String weatherInfo = weatherService.getWeatherForRegion(region);
                    model.addAttribute("weatherInfo", weatherInfo);
                    model.addAttribute("weatherRegion", region);
                } catch (Exception e) {
                    log.warn("Could not fetch weather for region: {}", region, e);
                    model.addAttribute("weatherError", "Wetterdaten nicht verfügbar");
                }
            }
            
            // Für Wetter-Formular
            model.addAttribute("weatherRegion", region != null ? region : "");
            
        } catch (Exception e) {
            log.error("Error loading dashboard data", e);
            model.addAttribute("errorMessage", "Fehler beim Laden der Dashboard-Daten");
        }
        
        return "dashboard";
    }

    /**
     * Schnelle Navigation zu wichtigen Bereichen.
     */
    @GetMapping("/quick-actions")
    public String quickActions() {
        return "quick-actions";
    }
}