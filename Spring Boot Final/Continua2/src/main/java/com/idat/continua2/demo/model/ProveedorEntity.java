package com.idat.continua2.demo.model;

import jakarta.persistence.*;
import java.util.List;

@Entity
@Table(name = "proveedor")
public class ProveedorEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ID_Proveedor")
    private Integer idProveedor;

    @Column(name = "Nombre", length = 100, nullable = false)
    private String nombre;

    @Column(name = "ruc", length = 11, nullable = false, unique = true)
    private String ruc;

    @Column(name = "telefono", length = 15, nullable = false)
    private String telefono;

    @Column(name = "Email", length = 100, nullable = false)
    private String email;

    @Enumerated(EnumType.STRING)
    @Column(name = "Estado")
    private EstadoProveedor estado = EstadoProveedor.Activo;

    public enum EstadoProveedor {
        Activo, Inactivo
    }

    @OneToMany(mappedBy = "proveedor", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<ProductoEntity> productos;

    // Constructores, getters y setters
    public ProveedorEntity() {}

    public Integer getIdProveedor() {
        return idProveedor;
    }

    public void setIdProveedor(Integer idProveedor) {
        this.idProveedor = idProveedor;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getRuc() {
        return ruc;
    }

    public void setRuc(String ruc) {
        this.ruc = ruc;
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

    public EstadoProveedor getEstado() {
        return estado;
    }

    public void setEstado(EstadoProveedor estado) {
        this.estado = estado;
    }

    public List<ProductoEntity> getProductos() {
        return productos;
    }

    public void setProductos(List<ProductoEntity> productos) {
        this.productos = productos;
    }
}