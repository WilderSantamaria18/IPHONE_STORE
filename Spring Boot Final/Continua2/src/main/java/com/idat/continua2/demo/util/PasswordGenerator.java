package com.idat.continua2.demo.util;

import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import java.security.SecureRandom;

/**
 * Clase utilitaria para generar y manejar contraseñas
 * Proporciona funciones para encriptar contraseñas y generar passwords aleatorios
 * Utiliza BCrypt para el cifrado seguro
 */
public class PasswordGenerator {
    
    private static final BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();
    private static final String CHARACTERS = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789@#$%^&*";
    private static final SecureRandom random = new SecureRandom();
    
    /**
     * Genera un hash BCrypt para la contraseña proporcionada
     * @param password - Contraseña en texto plano
     * @return Hash BCrypt de la contraseña
     */
    public static String encryptPassword(String password) {
        return encoder.encode(password);
    }
    
    /**
     * Verifica si una contraseña coincide con un hash
     * @param password - Contraseña en texto plano
     * @param hashedPassword - Hash almacenado
     * @return true si coinciden
     */
    public static boolean verifyPassword(String password, String hashedPassword) {
        return encoder.matches(password, hashedPassword);
    }
    
    /**
     * Genera una contraseña aleatoria segura
     * @param length - Longitud de la contraseña
     * @return Contraseña aleatoria
     */
    public static String generateRandomPassword(int length) {
        if (length < 8) {
            throw new IllegalArgumentException("La contraseña debe tener al menos 8 caracteres");
        }
        
        StringBuilder password = new StringBuilder();
        for (int i = 0; i < length; i++) {
            password.append(CHARACTERS.charAt(random.nextInt(CHARACTERS.length())));
        }
        return password.toString();
    }
    
    /**
     * Genera una contraseña temporal de 12 caracteres
     * @return Contraseña temporal
     */
    public static String generateTemporaryPassword() {
        return generateRandomPassword(12);
    }
    
    /**
     * Valida si una contraseña cumple con los criterios de seguridad
     * @param password - Contraseña a validar
     * @return true si es válida
     */
    public static boolean isValidPassword(String password) {
        if (password == null || password.length() < 8) {
            return false;
        }
        
        boolean hasUpper = password.chars().anyMatch(Character::isUpperCase);
        boolean hasLower = password.chars().anyMatch(Character::isLowerCase);
        boolean hasDigit = password.chars().anyMatch(Character::isDigit);
        boolean hasSpecial = password.chars().anyMatch(ch -> "!@#$%^&*()_+-=[]{}|;:,.<>?".indexOf(ch) >= 0);
        
        return hasUpper && hasLower && hasDigit && hasSpecial;
    }
    
    /**
     * Método principal para testing y generación de hashes
     */
    public static void main(String[] args) {
        System.out.println("=== GENERADOR DE CONTRASEÑAS BCrypt ===\n");
        
        // Generar hashes para las contraseñas existentes del sistema
        System.out.println("--- Hashes para contraseñas del sistema ---");
        System.out.println("Contraseña '123': " + encryptPassword("123"));
        System.out.println("Contraseña '456': " + encryptPassword("456"));
        System.out.println("Contraseña '789': " + encryptPassword("789"));
        
        // Contraseñas adicionales para testing
        System.out.println("\n--- Hashes para contraseñas adicionales ---");
        System.out.println("Contraseña 'admin': " + encryptPassword("admin"));
        System.out.println("Contraseña 'password': " + encryptPassword("password"));
        System.out.println("Contraseña 'vendedor123': " + encryptPassword("vendedor123"));
        
        // Generar contraseñas aleatorias
        System.out.println("\n--- Contraseñas aleatorias generadas ---");
        for (int i = 0; i < 3; i++) {
            String randomPass = generateRandomPassword(12);
            System.out.println("Contraseña aleatoria: " + randomPass + " -> Hash: " + encryptPassword(randomPass));
        }
        
        // Ejemplo de validación
        System.out.println("\n--- Validación de contraseñas ---");
        String testPassword = "Admin123@";
        System.out.println("Contraseña '" + testPassword + "' es válida: " + isValidPassword(testPassword));
        System.out.println("Contraseña '123' es válida: " + isValidPassword("123"));
    }
}
