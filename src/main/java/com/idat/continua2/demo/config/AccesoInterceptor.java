package com.idat.continua2.demo.config;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

/**
 * Interceptor para auditoría y control de acceso
 * Registra los accesos a diferentes recursos del sistema
 * Proporciona logging de seguridad y auditoría
 */
@Component
@SuppressWarnings("null")
public class AccesoInterceptor implements HandlerInterceptor {
    
    private static final Logger logger = LoggerFactory.getLogger(AccesoInterceptor.class);
    
    /**
     * Se ejecuta antes de procesar la petición
     * Registra información del acceso para auditoría
     */
    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        
        String requestURI = request.getRequestURI();
        String method = request.getMethod();
        String remoteAddr = getClientIP(request);
        
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        String username = (auth != null && auth.isAuthenticated()) ? auth.getName() : "anonymous";
        
        // Registrar acceso solo para rutas importantes (no recursos estáticos)
        if (shouldLogRequest(requestURI)) {
            logger.info("ACCESS: User={}, IP={}, Method={}, URI={}", 
                       username, remoteAddr, method, requestURI);
        }
        
        return true; // Continúa con el procesamiento
    }
    
    /**
     * Se ejecuta después de procesar la petición pero antes de renderizar la vista
     */
    @Override
    public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler, 
                          org.springframework.web.servlet.ModelAndView modelAndView) throws Exception {
        
        // Aquí se puede agregar lógica post-procesamiento si es necesario
        String requestURI = request.getRequestURI();
        
        if (shouldLogRequest(requestURI) && response.getStatus() >= 400) {
            Authentication auth = SecurityContextHolder.getContext().getAuthentication();
            String username = (auth != null) ? auth.getName() : "anonymous";
            
            logger.warn("ACCESS_ERROR: User={}, URI={}, Status={}", 
                       username, requestURI, response.getStatus());
        }
    }
    
    /**
     * Se ejecuta después de completar toda la petición
     */
    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, 
                               Exception ex) throws Exception {
        
        if (ex != null) {
            Authentication auth = SecurityContextHolder.getContext().getAuthentication();
            String username = (auth != null) ? auth.getName() : "anonymous";
            
            logger.error("ACCESS_EXCEPTION: User={}, URI={}, Exception={}", 
                        username, request.getRequestURI(), ex.getMessage());
        }
    }
    
    /**
     * Determina si la petición debe ser registrada
     * Filtra recursos estáticos y peticiones no relevantes
     */
    private boolean shouldLogRequest(String requestURI) {
        return requestURI != null && 
               !requestURI.startsWith("/css/") &&
               !requestURI.startsWith("/js/") &&
               !requestURI.startsWith("/images/") &&
               !requestURI.startsWith("/favicon.ico") &&
               !requestURI.startsWith("/error");
    }
    
    /**
     * Obtiene la IP real del cliente considerando proxies
     */
    private String getClientIP(HttpServletRequest request) {
        String xForwardedFor = request.getHeader("X-Forwarded-For");
        if (xForwardedFor != null && !xForwardedFor.isEmpty()) {
            return xForwardedFor.split(",")[0].trim();
        }
        
        String xRealIP = request.getHeader("X-Real-IP");
        if (xRealIP != null && !xRealIP.isEmpty()) {
            return xRealIP;
        }
        
        return request.getRemoteAddr();
    }
}