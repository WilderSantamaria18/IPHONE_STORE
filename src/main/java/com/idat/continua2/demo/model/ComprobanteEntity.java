package com.idat.continua2.demo.model;

import jakarta.persistence.*;
import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * Entidad JPA para la tabla COMPROBANTE
 * Representa los comprobantes de venta del sistema iPhone Store
 * Basado en la estructura SQL proporcionada
 */
@Entity
@Table(name = "comprobante")
public class ComprobanteEntity {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ID_Comprobante")
    private Integer idComprobante;
    
    @OneToOne
    @JoinColumn(name = "ID_Pedido", nullable = false, unique = true)
    private PedidoEntity pedido;
    
    @ManyToOne
    @JoinColumn(name = "ID_Cliente", nullable = false)
    private ClienteEntity cliente;
    
    @ManyToOne
    @JoinColumn(name = "ID_Usuario", nullable = false)
    private UsuarioEntity usuario;
    
    @Column(name = "Tipo_Pago", nullable = false, length = 50)
    private String tipoPago;
    
    @Column(name = "Tipo_Comprobante", nullable = false, length = 50)
    private String tipoComprobante;
    
    @Column(name = "Numero_Comprobante", nullable = false, unique = true, length = 50)
    private String numeroComprobante;
    
    @Column(name = "Subtotal", nullable = false, precision = 10, scale = 2)
    private BigDecimal subtotal;
    
    @Column(name = "IGV", nullable = false, precision = 10, scale = 2)
    private BigDecimal igv;
    
    @Column(name = "Total_Pagar", nullable = false, precision = 10, scale = 2)
    private BigDecimal totalPagar;
    
    @Column(name = "Fecha_Emision")
    private LocalDateTime fechaEmision;
    
    @Enumerated(EnumType.STRING)
    @Column(name = "Estado", nullable = false)
    private EstadoComprobante estado = EstadoComprobante.Emitido;

    public enum EstadoComprobante {
        Emitido, Anulado
    }


    // Constructores
    public ComprobanteEntity() {
        this.fechaEmision = LocalDateTime.now();
        this.subtotal = BigDecimal.ZERO;
        this.igv = BigDecimal.ZERO;
        this.totalPagar = BigDecimal.ZERO;
        this.estado = EstadoComprobante.Emitido;
    }
    
    public ComprobanteEntity(PedidoEntity pedido, ClienteEntity cliente, UsuarioEntity usuario,
                           String tipoPago, String tipoComprobante, String numeroComprobante,
                           BigDecimal subtotal, BigDecimal igv, BigDecimal totalPagar) {
        this();
        this.pedido = pedido;
        this.cliente = cliente;
        this.usuario = usuario;
        this.tipoPago = tipoPago;
        this.tipoComprobante = tipoComprobante;
        this.numeroComprobante = numeroComprobante;
        this.subtotal = subtotal;
        this.igv = igv;
        this.totalPagar = totalPagar;
    }
    
    // Getters y Setters
    public Integer getIdComprobante() {
        return idComprobante;
    }
    
    public void setIdComprobante(Integer idComprobante) {
        this.idComprobante = idComprobante;
    }
    
    public PedidoEntity getPedido() {
        return pedido;
    }
    
    public void setPedido(PedidoEntity pedido) {
        this.pedido = pedido;
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
    
    public String getTipoPago() {
        return tipoPago;
    }
    
    public void setTipoPago(String tipoPago) {
        this.tipoPago = tipoPago;
    }
    
    public String getTipoComprobante() {
        return tipoComprobante;
    }
    
    public void setTipoComprobante(String tipoComprobante) {
        this.tipoComprobante = tipoComprobante;
    }
    
    public String getNumeroComprobante() {
        return numeroComprobante;
    }
    
    public void setNumeroComprobante(String numeroComprobante) {
        this.numeroComprobante = numeroComprobante;
    }
    
    public BigDecimal getSubtotal() {
        return subtotal;
    }
    
    public void setSubtotal(BigDecimal subtotal) {
        this.subtotal = subtotal;
    }
    
    public BigDecimal getIgv() {
        return igv;
    }
    
    public void setIgv(BigDecimal igv) {
        this.igv = igv;
    }
    
    public BigDecimal getTotalPagar() {
        return totalPagar;
    }
    
    public void setTotalPagar(BigDecimal totalPagar) {
        this.totalPagar = totalPagar;
    }
    
    public LocalDateTime getFechaEmision() {
        return fechaEmision;
    }
    
    public void setFechaEmision(LocalDateTime fechaEmision) {
        this.fechaEmision = fechaEmision;
    }
    
    // Métodos de utilidad
    
    /**
     * Calcula el IGV basado en el subtotal (18%)
     */
    public void calcularIgv() {
        if (this.subtotal != null) {
            this.igv = this.subtotal.multiply(new BigDecimal("0.18"));
        }
    }
    
    /**
     * Calcula el total basado en subtotal + IGV
     */
    public void calcularTotal() {
        if (this.subtotal != null && this.igv != null) {
            this.totalPagar = this.subtotal.add(this.igv);
        }
    }
    
    /**
     * Calcula todos los montos automáticamente
     * @param montoBase Monto base sin IGV
     */
    public void calcularMontos(BigDecimal montoBase) {
        this.subtotal = montoBase;
        calcularIgv();
        calcularTotal();
    }

    public EstadoComprobante getEstado() {
        return estado;
    }

    public void setEstado(EstadoComprobante estado) {
        this.estado = estado;
    }
    
    @Override
    public String toString() {
        return "ComprobanteEntity{" +
                "idComprobante=" + idComprobante +
                ", pedido=" + (pedido != null ? pedido.getIdPedido() : null) +
                ", cliente=" + (cliente != null ? cliente.getNombre() : null) +
                ", usuario=" + (usuario != null ? usuario.getUsername() : null) +
                ", tipoPago='" + tipoPago + '\'' +
                ", tipoComprobante='" + tipoComprobante + '\'' +
                ", numeroComprobante='" + numeroComprobante + '\'' +
                ", subtotal=" + subtotal +
                ", igv=" + igv +
                ", totalPagar=" + totalPagar +
                ", fechaEmision=" + fechaEmision +
                '}';
    }
}
