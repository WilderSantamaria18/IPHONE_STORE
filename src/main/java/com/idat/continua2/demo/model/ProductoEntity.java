package com.idat.continua2.demo.model;

import jakarta.persistence.*;
import java.math.BigDecimal;

@Entity
@Table(name = "producto")
public class ProductoEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ID_Producto")
    private Integer idProducto;
    
    @ManyToOne
    @JoinColumn(name = "ID_Proveedor", nullable = false)
    private ProveedorEntity proveedor;
    
    @Column(name = "Modelo", length = 100, nullable = false)
    private String modelo;
    
    @Column(name = "Lanzamiento", length = 4, nullable = false)
    private String lanzamiento;
    
    @Column(name = "Procesador", length = 50, nullable = false)
    private String procesador;
    
    @Column(name = "Ram", length = 20, nullable = false)
    private String ram;
    
    @Column(name = "Almacenamiento", length = 20, nullable = false)
    private String almacenamiento;
    
    @Column(name = "Precio_Venta", precision = 10, scale = 2, nullable = false)
    private BigDecimal precioVenta;
    
    @Column(name = "Precio_Costo", precision = 10, scale = 2, nullable = false)
    private BigDecimal precioCosto;
    
    @Column(name = "Stock", nullable = false)
    private Integer stock;
    
    @Column(name = "Imagen", length = 255)
    private String imagen;

    @Enumerated(EnumType.STRING)
    @Column(name = "Estado")
    private EstadoProducto estado = EstadoProducto.Activo;

    public enum EstadoProducto {
        Activo, Inactivo
    }

    // Constructores, getters y setters
    public ProductoEntity() {}
    
    // Getters y Setters
    public Integer getIdProducto() { 
        return idProducto; 
    }
    public void setIdProducto(Integer idProducto) { 
        this.idProducto = idProducto; 
    }
    public ProveedorEntity getProveedor() { 
        return proveedor; 
    }
    public void setProveedor(ProveedorEntity proveedor) { 
        this.proveedor = proveedor; 
    }
    public String getModelo() { 
        return modelo; 
    }
    public void setModelo(String modelo) { 
        this.modelo = modelo; 
    }
    public String getLanzamiento() { 
        return lanzamiento; 
    }
    public void setLanzamiento(String lanzamiento) { 
        this.lanzamiento = lanzamiento; 
    }
    public String getProcesador() { 
        return procesador; 
    }
    public void setProcesador(String procesador) { 
        this.procesador = procesador; 
    }
    public String getRam() { 
        return ram; 
    }
    public void setRam(String ram) { 
        this.ram = ram; 
    }
    public String getAlmacenamiento() { 
        return almacenamiento; 
    }
    public void setAlmacenamiento(String almacenamiento) { 
        this.almacenamiento = almacenamiento; 
    }
    public BigDecimal getPrecioVenta() { 
        return precioVenta; 
    }
    public void setPrecioVenta(BigDecimal precioVenta) { 
        this.precioVenta = precioVenta; 
    }
    public BigDecimal getPrecioCosto() { 
        return precioCosto; 
    }
    public void setPrecioCosto(BigDecimal precioCosto) { 
        this.precioCosto = precioCosto; 
    }
    public Integer getStock() { 
        return stock; 
    }
    public void setStock(Integer stock) { 
        this.stock = stock; 
    }
    public String getImagen() { 
        return imagen; 
    }
    public void setImagen(String imagen) { 
        this.imagen = imagen; 
    }
    public EstadoProducto getEstado() { 
        return estado; 
    }
    public void setEstado(EstadoProducto estado) { 
        this.estado = estado; 
    }
}