package com.idat.continua2.demo.Impl;

import com.idat.continua2.demo.Repository.PedidoRepository;
import com.idat.continua2.demo.Repository.DetallePedidoRepository;
import com.idat.continua2.demo.Service.PedidoService;
import com.idat.continua2.demo.model.PedidoEntity;
import com.idat.continua2.demo.model.DetallePedidoEntity;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

/**
 * Implementación del servicio para gestión de Pedidos
 * Proporciona operaciones CRUD y lógica de negocio
 * Integra con PedidoRepository para persistencia
 * ✅ OPTIMIZADO: Métodos con JOIN FETCH para eliminar N+1 queries
 */
@Service
@Transactional
public class PedidoServiceImpl implements PedidoService {

    @Autowired
    private PedidoRepository pedidoRepository;
    
    @Autowired
    private DetallePedidoRepository detallePedidoRepository;

    @Override
    @Transactional(readOnly = true)
    public List<PedidoEntity> findAllWithRelations() {
        return pedidoRepository.findAllWithRelations();
    }

    @Override
    @Transactional(readOnly = true)
    public List<PedidoEntity> findAll() {
        return pedidoRepository.findAll();
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<PedidoEntity> findById(Integer id) {
        return pedidoRepository.findById(id);
    }

    @Override
    public PedidoEntity add(PedidoEntity pedido) {
        // Establecer fecha de pedido si no está definida
        if (pedido.getFechaPedido() == null) {
            pedido.setFechaPedido(LocalDateTime.now());
        }
        
        // Establecer total inicial si no está definido
        if (pedido.getTotalPagar() == null) {
            pedido.setTotalPagar(BigDecimal.ZERO);
        }
        
        return pedidoRepository.save(pedido);
    }

    @Override
    public PedidoEntity update(PedidoEntity pedido) {
        return pedidoRepository.save(pedido);
    }

    @Override
    public void delete(Integer id) {
        pedidoRepository.deleteById(id);
    }

    @Override
    @Transactional(readOnly = true)
    public List<PedidoEntity> findByClienteIdWithRelations(Integer clienteId) {
        return pedidoRepository.findByClienteIdWithRelations(clienteId);
    }

    @Override
    @Transactional(readOnly = true)
    public List<PedidoEntity> findByUsuarioIdWithRelations(Integer usuarioId) {
        return pedidoRepository.findByUsuarioIdWithRelations(usuarioId);
    }

    @Override
    @Transactional(readOnly = true)
    public List<PedidoEntity> findByFechaPedidoBetween(LocalDateTime fechaInicio, LocalDateTime fechaFin) {
        return pedidoRepository.findByFechaPedidoBetween(fechaInicio, fechaFin);
    }

    @Override
    @Transactional(readOnly = true)
    public List<PedidoEntity> findByFechaEntrega(LocalDate fechaEntrega) {
        return pedidoRepository.findByFechaEntrega(fechaEntrega);
    }

    @Override
    @Transactional(readOnly = true)
    public List<PedidoEntity> findByTipoPago(String tipoPago) {
        return pedidoRepository.findByTipoPago(tipoPago);
    }

    @Override
    @Transactional(readOnly = true)
    public List<PedidoEntity> findByTotalPagarBetween(BigDecimal montoMinimo, BigDecimal montoMaximo) {
        return pedidoRepository.findByTotalPagarBetween(montoMinimo, montoMaximo);
    }

    @Override
    @Transactional(readOnly = true)
    public BigDecimal getTotalVentasByFecha(LocalDate fecha) {
        return pedidoRepository.getTotalVentasByFecha(fecha);
    }

    @Override
    @Transactional(readOnly = true)
    public BigDecimal getTotalVentasByMes(Integer year, Integer month) {
        return pedidoRepository.getTotalVentasByMes(year, month);
    }

    @Override
    @Transactional(readOnly = true)
    public Long countByClienteId(Integer clienteId) {
        return pedidoRepository.countByClienteId(clienteId);
    }

    @Override
    @Transactional(readOnly = true)
    public List<PedidoEntity> findTopNByOrderByFechaPedidoDesc(Integer limit) {
        return pedidoRepository.findTopNByOrderByFechaPedidoDesc(limit);
    }

    @Override
    @Transactional(readOnly = true)
    public List<PedidoEntity> findPedidosHoy() {
        return pedidoRepository.findPedidosHoy();
    }

    @Override
    @Transactional(readOnly = true)
    public BigDecimal calcularTotalPedido(Integer pedidoId) {
        List<DetallePedidoEntity> detalles = detallePedidoRepository.findByIdPedidoOrderByIdDetalle(pedidoId);
        
        return detalles.stream()
                .map(detalle -> detalle.getPrecioUnitario().multiply(BigDecimal.valueOf(detalle.getCantidad())))
                .reduce(BigDecimal.ZERO, BigDecimal::add);
    }

    @Override
    public PedidoEntity actualizarTotalPedido(Integer pedidoId) {
        Optional<PedidoEntity> pedidoOpt = pedidoRepository.findById(pedidoId);
        
        if (pedidoOpt.isPresent()) {
            PedidoEntity pedido = pedidoOpt.get();
            BigDecimal nuevoTotal = calcularTotalPedido(pedidoId);
            pedido.setTotalPagar(nuevoTotal);
            return pedidoRepository.save(pedido);
        }
        
        throw new RuntimeException("Pedido no encontrado con ID: " + pedidoId);
    }
}
