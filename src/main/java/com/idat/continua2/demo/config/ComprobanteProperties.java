package com.idat.continua2.demo.config;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

/**
 * Configuración de propiedades para comprobantes
 * Mapea las propiedades del archivo application.properties con prefijo 'app.comprobantes'
 * CORREGIDO: Nombres de campos alineados con kebab-case del properties
 */
@Component
@ConfigurationProperties(prefix = "app.comprobantes")
public class ComprobanteProperties {
    
    // CORREGIDO: Nombres que coinciden exactamente con application.properties
    private String serieFactura;      // mapea app.comprobantes.serie-factura
    private String serieBoleta;       // mapea app.comprobantes.serie-boleta
    private String prefijoNumeracion; // mapea app.comprobantes.prefijo-numeracion
    
    // Constructor por defecto
    public ComprobanteProperties() {}
    
    // Getters y Setters EXACTOS
    public String getSerieFactura() {
        return serieFactura;
    }
    
    public void setSerieFactura(String serieFactura) {
        this.serieFactura = serieFactura;
    }
    
    public String getSerieBoleta() {
        return serieBoleta;
    }
    
    public void setSerieBoleta(String serieBoleta) {
        this.serieBoleta = serieBoleta;
    }
    
    public String getPrefijoNumeracion() {
        return prefijoNumeracion;
    }
    
    public void setPrefijoNumeracion(String prefijoNumeracion) {
        this.prefijoNumeracion = prefijoNumeracion;
    }
    
    // Métodos de utilidad para generar números
    public String generarNumeroFactura(int consecutivo) {
        return serieFactura + "-" + prefijoNumeracion + String.format("%03d", consecutivo);
    }
    
    public String generarNumeroBoleta(int consecutivo) {
        return serieBoleta + "-" + prefijoNumeracion + String.format("%03d", consecutivo);
    }
    
    @Override
    public String toString() {
        return "ComprobanteProperties{" +
                "serieFactura='" + serieFactura + '\'' +
                ", serieBoleta='" + serieBoleta + '\'' +
                ", prefijoNumeracion='" + prefijoNumeracion + '\'' +
                '}';
    }
}