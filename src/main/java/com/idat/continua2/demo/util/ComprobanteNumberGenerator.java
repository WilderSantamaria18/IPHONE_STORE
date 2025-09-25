package com.idat.continua2.demo.util;

// TODO: Implementar cuando se tengan las entidades de comprobante
// import com.idat.continua2.demo.Repository.ComprobanteRepository;
import org.springframework.stereotype.Component;

/**
 * Generador de números de comprobante siguiendo estándar SUNAT
 * Formato: F001-IS001 (Facturas) | B001-IS001 (Boletas)
 * Patrón: [TIPO][SERIE]-IS[NUMERO_CORRELATIVO]
 * TEMPORALMENTE DESHABILITADO - Implementar cuando se requiera
 */
@Component
public class ComprobanteNumberGenerator {
    
    // TODO: Implementar cuando se tenga ComprobanteRepository
    // @Autowired
    // private ComprobanteRepository comprobanteRepository;
    
    /**
     * Genera el siguiente número de comprobante
     * @param tipoComprobante "FACTURA" o "BOLETA"
     * @return Número correlativo formateado
     * TODO: Implementar cuando se tenga ComprobanteRepository
     */
    public String generarSiguienteNumero(String tipoComprobante) {
        // Implementación temporal
        String prefijo = obtenerPrefijo(tipoComprobante);
        String serie = "001";
        Long siguienteNumero = 1L; // Por ahora retorna siempre 1
        
        return String.format("%s%s-IS%03d", prefijo, serie, siguienteNumero);
    }
    
    /**
     * Obtiene prefijo según tipo de comprobante
     */
    private String obtenerPrefijo(String tipoComprobante) {
        switch (tipoComprobante.toUpperCase()) {
            case "FACTURA": return "F";
            case "BOLETA": return "B";
            default: throw new IllegalArgumentException("Tipo comprobante inválido: " + tipoComprobante);
        }
    }
    
    /**
     * Valida formato de número de comprobante
     */
    public boolean esNumeroValido(String numeroComprobante) {
        return numeroComprobante != null && 
               numeroComprobante.matches("^[FB]001-IS\\d{3}$");
    }
}