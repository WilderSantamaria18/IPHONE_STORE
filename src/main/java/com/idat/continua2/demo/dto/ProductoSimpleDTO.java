package com.idat.continua2.demo.dto;

import java.math.BigDecimal;

/**
 * DTO para respuestas de producto simplificadas
 * Útil para listados y selecciones en formularios
 */
public class ProductoSimpleDTO {
    
    private Integer idProducto;
    private String nombre;
    private String modelo;
    private BigDecimal precioVenta;
    private Integer stock;
    private String nombreProveedor;
    private boolean disponible;
    
    public ProductoSimpleDTO() {}
    
    public ProductoSimpleDTO(Integer idProducto, String nombre, String modelo, 
                           BigDecimal precioVenta, Integer stock) {
        this.idProducto = idProducto;
        this.nombre = nombre;
        this.modelo = modelo;
        this.precioVenta = precioVenta;
        this.stock = stock;
        this.disponible = stock != null && stock > 0;
    }
    
    // Getters and Setters
    public Integer getIdProducto() {
        return idProducto;
    }
    
    public void setIdProducto(Integer idProducto) {
        this.idProducto = idProducto;
    }
    
    public String getNombre() {
        return nombre;
    }
    
    public void setNombre(String nombre) {
        this.nombre = nombre;
    }
    
    public String getModelo() {
        return modelo;
    }
    
    public void setModelo(String modelo) {
        this.modelo = modelo;
    }
    
    public BigDecimal getPrecioVenta() {
        return precioVenta;
    }
    
    public void setPrecioVenta(BigDecimal precioVenta) {
        this.precioVenta = precioVenta;
    }
    
    public Integer getStock() {
        return stock;
    }
    
    public void setStock(Integer stock) {
        this.stock = stock;
        this.disponible = stock != null && stock > 0;
    }
    
    public String getNombreProveedor() {
        return nombreProveedor;
    }
    
    public void setNombreProveedor(String nombreProveedor) {
        this.nombreProveedor = nombreProveedor;
    }
    
    public boolean isDisponible() {
        return disponible;
    }
    
    public void setDisponible(boolean disponible) {
        this.disponible = disponible;
    }
    
    // Métodos de utilidad
    public String getNombreCompleto() {
        return nombre + " " + modelo;
    }
    
    public String getEstadoStock() {
        if (stock == null || stock == 0) {
            return "Sin stock";
        } else if (stock < 10) {
            return "Stock bajo (" + stock + ")";
        } else {
            return "Disponible (" + stock + ")";
        }
    }
    
    public String getClaseEstadoStock() {
        if (stock == null || stock == 0) {
            return "danger";
        } else if (stock < 10) {
            return "warning";
        } else {
            return "success";
        }
    }
    
    @Override
    public String toString() {
        return "ProductoSimpleDTO{" +
                "idProducto=" + idProducto +
                ", nombre='" + nombre + '\'' +
                ", modelo='" + modelo + '\'' +
                ", precioVenta=" + precioVenta +
                ", stock=" + stock +
                ", disponible=" + disponible +
                '}';
    }
}
