package com.idat.continua2.demo.model;

import jakarta.persistence.*;

@Entity
@Table(name = "usuario")
public class UsuarioEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ID_Usuario")
    private Integer idUsuario;

    @ManyToOne
    @JoinColumn(name = "ID_Cargo", nullable = false)
    private CargoEntity cargo;

    @Column(name = "Nombres", length = 100, nullable = false)
    private String nombres;

    @Column(name = "dni", length = 8, nullable = false, unique = true)
    private String dni;

    @Column(name = "Email", length = 100, nullable = false, unique = true)
    private String email;

    @Column(name = "Username", length = 50, nullable = false, unique = true)
    private String username;

    @Column(name = "Contrasena", length = 100, nullable = false)
    private String contrasena;

    @Enumerated(EnumType.STRING)
    @Column(name = "Estado")
    private EstadoUsuario estado = EstadoUsuario.Activo;

    public enum EstadoUsuario {
        Activo, Inactivo
    }

    // Constructores, getters y setters
    public UsuarioEntity() {}

    public Integer getIdUsuario() {
        return idUsuario;
    }

    public CargoEntity getCargo() {
        return cargo;
    }

    public void setCargo(CargoEntity cargo) {
        this.cargo = cargo;
    }

    public String getNombres() {
        return nombres;
    }

    public void setNombres(String nombres) {
        this.nombres = nombres;
    }

    public String getDni() {
        return dni;
    }

    public void setDni(String dni) {
        this.dni = dni;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getContrasena() {
        return contrasena;
    }

    public void setContrasena(String contrasena) {
        this.contrasena = contrasena;
    }

    public EstadoUsuario getEstado() {
        return estado;
    }

    public void setEstado(EstadoUsuario estado) {
        this.estado = estado;
    }
}