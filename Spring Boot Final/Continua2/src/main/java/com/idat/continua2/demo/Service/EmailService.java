package com.idat.continua2.demo.Service;

// TODO: Implementar servicio de email cuando se requiera
// import com.idat.continua2.demo.model.ComprobanteEntity;

/**
 * Servicio para envío de emails con comprobantes
 * Integra con Spring Mail para notificaciones automáticas
 * TEMPORALMENTE DESHABILITADO - Implementar cuando se requiera
 */
public interface EmailService {
    
    // TODO: Implementar métodos cuando se tengan las entidades de comprobante
    /*
    /**
     * Envía comprobante por email al cliente
     * @param comprobante Comprobante a enviar
     * @return true si se envió correctamente
     */
    // boolean enviarComprobanteCliente(ComprobanteEntity comprobante);
    
    /*
    /**
     * Envía notificación de venta a administración
     * @param comprobante Comprobante generado
     * @return true si se envió correctamente
     */
    // boolean notificarVentaAdministracion(ComprobanteEntity comprobante);
    
    /**
     * Envía email con HTML personalizado
     * @param destinatario Email destino
     * @param asunto Asunto del email
     * @param contenidoHtml Contenido en HTML
     * @return true si se envió correctamente
     */
    boolean enviarEmailHtml(String destinatario, String asunto, String contenidoHtml);
}