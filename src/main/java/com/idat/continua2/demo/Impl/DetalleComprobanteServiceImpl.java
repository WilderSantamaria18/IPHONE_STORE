package com.idat.continua2.demo.Impl;

import com.idat.continua2.demo.Repository.DetalleComprobanteRepository;
import com.idat.continua2.demo.Repository.ProductoRepository;
import com.idat.continua2.demo.Service.DetalleComprobanteService;
import com.idat.continua2.demo.model.DetalleComprobanteEntity;
import com.idat.continua2.demo.model.ProductoEntity;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

/**
 * Implementación del servicio para gestión de Detalles de Comprobante
 * Proporciona operaciones CRUD y lógica de negocio
 * Integra con DetalleComprobanteRepository para persistencia
 * ✅ OPTIMIZADO: Métodos con JOIN FETCH para eliminar N+1 queries
 */
@Service
@Transactional
public class DetalleComprobanteServiceImpl implements DetalleComprobanteService {

    @Autowired
    private DetalleComprobanteRepository detalleComprobanteRepository;
    
    @Autowired
    private ProductoRepository productoRepository;

    @Override
    @Transactional(readOnly = true)
    public List<DetalleComprobanteEntity> findAllWithRelations() {
        return detalleComprobanteRepository.findAllWithRelations();
    }

    @Override
    @Transactional(readOnly = true)
    public List<DetalleComprobanteEntity> findAll() {
        return detalleComprobanteRepository.findAll();
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<DetalleComprobanteEntity> findById(Integer id) {
        return detalleComprobanteRepository.findById(id);
    }

    @Override
    public DetalleComprobanteEntity add(DetalleComprobanteEntity detalle) {
        // Validar que la cantidad sea positiva
        if (detalle.getCantidad() <= 0) {
            throw new IllegalArgumentException("La cantidad debe ser mayor a cero");
        }
        
        // Validar que el precio unitario sea positivo
        if (detalle.getPrecioUnitario().compareTo(BigDecimal.ZERO) <= 0) {
            throw new IllegalArgumentException("El precio unitario debe ser mayor a cero");
        }
        
        return detalleComprobanteRepository.save(detalle);
    }

    @Override
    public DetalleComprobanteEntity update(DetalleComprobanteEntity detalle) {
        // Validaciones similares al add
        if (detalle.getCantidad() <= 0) {
            throw new IllegalArgumentException("La cantidad debe ser mayor a cero");
        }
        
        if (detalle.getPrecioUnitario().compareTo(BigDecimal.ZERO) <= 0) {
            throw new IllegalArgumentException("El precio unitario debe ser mayor a cero");
        }
        
        return detalleComprobanteRepository.save(detalle);
    }

    @Override
    public void delete(Integer id) {
        detalleComprobanteRepository.deleteById(id);
    }

    @Override
    @Transactional(readOnly = true)
    public List<DetalleComprobanteEntity> findByComprobanteIdWithRelations(Integer comprobanteId) {
        return detalleComprobanteRepository.findByComprobanteIdWithRelations(comprobanteId);
    }

    @Override
    @Transactional(readOnly = true)
    public List<DetalleComprobanteEntity> findByProductoIdWithRelations(Integer productoId) {
        return detalleComprobanteRepository.findByProductoIdWithRelations(productoId);
    }

    @Override
    @Transactional(readOnly = true)
    public BigDecimal calculateTotalByComprobanteId(Integer comprobanteId) {
        return detalleComprobanteRepository.calculateTotalByComprobanteId(comprobanteId);
    }

    @Override
    @Transactional(readOnly = true)
    public Long countByComprobanteId(Integer comprobanteId) {
        return detalleComprobanteRepository.countByComprobanteId(comprobanteId);
    }

    @Override
    @Transactional(readOnly = true)
    public Integer getTotalCantidadVendidaByProducto(Integer productoId) {
        return detalleComprobanteRepository.getTotalCantidadVendidaByProducto(productoId);
    }

    @Override
    @Transactional(readOnly = true)
    public BigDecimal getTotalIngresosByProducto(Integer productoId) {
        return detalleComprobanteRepository.getTotalIngresosByProducto(productoId);
    }

    @Override
    @Transactional(readOnly = true)
    public List<Object[]> findTopProductosMasVendidos(Integer limit) {
        return detalleComprobanteRepository.findTopProductosMasVendidos(limit);
    }

    @Override
    @Transactional(readOnly = true)
    public List<Object[]> findTopProductosMayorIngreso(Integer limit) {
        return detalleComprobanteRepository.findTopProductosMayorIngreso(limit);
    }

    @Override
    public void deleteByComprobanteId(Integer comprobanteId) {
        detalleComprobanteRepository.deleteByComprobanteId(comprobanteId);
    }

    @Override
    @Transactional(readOnly = true)
    public boolean existsByComprobanteId(Integer comprobanteId) {
        return detalleComprobanteRepository.existsByComprobanteId(comprobanteId);
    }

    @Override
    public List<DetalleComprobanteEntity> addAll(List<DetalleComprobanteEntity> detalles) {
        // Validar cada detalle antes de guardar
        for (DetalleComprobanteEntity detalle : detalles) {
            if (detalle.getCantidad() <= 0) {
                throw new IllegalArgumentException("Todos los detalles deben tener cantidad mayor a cero");
            }
            
            if (detalle.getPrecioUnitario().compareTo(BigDecimal.ZERO) <= 0) {
                throw new IllegalArgumentException("Todos los detalles deben tener precio unitario mayor a cero");
            }
        }
        
        return detalleComprobanteRepository.saveAll(detalles);
    }

    @Override
    public void actualizarStockProductos(Integer comprobanteId) {
        // Obtener todos los detalles del comprobante
        List<DetalleComprobanteEntity> detalles = findByComprobanteIdWithRelations(comprobanteId);
        
        // Actualizar stock de cada producto
        for (DetalleComprobanteEntity detalle : detalles) {
            ProductoEntity producto = detalle.getProducto();
            
            // Reducir stock
            Integer nuevoStock = producto.getStock() - detalle.getCantidad();
            
            if (nuevoStock < 0) {
                throw new IllegalStateException("Stock insuficiente para el producto: " + producto.getModelo());
            }
            
            producto.setStock(nuevoStock);
            productoRepository.save(producto);
        }
    }
}
