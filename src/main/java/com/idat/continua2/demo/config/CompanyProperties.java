package com.idat.continua2.demo.config;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

/**
 * Configuración de propiedades de la empresa IphoneStore
 * Mapea las propiedades del archivo application.properties con prefijo 'app.company'
 * CORREGIDO: Nombres de campos alineados con kebab-case del properties
 */
@Component
@ConfigurationProperties(prefix = "app.company")
public class CompanyProperties {
    
    // CORREGIDO: Nombres que coinciden exactamente con application.properties
    private String adminEmail;  // mapea app.company.admin-email
    private String name;        // mapea app.company.name
    private String ruc;         // mapea app.company.ruc
    private String address;     // mapea app.company.address
    private String phone;       // mapea app.company.phone
    private String website;     // mapea app.company.website
    
    // Constructor por defecto
    public CompanyProperties() {}
    
    // Getters y Setters EXACTOS
    public String getAdminEmail() {
        return adminEmail;
    }
    
    public void setAdminEmail(String adminEmail) {
        this.adminEmail = adminEmail;
    }
    
    public String getName() {
        return name;
    }
    
    public void setName(String name) {
        this.name = name;
    }
    
    public String getRuc() {
        return ruc;
    }
    
    public void setRuc(String ruc) {
        this.ruc = ruc;
    }
    
    public String getAddress() {
        return address;
    }
    
    public void setAddress(String address) {
        this.address = address;
    }
    
    public String getPhone() {
        return phone;
    }
    
    public void setPhone(String phone) {
        this.phone = phone;
    }
    
    public String getWebsite() {
        return website;
    }
    
    public void setWebsite(String website) {
        this.website = website;
    }
    
    @Override
    public String toString() {
        return "CompanyProperties{" +
                "adminEmail='" + adminEmail + '\'' +
                ", name='" + name + '\'' +
                ", ruc='" + ruc + '\'' +
                ", address='" + address + '\'' +
                ", phone='" + phone + '\'' +
                ", website='" + website + '\'' +
                '}';
    }
}