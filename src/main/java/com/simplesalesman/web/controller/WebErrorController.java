package com.simplesalesman.web.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.web.servlet.error.ErrorController;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

import jakarta.servlet.RequestDispatcher;
import jakarta.servlet.http.HttpServletRequest;

/**
 * Error Controller für die Web-Oberfläche.
 * 
 * Behandelt HTTP-Fehler und zeigt benutzerfreundliche Fehlerseiten an.
 */
@Controller
public class WebErrorController implements ErrorController {

    private static final Logger log = LoggerFactory.getLogger(WebErrorController.class);

    /**
     * Behandelt alle Web-UI Fehler und leitet zu entsprechenden Error-Pages weiter.
     */
    @RequestMapping("/error")
    public String handleError(HttpServletRequest request, Model model) {
        // HTTP Status Code ermitteln
        Object status = request.getAttribute(RequestDispatcher.ERROR_STATUS_CODE);
        
        if (status != null) {
            int statusCode = Integer.valueOf(status.toString());
            HttpStatus httpStatus = HttpStatus.resolve(statusCode);
            
            log.warn("Web error occurred: status={}, path={}", 
                    statusCode, request.getAttribute(RequestDispatcher.ERROR_REQUEST_URI));
            
            // Model-Attribute für Error-Page
            model.addAttribute("statusCode", statusCode);
            model.addAttribute("statusText", httpStatus != null ? httpStatus.getReasonPhrase() : "Unknown Error");
            model.addAttribute("requestUri", request.getAttribute(RequestDispatcher.ERROR_REQUEST_URI));
            
            // Spezielle Error-Pages für häufige Fehler
            switch (statusCode) {
                case 404:
                    model.addAttribute("errorTitle", "Seite nicht gefunden");
                    model.addAttribute("errorMessage", "Die angeforderte Seite konnte nicht gefunden werden.");
                    return "error/404";
                    
                case 403:
                    model.addAttribute("errorTitle", "Zugriff verweigert");
                    model.addAttribute("errorMessage", "Sie haben keine Berechtigung für diese Aktion.");
                    return "error/403";
                    
                case 500:
                    model.addAttribute("errorTitle", "Interner Serverfehler");
                    model.addAttribute("errorMessage", "Ein unerwarteter Fehler ist aufgetreten. Bitte versuchen Sie es später erneut.");
                    return "error/500";
                    
                default:
                    model.addAttribute("errorTitle", "Fehler " + statusCode);
                    model.addAttribute("errorMessage", "Ein Fehler ist aufgetreten.");
                    return "error/general";
            }
        }
        
        // Fallback für unbekannte Fehler
        log.error("Unknown web error occurred");
        model.addAttribute("statusCode", "Unknown");
        model.addAttribute("statusText", "Unknown Error");
        model.addAttribute("errorTitle", "Unbekannter Fehler");
        model.addAttribute("errorMessage", "Ein unbekannter Fehler ist aufgetreten.");
        
        return "error/general";
    }
}