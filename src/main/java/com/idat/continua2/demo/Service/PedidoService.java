package com.idat.continua2.demo.Service;

import com.idat.continua2.demo.model.PedidoEntity;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

/**
 * Interfaz de servicio para gestión de Pedidos
 * Define operaciones CRUD y consultas de negocio
 * Siguiendo principios SOLID y Spring Data JPA
 */
public interface PedidoService {
    
    /**
     * ✅ OPTIMIZADO: Obtiene todos los pedidos con sus relaciones
     * @return Lista de todos los pedidos con cliente y usuario optimizada
     */
    List<PedidoEntity> findAllWithRelations();
    
    /**
     * Obtiene todos los pedidos (sin optimización)
     * @return Lista de todos los pedidos
     */
    List<PedidoEntity> findAll();
    
    /**
     * Busca un pedido por ID
     * @param id - ID del pedido
     * @return Optional con el pedido encontrado
     */
    Optional<PedidoEntity> findById(Integer id);
    
    /**
     * Crea un nuevo pedido
     * @param pedido - Datos del pedido a crear
     * @return Pedido creado
     */
    PedidoEntity add(PedidoEntity pedido);
    
    /**
     * Actualiza un pedido existente
     * @param pedido - Datos del pedido a actualizar
     * @return Pedido actualizado
     */
    PedidoEntity update(PedidoEntity pedido);
    
    /**
     * Elimina un pedido por ID
     * @param id - ID del pedido a eliminar
     */
    void delete(Integer id);
    
    /**
     * ✅ OPTIMIZADO: Busca pedidos por cliente con relaciones
     * @param clienteId - ID del cliente
     * @return Lista de pedidos del cliente
     */
    List<PedidoEntity> findByClienteIdWithRelations(Integer clienteId);
    
    /**
     * ✅ OPTIMIZADO: Busca pedidos por usuario con relaciones
     * @param usuarioId - ID del usuario
     * @return Lista de pedidos del usuario
     */
    List<PedidoEntity> findByUsuarioIdWithRelations(Integer usuarioId);
    
    /**
     * Busca pedidos del día actual
     * @return Lista de pedidos creados hoy
     */
    List<PedidoEntity> findPedidosHoy();
    
    /**
     * Busca pedidos por rango de fechas
     * @param fechaInicio - Fecha de inicio
     * @param fechaFin - Fecha de fin
     * @return Lista de pedidos en el rango
     */
    List<PedidoEntity> findByFechaPedidoBetween(LocalDateTime fechaInicio, LocalDateTime fechaFin);
    
    /**
     * Busca pedidos por fecha de entrega
     * @param fechaEntrega - Fecha de entrega
     * @return Lista de pedidos para entregar en esa fecha
     */
    List<PedidoEntity> findByFechaEntrega(LocalDate fechaEntrega);
    
    /**
     * Busca pedidos por tipo de pago
     * @param tipoPago - Tipo de pago
     * @return Lista de pedidos con ese tipo de pago
     */
    List<PedidoEntity> findByTipoPago(String tipoPago);
    
    /**
     * Busca pedidos por rango de montos
     * @param montoMinimo - Monto mínimo
     * @param montoMaximo - Monto máximo
     * @return Lista de pedidos en el rango de montos
     */
    List<PedidoEntity> findByTotalPagarBetween(BigDecimal montoMinimo, BigDecimal montoMaximo);
    
    /**
     * Obtiene el total de ventas por fecha
     * @param fecha - Fecha a consultar
     * @return Total de ventas del día
     */
    BigDecimal getTotalVentasByFecha(LocalDate fecha);
    
    /**
     * Obtiene el total de ventas por mes
     * @param year - Año
     * @param month - Mes
     * @return Total de ventas del mes
     */
    BigDecimal getTotalVentasByMes(Integer year, Integer month);
    
    /**
     * Cuenta pedidos por cliente
     * @param clienteId - ID del cliente
     * @return Número de pedidos del cliente
     */
    Long countByClienteId(Integer clienteId);
    
    /**
     * Obtiene los últimos N pedidos
     * @param limit - Número de pedidos a obtener
     * @return Lista de los últimos pedidos
     */
    List<PedidoEntity> findTopNByOrderByFechaPedidoDesc(Integer limit);
    
    /**
     * Calcula el total de un pedido basado en sus detalles
     * @param pedidoId - ID del pedido
     * @return Total calculado
     */
    BigDecimal calcularTotalPedido(Integer pedidoId);
    
    /**
     * Actualiza el total de un pedido
     * @param pedidoId - ID del pedido
     * @return Pedido actualizado
     */
    PedidoEntity actualizarTotalPedido(Integer pedidoId);
}
