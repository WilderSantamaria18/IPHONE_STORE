package com.idat.continua2.demo.Impl;

import com.idat.continua2.demo.Repository.DetallePedidoRepository;
import com.idat.continua2.demo.Repository.PedidoRepository;
import com.idat.continua2.demo.Repository.ProductoRepository;
import com.idat.continua2.demo.Service.DetallePedidoService;
import com.idat.continua2.demo.model.DetallePedidoEntity;
import com.idat.continua2.demo.model.PedidoEntity;
import com.idat.continua2.demo.model.ProductoEntity;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

/**
 * Implementación del servicio para gestión de DetallePedido
 * Proporciona operaciones CRUD y lógica de negocio
 * Integra con DetallePedidoRepository para persistencia
 * ✅ OPTIMIZADO: Métodos con JOIN FETCH para eliminar N+1 queries
 * 🔗 INTEGRADO: Recálculo automático de totales de pedido
 */
@Service
@Transactional
public class DetallePedidoServiceImpl implements DetallePedidoService {

    @Autowired
    private DetallePedidoRepository detallePedidoRepository;
    
    @Autowired
    private PedidoRepository pedidoRepository;
    
    @Autowired
    private ProductoRepository productoRepository;

    @Override
    @Transactional(readOnly = true)
    public List<DetallePedidoEntity> findAllWithRelations() {
        return detallePedidoRepository.findAllWithRelations();
    }

