package com.idat.continua2.demo.model;

import jakarta.persistence.*;
import java.util.List;

@Entity
@Table(name = "cliente")
public class ClienteEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ID_Cliente")
    private Integer idCliente;

    @Column(name = "Tipo_Cliente", length = 50, nullable = false)
    private String tipoCliente;

    @Column(name = "Nombre", length = 100, nullable = false)
    private String nombre;

    @Column(name = "documento", length = 11, unique = true)
    private String documento;

    @Column(name = "Direccion", length = 200, nullable = false)
    private String direccion;

    @Column(name = "Telefono", length = 9, nullable = false)
    private String telefono;

    @Column(name = "Email", length = 100, nullable = false)
    private String email;

    @Enumerated(EnumType.STRING)
    @Column(name = "Estado")
    private EstadoCliente estado = EstadoCliente.Activo;

    public enum EstadoCliente {
        Activo, Inactivo
    }

    @OneToMany(mappedBy = "cliente", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<PedidoEntity> pedidos;

    // TODO: Implementar comprobantes cuando se requiera
    // @OneToMany(mappedBy = "cliente", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    // private List<ComprobanteEntity> comprobantes;

    // Constructores, getters y setters
    public ClienteEntity() {}

    public Integer getIdCliente() {
        return idCliente;
    }

    public void setIdCliente(Integer idCliente) {
        this.idCliente = idCliente;
    }

    public String getTipoCliente() {
        return tipoCliente;
    }

    public void setTipoCliente(String tipoCliente) {
        this.tipoCliente = tipoCliente;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getDocumento() {
        return documento;
    }

    public void setDocumento(String documento) {
        this.documento = documento;
    }

    public String getDireccion() {
        return direccion;
    }

    public void setDireccion(String direccion) {
        this.direccion = direccion;
    }

    public String getTelefono() {
        return telefono;
    }

    public void setTelefono(String telefono) {
        this.telefono = telefono;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public EstadoCliente getEstado() {
        return estado;
    }

    public void setEstado(EstadoCliente estado) {
        this.estado = estado;
    }

    public List<PedidoEntity> getPedidos() {
        return pedidos;
    }

    public void setPedidos(List<PedidoEntity> pedidos) {
        this.pedidos = pedidos;
    }

    // TODO: Implementar comprobantes cuando se requiera
    // public List<ComprobanteEntity> getComprobantes() {
    //     return comprobantes;
    // }

    // public void setComprobantes(List<ComprobanteEntity> comprobantes) {
    //     this.comprobantes = comprobantes;
    // }
}