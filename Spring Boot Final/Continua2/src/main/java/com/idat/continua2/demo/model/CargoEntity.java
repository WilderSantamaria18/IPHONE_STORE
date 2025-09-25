package com.idat.continua2.demo.model;

import jakarta.persistence.*;

@Entity
@Table(name = "cargo")
public class CargoEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ID_Cargo")
    private Integer idCargo;

    @Column(name = "Nombre", length = 50, nullable = false, unique = true)
    private String nombre;

    @Column(name = "Descripcion", length = 200)
    private String descripcion;

    // Constructores, getters y setters
    public CargoEntity() {}

    public CargoEntity(String nombre, String descripcion) {
        this.nombre = nombre;
        this.descripcion = descripcion;
    }

    public Integer getIdCargo() {
        return idCargo;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }
}