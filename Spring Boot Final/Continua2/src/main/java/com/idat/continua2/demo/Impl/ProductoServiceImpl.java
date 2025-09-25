package com.idat.continua2.demo.Impl;

import com.idat.continua2.demo.Service.ProductoService;
import com.idat.continua2.demo.Repository.ProductoRepository;
import com.idat.continua2.demo.model.ProductoEntity;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

/**
 * Implementación del servicio para gestión de Productos
 * Proporciona operaciones CRUD y lógica de negocio específica
 * Incluye gestión de stock y validaciones
 */
@Service
@Transactional
public class ProductoServiceImpl implements ProductoService {

    @Autowired
    private ProductoRepository productoRepository;

    @Override
    @Transactional(readOnly = true)
    public List<ProductoEntity> findAll() {
        return productoRepository.findAll();
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<ProductoEntity> findById(Integer id) {
        return productoRepository.findById(id);
    }

    @Override
    public ProductoEntity add(ProductoEntity producto) {
        // Establecer estado activo por defecto
        if (producto.getEstado() == null) {
            producto.setEstado(ProductoEntity.EstadoProducto.Activo);
        }
        return productoRepository.save(producto);
    }

    @Override
    public ProductoEntity update(ProductoEntity producto) {
        return productoRepository.save(producto);
    }

    @Override
    public ProductoEntity delete(ProductoEntity producto) {
        // Soft delete - cambiar estado a Inactivo
        producto.setEstado(ProductoEntity.EstadoProducto.Inactivo);
        return productoRepository.save(producto);
    }

    @Override
    public void cambiarEstado(Integer id, ProductoEntity.EstadoProducto estado) {
        Optional<ProductoEntity> productoOpt = productoRepository.findById(id);
        if (productoOpt.isPresent()) {
            ProductoEntity producto = productoOpt.get();
            producto.setEstado(estado);
            productoRepository.save(producto);
        }
    }

    @Override
    @Transactional(readOnly = true)
    public List<ProductoEntity> findByEstado(ProductoEntity.EstadoProducto estado) {
        return productoRepository.findByEstado(estado);
    }

    @Override
    @Transactional(readOnly = true)
    public List<ProductoEntity> findByNombre(String nombre) {
        // Usar findByModeloContainingIgnoreCase como alternativa
        return productoRepository.findByModeloContainingIgnoreCase(nombre);
    }

    @Override
    @Transactional(readOnly = true)
    public List<ProductoEntity> findByModelo(String modelo) {
        return productoRepository.findByModeloContainingIgnoreCase(modelo);
    }

    @Override
    @Transactional(readOnly = true)
    public List<ProductoEntity> findByPrecioBetween(BigDecimal precioMin, BigDecimal precioMax) {
        return productoRepository.findByPrecioVentaBetween(precioMin, precioMax);
    }

    @Override
    @Transactional(readOnly = true)
    public List<ProductoEntity> findWithStock() {
        return productoRepository.findProductosActivos();
    }

    @Override
    public void actualizarStock(Integer id, Integer cantidad) {
        Optional<ProductoEntity> productoOpt = productoRepository.findById(id);
        if (productoOpt.isPresent()) {
            ProductoEntity producto = productoOpt.get();
            producto.setStock(cantidad);
            productoRepository.save(producto);
        }
    }

    @Override
    public void reducirStock(Integer id, Integer cantidad) {
        Optional<ProductoEntity> productoOpt = productoRepository.findById(id);
        if (productoOpt.isPresent()) {
            ProductoEntity producto = productoOpt.get();
            if (producto.getStock() >= cantidad) {
                producto.setStock(producto.getStock() - cantidad);
                productoRepository.save(producto);
            } else {
                throw new IllegalArgumentException(
                    "Stock insuficiente. Disponible: " + producto.getStock() + 
                    ", Solicitado: " + cantidad
                );
            }
        }
    }

    @Override
    public void aumentarStock(Integer id, Integer cantidad) {
        if (cantidad == null || cantidad <= 0) {
            throw new IllegalArgumentException("La cantidad a aumentar debe ser > 0");
        }
        Optional<ProductoEntity> productoOpt = productoRepository.findById(id);
        if (productoOpt.isPresent()) {
            ProductoEntity producto = productoOpt.get();
            producto.setStock(producto.getStock() + cantidad);
            productoRepository.save(producto);
        } else {
            throw new IllegalArgumentException("Producto no encontrado id=" + id);
        }
    }

    @Override
    @Transactional(readOnly = true)
    public List<ProductoEntity> findProductosStockBajo() {
        return productoRepository.findProductosConStockBajo(10);
    }

    @Override
    @Transactional(readOnly = true)
    public Long countProductosActivos() {
        return productoRepository.countProductosActivos();
    }
}
