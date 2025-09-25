package com.idat.continua2.demo.Impl;

import com.idat.continua2.demo.Repository.ComprobanteRepository;
import com.idat.continua2.demo.Repository.DetallePedidoRepository;
import com.idat.continua2.demo.Service.ComprobanteService;
import com.idat.continua2.demo.Service.DetalleComprobanteService;
import com.idat.continua2.demo.Service.ProductoService;
import com.idat.continua2.demo.model.ComprobanteEntity;
import com.idat.continua2.demo.model.PedidoEntity;
import com.idat.continua2.demo.model.DetalleComprobanteEntity;
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
 * Implementación del servicio de Comprobantes
 */
@Service
@Transactional
public class ComprobanteServiceImpl implements ComprobanteService {

    @Autowired
    private ComprobanteRepository comprobanteRepository;

    @Autowired
    private DetallePedidoRepository detallePedidoRepository;

    @Autowired
    private DetalleComprobanteService detalleComprobanteService;

    @Autowired
    private ProductoService productoService;

    @Override
    @Transactional(readOnly = true)
    public List<ComprobanteEntity> findAllWithRelations() {
        return comprobanteRepository.findAllWithRelations();
    }

    @Override
    @Transactional(readOnly = true)
    public List<ComprobanteEntity> findAll() {
        return comprobanteRepository.findAll();
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<ComprobanteEntity> findByIdWithRelations(Integer id) {
        return comprobanteRepository.findByIdWithRelations(id);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<ComprobanteEntity> findById(Integer id) {
        return comprobanteRepository.findById(id);
    }

    @Override
    public ComprobanteEntity add(ComprobanteEntity comprobante) {
        if (comprobante.getFechaEmision() == null) {
            comprobante.setFechaEmision(LocalDateTime.now());
        }
        if (comprobante.getNumeroComprobante() == null || comprobante.getNumeroComprobante().isEmpty()) {
            comprobante.setNumeroComprobante(generateNumeroComprobante(comprobante.getTipoComprobante()));
        }
        comprobante = calcularMontos(comprobante);
        return comprobanteRepository.save(comprobante);
    }

    @Override
    public ComprobanteEntity update(ComprobanteEntity comprobante) {
        comprobante = calcularMontos(comprobante);
        return comprobanteRepository.save(comprobante);
    }

    @Override
    public void delete(Integer id) {
        comprobanteRepository.deleteById(id);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<ComprobanteEntity> findByPedidoIdWithRelations(Integer pedidoId) {
        return comprobanteRepository.findByPedidoIdWithRelations(pedidoId);
    }

    @Override
    @Transactional(readOnly = true)
    public List<ComprobanteEntity> findByClienteIdWithRelations(Integer clienteId) {
        return comprobanteRepository.findByClienteIdWithRelations(clienteId);
    }

    @Override
    @Transactional(readOnly = true)
    public List<ComprobanteEntity> findByUsuarioIdWithRelations(Integer usuarioId) {
        return comprobanteRepository.findByUsuarioIdWithRelations(usuarioId);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<ComprobanteEntity> findByNumeroComprobanteWithRelations(String numeroComprobante) {
        return comprobanteRepository.findByNumeroComprobanteWithRelations(numeroComprobante);
    }

    @Override
    @Transactional(readOnly = true)
    public List<ComprobanteEntity> findByTipoComprobante(String tipoComprobante) {
        return comprobanteRepository.findByTipoComprobante(tipoComprobante);
    }

    @Override
    @Transactional(readOnly = true)
    public List<ComprobanteEntity> findByTipoPago(String tipoPago) {
        return comprobanteRepository.findByTipoPago(tipoPago);
    }

    @Override
    @Transactional(readOnly = true)
    public List<ComprobanteEntity> findComprobantesHoy() {
        return comprobanteRepository.findComprobantesHoy();
    }

    @Override
    @Transactional(readOnly = true)
    public List<ComprobanteEntity> findByFechaEmisionBetween(LocalDateTime fechaInicio, LocalDateTime fechaFin) {
        return comprobanteRepository.findByFechaEmisionBetween(fechaInicio, fechaFin);
    }

    @Override
    @Transactional(readOnly = true)
    public List<ComprobanteEntity> findByTotalPagarBetween(BigDecimal montoMinimo, BigDecimal montoMaximo) {
        return comprobanteRepository.findByTotalPagarBetween(montoMinimo, montoMaximo);
    }

    @Override
    @Transactional(readOnly = true)
    public BigDecimal getTotalVentasByFecha(LocalDate fecha) {
        return comprobanteRepository.getTotalVentasByFecha(fecha);
    }

    @Override
    @Transactional(readOnly = true)
    public BigDecimal getTotalVentasByMes(Integer year, Integer month) {
        return comprobanteRepository.getTotalVentasByMes(year, month);
    }

    @Override
    @Transactional(readOnly = true)
    public Long countByClienteId(Integer clienteId) {
        return comprobanteRepository.countByClienteId(clienteId);
    }

    @Override
    @Transactional(readOnly = true)
    public List<ComprobanteEntity> findTopNByOrderByFechaEmisionDesc(Integer limit) {
        return comprobanteRepository.findTopNByOrderByFechaEmisionDesc(limit);
    }

    @Override
    public ComprobanteEntity generarComprobanteFromPedido(PedidoEntity pedido, String tipoComprobante) {
        if (existsByPedidoId(pedido.getIdPedido())) {
            throw new IllegalStateException("Ya existe un comprobante para este pedido");
        }

        List<DetallePedidoEntity> detallesPedido =
            detallePedidoRepository.findByPedidoIdWithRelations(pedido.getIdPedido());

        if (detallesPedido.isEmpty()) {
            throw new IllegalStateException("El pedido no tiene productos");
        }

        ComprobanteEntity comprobante = new ComprobanteEntity();
        comprobante.setPedido(pedido);
        comprobante.setCliente(pedido.getCliente());
        comprobante.setUsuario(pedido.getUsuario());
        comprobante.setTipoPago(pedido.getTipoPago());
        comprobante.setTipoComprobante(tipoComprobante);
        comprobante.setNumeroComprobante(generateNumeroComprobante(tipoComprobante));
        comprobante.setFechaEmision(LocalDateTime.now());

        BigDecimal subtotal = detallesPedido.stream()
            .map(d -> d.getPrecioUnitario().multiply(new BigDecimal(d.getCantidad())))
            .reduce(BigDecimal.ZERO, BigDecimal::add);

        comprobante.calcularMontos(subtotal);

        comprobante = comprobanteRepository.save(comprobante);

        for (DetallePedidoEntity detPedido : detallesPedido) {
            DetalleComprobanteEntity detComp = new DetalleComprobanteEntity();
            detComp.setComprobante(comprobante);
            detComp.setProducto(detPedido.getProducto());
            detComp.setCantidad(detPedido.getCantidad());
            detComp.setPrecioUnitario(detPedido.getPrecioUnitario());
            detalleComprobanteService.add(detComp);
        }

        return comprobante;
    }

    @Override
    public String generateNumeroComprobante(String tipoComprobante) {
        return generarNumeroComprobanteSeguro(tipoComprobante);
    }

    private String generarNumeroComprobanteSeguro(String tipoComprobante) {
        String prefijo = (tipoComprobante != null && tipoComprobante.toUpperCase().startsWith("FAC"))
                ? "FAC" : "BOL";
        for (int i = 0; i < 5; i++) {
            Integer next = comprobanteRepository.getNextNumeroComprobanteByPrefijo(prefijo);
            if (next == null) next = 1;
            String numero = prefijo + String.format("%06d", next);
            if (!comprobanteRepository.existsByNumeroComprobante(numero)) {
                return numero;
            }
            // Pequeño fallback: si chocó, continuar y esperar siguiente transacción
            try { Thread.sleep(10); } catch (InterruptedException ignored) {}
        }
        throw new IllegalStateException("No se pudo generar número único de comprobante");
    }

    @Override
    @Transactional(readOnly = true)
    public boolean existsByPedidoId(Integer pedidoId) {
        return comprobanteRepository.existsByPedidoId(pedidoId);
    }

    @Override
    public ComprobanteEntity calcularMontos(ComprobanteEntity comprobante) {
        if (comprobante.getSubtotal() != null) {
            BigDecimal igv = comprobante.getSubtotal().multiply(new BigDecimal("0.18"));
            comprobante.setIgv(igv);
            comprobante.setTotalPagar(comprobante.getSubtotal().add(igv));
        }
        return comprobante;
    }

    @Override
    public ComprobanteEntity anularComprobante(Integer id) {
        Optional<ComprobanteEntity> opt = findByIdWithRelations(id);
        if (opt.isPresent()) {
            ComprobanteEntity comprobante = opt.get();
            List<DetalleComprobanteEntity> detalles =
                detalleComprobanteService.findByComprobanteIdWithRelations(id);

            for (DetalleComprobanteEntity det : detalles) {
                Integer stockActual = det.getProducto().getStock();
                productoService.actualizarStock(
                    det.getProducto().getIdProducto(),
                    stockActual + det.getCantidad()
                );
            }
            comprobante.setTipoComprobante("ANULADO");
            return update(comprobante);
        }
        throw new IllegalArgumentException("Comprobante no encontrado");
    }
}