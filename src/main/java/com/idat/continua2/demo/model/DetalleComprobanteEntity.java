package com.idat.continua2.demo.model;

import jakarta.persistence.*;
import java.math.BigDecimal;

/**
 * Entidad JPA para la tabla DETALLE_COMPROBANTE
 * Representa los detalles de productos en cada comprobante
 * Basado en la estructura SQL proporcionada
 */
@Entity
@Table(name = "detalle_comprobante")
public class DetalleComprobanteEntity {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ID_Detalle_Comprobante")
    private Integer idDetalleComprobante;
    
    @ManyToOne
    @JoinColumn(name = "ID_Comprobante", nullable = false)
    private ComprobanteEntity comprobante;
    
    @ManyToOne
    @JoinColumn(name = "ID_Producto", nullable = false)
    private ProductoEntity producto;
    
    @Column(name = "Cantidad", nullable = false)
    private Integer cantidad;
    
    @Column(name = "Precio_Unitario", nullable = false, precision = 10, scale = 2)
    private BigDecimal precioUnitario;
    
    @Column(name = "Subtotal", precision = 10, scale = 2, insertable = false, updatable = false)
    private BigDecimal subtotal;
    
    // Constructores
    public DetalleComprobanteEntity() {
        this.cantidad = 0;
        this.precioUnitario = BigDecimal.ZERO;
    }
    
    public DetalleComprobanteEntity(ComprobanteEntity comprobante, ProductoEntity producto,
                                  Integer cantidad, BigDecimal precioUnitario) {
        this();
        this.comprobante = comprobante;
        this.producto = producto;
        this.cantidad = cantidad;
        this.precioUnitario = precioUnitario;
    }
    
    // Getters y Setters
    public Integer getIdDetalleComprobante() {
        return idDetalleComprobante;
    }
    
    public void setIdDetalleComprobante(Integer idDetalleComprobante) {
        this.idDetalleComprobante = idDetalleComprobante;
    }
    
    public ComprobanteEntity getComprobante() {
        return comprobante;
    }
    
    public void setComprobante(ComprobanteEntity comprobante) {
        this.comprobante = comprobante;
    }
    
    public ProductoEntity getProducto() {
        return producto;
    }
    
    public void setProducto(ProductoEntity producto) {
        this.producto = producto;
    }
    
    public Integer getCantidad() {
        return cantidad;
    }
    
    public void setCantidad(Integer cantidad) {
        this.cantidad = cantidad;
    }
    
    public BigDecimal getPrecioUnitario() {
        return precioUnitario;
    }
    
    public void setPrecioUnitario(BigDecimal precioUnitario) {
        this.precioUnitario = precioUnitario;
    }
    
    public BigDecimal getSubtotal() {
        return subtotal;
    }
    
    public void setSubtotal(BigDecimal subtotal) {
        this.subtotal = subtotal;
    }
    
    // Métodos de utilidad
    
    /**
     * Calcula el subtotal basado en cantidad y precio unitario
     * @return Subtotal calculado
     */
    public BigDecimal calcularSubtotal() {
        if (this.cantidad != null && this.precioUnitario != null) {
            return this.precioUnitario.multiply(new BigDecimal(this.cantidad));
        }
        return BigDecimal.ZERO;
    }
    
    @Override
    public String toString() {
        return "DetalleComprobanteEntity{" +
                "idDetalleComprobante=" + idDetalleComprobante +
                ", comprobante=" + (comprobante != null ? comprobante.getIdComprobante() : null) +
                ", producto=" + (producto != null ? producto.getModelo() : null) +
                ", cantidad=" + cantidad +
                ", precioUnitario=" + precioUnitario +
                ", subtotal=" + subtotal +
                '}';
    }
}
