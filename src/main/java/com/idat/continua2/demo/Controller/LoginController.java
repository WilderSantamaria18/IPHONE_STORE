package com.idat.continua2.demo.Controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

/**
 * Controlador para gestión de autenticación y login
 * Siguiendo principios Spring Boot MVC y Spring Security
 */
@Controller
public class LoginController {

    /**
     * Página principal redirige al login
     * @param model - Modelo para la vista
     * @return vista login
     */
    @GetMapping("/")
    public String mostrarLogin(Model model) {
        return "login";
    }

    /**
     * Página de login
     * @param model - Modelo para la vista
     * @param error - Parámetro de error opcional
     * @param logout - Parámetro de logout opcional
     * @return vista login
     */
    @GetMapping("/login")
    public String mostrarLogin2(Model model, 
                               @RequestParam(value = "error", required = false) String error,
                               @RequestParam(value = "logout", required = false) String logout) {
        
        if (error != null) {
            model.addAttribute("error", "Credenciales inválidas. Por favor, intente nuevamente.");
        }
        
        if (logout != null) {
            model.addAttribute("message", "Ha cerrado sesión exitosamente.");
        }
        
        return "login";
    }

    /**
     * Página de acceso denegado
     * @param model - Modelo para la vista
     * @return vista de error 403
     */
    @GetMapping("/access-denied")
    public String accessDenied(Model model) {
        model.addAttribute("error", "No tiene permisos para acceder a esta página.");
        return "error/403";
    }

    /**
     * Manejo de errores de sesión expirada
     * @param model - Modelo para la vista
     * @return vista login con mensaje
     */
    @GetMapping("/session-expired")
    public String sessionExpired(Model model) {
        model.addAttribute("warning", "Su sesión ha expirado. Por favor, inicie sesión nuevamente.");
        return "login";
    }
}