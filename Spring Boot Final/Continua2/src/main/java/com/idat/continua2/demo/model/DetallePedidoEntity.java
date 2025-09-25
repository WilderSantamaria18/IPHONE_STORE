package com.idat.continua2.demo.model;

import jakarta.persistence.*;
import java.math.BigDecimal;

/**
 * Entidad JPA para la tabla DETALLE_PEDIDO
 * Representa los detalles/items de cada pedido en el sistema iPhone Store
 * Basado en la estructura SQL proporcionada con columna Subtotal GENERATED
 */
@Entity
@Table(name = "detalle_pedido")
public class DetallePedidoEntity {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ID_Detalle")
    private Integer idDetalle;
    
    @ManyToOne
    @JoinColumn(name = "ID_Pedido", nullable = false)
    private PedidoEntity pedido;
    
    @ManyToOne
    @JoinColumn(name = "ID_Producto", nullable = false)
    private ProductoEntity producto;
    
    @Column(name = "Cantidad", nullable = false)
    private Integer cantidad;
    
    @Column(name = "Precio_Unitario", precision = 10, scale = 2, nullable = false)
    private BigDecimal precioUnitario;
    
    @Column(name = "Subtotal", precision = 10, scale = 2, insertable = false, updatable = false)
    private BigDecimal subtotal;
    
    // Constructores
    public DetallePedidoEntity() {}
    
    public DetallePedidoEntity(PedidoEntity pedido, ProductoEntity producto, 
                              Integer cantidad, BigDecimal precioUnitario) {
        this.pedido = pedido;
        this.producto = producto;
        this.cantidad = cantidad;
        this.precioUnitario = precioUnitario;
    }
    
    // Getters y Setters
    public Integer getIdDetalle() {
        return idDetalle;
    }
    
    public void setIdDetalle(Integer idDetalle) {
        this.idDetalle = idDetalle;
    }
    
    public PedidoEntity getPedido() {
        return pedido;
    }
    
    public void setPedido(PedidoEntity pedido) {
        this.pedido = pedido;
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
    
    /**
     * Calcula el subtotal manualmente (cantidad * precio unitario)
     * Útil para cálculos antes de persistir en BD
     */
    public BigDecimal calcularSubtotal() {
        if (cantidad != null && precioUnitario != null) {
            return precioUnitario.multiply(BigDecimal.valueOf(cantidad));
        }
        return BigDecimal.ZERO;
    }
    
    @Override
    public String toString() {
        return "DetallePedidoEntity{" +
                "idDetalle=" + idDetalle +
                ", pedido=" + (pedido != null ? pedido.getIdPedido() : null) +
                ", producto=" + (producto != null ? producto.getModelo() : null) +
                ", cantidad=" + cantidad +
                ", precioUnitario=" + precioUnitario +
                ", subtotal=" + subtotal +
                '}';
    }
}
