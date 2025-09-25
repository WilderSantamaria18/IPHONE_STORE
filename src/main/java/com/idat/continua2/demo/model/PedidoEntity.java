package com.idat.continua2.demo.model;

import jakarta.persistence.*;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

/**
 * Entidad JPA para la tabla PEDIDO
 * Representa las ventas directas del sistema iPhone Store
 * Basado en la estructura SQL proporcionada
 */
@Entity
@Table(name = "pedido")
public class PedidoEntity {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ID_Pedido")
    private Integer idPedido;
    
    @ManyToOne
    @JoinColumn(name = "ID_Cliente", nullable = false)
    private ClienteEntity cliente;
    
    @ManyToOne
    @JoinColumn(name = "ID_Usuario", nullable = false)
    private UsuarioEntity usuario;
    
    @Column(name = "Fecha_Pedido")
    private LocalDateTime fechaPedido;
    
    @Column(name = "Fecha_Entrega")
    private LocalDate fechaEntrega;
    
    @Column(name = "Tipo_Pago", length = 50)
    private String tipoPago;
    
    @Column(name = "Total_Pagar", precision = 10, scale = 2)
    private BigDecimal totalPagar;

    @Enumerated(EnumType.STRING)
    @Column(name = "Estado", nullable = false)
    private EstadoPedido estado = EstadoPedido.Emitido;

    public enum EstadoPedido {
        Emitido, Anulado
    }
    
    // Constructores
    public PedidoEntity() {
        this.fechaPedido = LocalDateTime.now();
        this.totalPagar = BigDecimal.ZERO;
        this.estado = EstadoPedido.Emitido;
    }
    
    public PedidoEntity(ClienteEntity cliente, UsuarioEntity usuario, LocalDate fechaEntrega, 
                       String tipoPago, BigDecimal totalPagar) {
        this();
        this.cliente = cliente;
        this.usuario = usuario;
        this.fechaEntrega = fechaEntrega;
        this.tipoPago = tipoPago;
        this.totalPagar = totalPagar;
    }
    
    // Getters y Setters
    public Integer getIdPedido() {
        return idPedido;
    }
    
    public void setIdPedido(Integer idPedido) {
        this.idPedido = idPedido;
    }
    
    public ClienteEntity getCliente() {
        return cliente;
    }
    
    public void setCliente(ClienteEntity cliente) {
        this.cliente = cliente;
    }
    
    public UsuarioEntity getUsuario() {
        return usuario;
    }
    
    public void setUsuario(UsuarioEntity usuario) {
        this.usuario = usuario;
    }
    
    public LocalDateTime getFechaPedido() {
        return fechaPedido;
    }
    
    public void setFechaPedido(LocalDateTime fechaPedido) {
        this.fechaPedido = fechaPedido;
    }
    
    public LocalDate getFechaEntrega() {
        return fechaEntrega;
    }
    
    public void setFechaEntrega(LocalDate fechaEntrega) {
        this.fechaEntrega = fechaEntrega;
    }
    
    public String getTipoPago() {
        return tipoPago;
    }
    
    public void setTipoPago(String tipoPago) {
        this.tipoPago = tipoPago;
    }
    
    public BigDecimal getTotalPagar() {
        return totalPagar;
    }
    
    public void setTotalPagar(BigDecimal totalPagar) {
        this.totalPagar = totalPagar;
    }
    
        public EstadoPedido getEstado() {
        return estado;
    }

    public void setEstado(EstadoPedido estado) {
        this.estado = estado;
    }

    
    @Override
    public String toString() {
        return "PedidoEntity{" +
                "idPedido=" + idPedido +
                ", cliente=" + (cliente != null ? cliente.getNombre() : null) +
                ", usuario=" + (usuario != null ? usuario.getUsername() : null) +
                ", fechaPedido=" + fechaPedido +
                ", fechaEntrega=" + fechaEntrega +
                ", tipoPago='" + tipoPago + '\'' +
                ", totalPagar=" + totalPagar +
                '}';
    }
}