    @Override
    @Transactional(readOnly = true)
    public List<DetallePedidoEntity> findAll() {
        return detallePedidoRepository.findAll();
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<DetallePedidoEntity> findById(Integer id) {
        return detallePedidoRepository.findById(id);
    }

    @Override
    public DetallePedidoEntity add(DetallePedidoEntity detalle) {
        // Validar que el producto existe y tiene stock
        if (detalle.getProducto() != null && detalle.getCantidad() != null) {
            Optional<ProductoEntity> producto = productoRepository.findById(detalle.getProducto().getIdProducto());
            if (producto.isPresent()) {
                ProductoEntity prod = producto.get();
                if (prod.getStock() < detalle.getCantidad()) {
                    throw new RuntimeException("Stock insuficiente. Stock disponible: " + prod.getStock());
                }
                
                // Establecer precio unitario del producto si no está definido
                if (detalle.getPrecioUnitario() == null) {
                    detalle.setPrecioUnitario(prod.getPrecioVenta());
                }
            }
        }
        
        DetallePedidoEntity detalleGuardado = detallePedidoRepository.save(detalle);
        
        // Recalcular total del pedido automáticamente
        if (detalleGuardado.getPedido() != null) {
            recalcularYActualizarTotalPedido(detalleGuardado.getPedido().getIdPedido());
        }
        
        return detalleGuardado;
    }

    @Override
    public DetallePedidoEntity update(DetallePedidoEntity detalle) {
        DetallePedidoEntity detalleActualizado = detallePedidoRepository.save(detalle);
        
        // Recalcular total del pedido automáticamente
        if (detalleActualizado.getPedido() != null) {
            recalcularYActualizarTotalPedido(detalleActualizado.getPedido().getIdPedido());
        }
        
        return detalleActualizado;
    }

    @Override
    public void delete(Integer id) {
        Optional<DetallePedidoEntity> detalle = detallePedidoRepository.findById(id);
        Integer pedidoId = null;
        
        if (detalle.isPresent()) {
            pedidoId = detalle.get().getPedido().getIdPedido();
        }
        
        detallePedidoRepository.deleteById(id);
        
        // Recalcular total del pedido automáticamente
        if (pedidoId != null) {
            recalcularYActualizarTotalPedido(pedidoId);
        }
    }

    @Override
    @Transactional(readOnly = true)
    public List<DetallePedidoEntity> findByPedidoIdWithRelations(Integer pedidoId) {
        return detallePedidoRepository.findByPedidoIdWithRelations(pedidoId);
    }

    @Override
    @Transactional(readOnly = true)
    public List<DetallePedidoEntity> findByProductoIdWithRelations(Integer productoId) {
        return detallePedidoRepository.findByProductoIdWithRelations(productoId);
    }

    @Override
    @Transactional(readOnly = true)
    public List<DetallePedidoEntity> findByPedidoId(Integer pedidoId) {
        return detallePedidoRepository.findByIdPedidoOrderByIdDetalle(pedidoId);
    }

    @Override
    @Transactional(readOnly = true)
    public List<DetallePedidoEntity> findByProductoId(Integer productoId) {
        return detallePedidoRepository.findByProductoIdProducto(productoId);
    }

    @Override
    @Transactional(readOnly = true)
    public List<DetallePedidoEntity> findByCantidadBetween(Integer cantidadMinima, Integer cantidadMaxima) {
        return detallePedidoRepository.findByCantidadBetween(cantidadMinima, cantidadMaxima);
    }

    @Override
    @Transactional(readOnly = true)
    public List<DetallePedidoEntity> findByPrecioUnitarioBetween(BigDecimal precioMinimo, BigDecimal precioMaximo) {
        return detallePedidoRepository.findByPrecioUnitarioBetween(precioMinimo, precioMaximo);
    }

    @Override
    @Transactional(readOnly = true)
    public List<DetallePedidoEntity> findByCantidadGreaterThan(Integer cantidad) {
        return detallePedidoRepository.findByCantidadGreaterThan(cantidad);
    }

    @Override
    @Transactional(readOnly = true)
    public Long countCantidadByProductoId(Integer productoId) {
        return detallePedidoRepository.countCantidadByProductoId(productoId);
    }

    @Override
    @Transactional(readOnly = true)
    public BigDecimal getTotalVentasByProductoId(Integer productoId) {
        return detallePedidoRepository.getTotalVentasByProductoId(productoId);
    }

    @Override
    @Transactional(readOnly = true)
    public Long countByPedidoId(Integer pedidoId) {
        return detallePedidoRepository.countByPedidoId(pedidoId);
    }

    @Override
    @Transactional(readOnly = true)
    public List<Object[]> findProductosMasVendidos() {
        return detallePedidoRepository.findProductosMasVendidos();
    }

    @Override
    @Transactional(readOnly = true)
    public boolean isProductoEnUso(Integer productoId) {
        return detallePedidoRepository.existsByProductoIdProducto(productoId);
    }

    @Override
    public void deleteByPedidoId(Integer pedidoId) {
        detallePedidoRepository.deleteByPedidoIdPedido(pedidoId);
    }

    @Override
    @Transactional(readOnly = true)
    public BigDecimal calcularTotalPedido(Integer pedidoId) {
        List<DetallePedidoEntity> detalles = detallePedidoRepository.findByIdPedidoOrderByIdDetalle(pedidoId);
        
        return detalles.stream()
                .map(DetallePedidoEntity::calcularSubtotal)
                .reduce(BigDecimal.ZERO, BigDecimal::add);
    }

    @Override
    public BigDecimal recalcularYActualizarTotalPedido(Integer pedidoId) {
        BigDecimal nuevoTotal = calcularTotalPedido(pedidoId);
        
        Optional<PedidoEntity> pedidoOpt = pedidoRepository.findById(pedidoId);
        if (pedidoOpt.isPresent()) {
            PedidoEntity pedido = pedidoOpt.get();
            pedido.setTotalPagar(nuevoTotal);
            pedidoRepository.save(pedido);
        }
        
        return nuevoTotal;
    }

    @Override
    public List<DetallePedidoEntity> addMultiples(List<DetallePedidoEntity> detalles) {
        List<DetallePedidoEntity> detallesGuardados = detallePedidoRepository.saveAll(detalles);
        
        // Recalcular total del pedido para todos los detalles (asumiendo mismo pedido)
        if (!detalles.isEmpty() && detalles.get(0).getPedido() != null) {
            recalcularYActualizarTotalPedido(detalles.get(0).getPedido().getIdPedido());
        }
        
        return detallesGuardados;
    }

    @Override
    public void actualizarStockProducto(Integer detalleId) {
        Optional<DetallePedidoEntity> detalleOpt = detallePedidoRepository.findById(detalleId);
        
        if (detalleOpt.isPresent()) {
            DetallePedidoEntity detalle = detalleOpt.get();
            ProductoEntity producto = detalle.getProducto();
            
            if (producto != null) {
                // Reducir stock
                int nuevoStock = producto.getStock() - detalle.getCantidad();
                if (nuevoStock < 0) {
                    throw new RuntimeException("No se puede reducir el stock. Stock actual: " + producto.getStock());
                }
                
                producto.setStock(nuevoStock);
                productoRepository.save(producto);
            }
        }
    }
}
