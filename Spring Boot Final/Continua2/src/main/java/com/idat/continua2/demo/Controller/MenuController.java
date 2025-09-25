package com.idat.continua2.demo.Controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;

/**
 * Controlador para el menú principal de la aplicación
 * Implementa Spring Security para mostrar opciones según roles
 * Siguiendo principios Spring Boot MVC
 */
@Controller
public class MenuController {
    
    /**
     * Muestra el menú principal con opciones según el rol del usuario
     * @param model - Modelo para la vista Thymeleaf
     * @return vista menu
     */
    @GetMapping("/menu")
    public String mostrarMenu(Model model){
        try {
            // Obtener información del usuario autenticado
            Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
            
            if (authentication != null && authentication.isAuthenticated()) {
                String username = authentication.getName();
                model.addAttribute("username", username);
                model.addAttribute("authorities", authentication.getAuthorities());
                
                // Agregar información adicional para el menú
                model.addAttribute("hasClienteAccess", 
                    authentication.getAuthorities().stream()
                        .anyMatch(auth -> auth.getAuthority().startsWith("CLIENTE_")));
                        
                model.addAttribute("hasProductoAccess", 
                    authentication.getAuthorities().stream()
                        .anyMatch(auth -> auth.getAuthority().startsWith("PRODUCTO_")));
                        
                model.addAttribute("hasProveedorAccess", 
                    authentication.getAuthorities().stream()
                        .anyMatch(auth -> auth.getAuthority().startsWith("PROVEEDOR_")));
                        
                model.addAttribute("hasPedidoAccess", 
                    authentication.getAuthorities().stream()
                        .anyMatch(auth -> auth.getAuthority().startsWith("PEDIDO_")));
                        
                model.addAttribute("hasUsuarioAccess", 
                    authentication.getAuthorities().stream()
                        .anyMatch(auth -> auth.getAuthority().startsWith("USUARIO_")));
            }
            
            return "menu";
        } catch (Exception e) {
            model.addAttribute("error", "Error al cargar el menú: " + e.getMessage());
            return "error";
        }
    }

    /**
     * Dashboard con estadísticas básicas (opcional)
     * @param model - Modelo para la vista
     * @return vista dashboard
     */
    @GetMapping("/dashboard")
    public String mostrarDashboard(Model model) {
        try {
            // TODO: Agregar estadísticas básicas
            // model.addAttribute("totalClientes", clienteService.count());
            // model.addAttribute("totalProductos", productoService.count());
            // model.addAttribute("totalPedidos", pedidoService.count());
            
            return "dashboard";
        } catch (Exception e) {
            model.addAttribute("error", "Error al cargar dashboard: " + e.getMessage());
            return "error";
        }
    }
}
