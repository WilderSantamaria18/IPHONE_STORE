package com.idat.continua2.demo.config;

import org.springframework.security.crypto.password.PasswordEncoder;

/**
 * ⚠️ ENCODER TEMPORAL PARA TESTING - NO USAR EN PRODUCCIÓN ⚠️
 * 
 * Este encoder NO cifra las contraseñas y permite texto plano.
 * Solo debe usarse para pruebas de desarrollo local.
 * 
 * Para producción usar BCryptPasswordEncoder configurado en SecurityConfig.
 * 
 * @deprecated Solo para desarrollo - usar BCryptPasswordEncoder en producción
 */
@Deprecated
public class NoOpPasswordEncoder implements PasswordEncoder {
    
    /**
     * ⚠️ NO CIFRA LA CONTRASEÑA - Solo para testing
     * @param rawPassword contraseña en texto plano
     * @return la misma contraseña sin cifrar
     */
    @Override
    public String encode(CharSequence rawPassword) {
        // ⚠️ PELIGRO: No cifra la contraseña
        return rawPassword.toString();
    }
    
    /**
     * ⚠️ Comparación en texto plano - Solo para testing
     * @param rawPassword contraseña ingresada
     * @param encodedPassword contraseña almacenada
     * @return true si son iguales
     */
    @Override
    public boolean matches(CharSequence rawPassword, String encodedPassword) {
        // ⚠️ PELIGRO: Comparación en texto plano
        return rawPassword.toString().equals(encodedPassword);
    }
    
    /**
     * Crea una instancia del encoder sin cifrado
     * ⚠️ Solo para desarrollo
     * @return NoOpPasswordEncoder instance
     */
    public static NoOpPasswordEncoder getInstance() {
        return new NoOpPasswordEncoder();
    }
}
