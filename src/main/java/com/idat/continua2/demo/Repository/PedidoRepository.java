package com.idat.continua2.demo.Repository;

import com.idat.continua2.demo.model.PedidoEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

/**
 * Repositorio para entidad Pedido
 * Extiende JpaRepository con Integer como tipo de ID
 * Incluye consultas personalizadas optimizadas
 */
@Repository
public interface PedidoRepository extends JpaRepository<PedidoEntity, Integer> {
    
    /**
     * ✅ OPTIMIZADO: Busca todos los pedidos con sus relaciones
     * @return Lista de pedidos con cliente y usuario optimizada
     */
    @Query("SELECT p FROM PedidoEntity p JOIN FETCH p.cliente JOIN FETCH p.usuario ORDER BY p.fechaPedido DESC")
    List<PedidoEntity> findAllWithRelations();
    
    /**
     * ✅ OPTIMIZADO: Busca pedidos por cliente con relaciones
     * @param clienteId - ID del cliente
     * @return Lista de pedidos del cliente
     */
    @Query("SELECT p FROM PedidoEntity p JOIN FETCH p.cliente JOIN FETCH p.usuario WHERE p.cliente.idCliente = :clienteId ORDER BY p.fechaPedido DESC")
    List<PedidoEntity> findByClienteIdWithRelations(@Param("clienteId") Integer clienteId);
    
    /**
     * ✅ OPTIMIZADO: Busca pedidos por usuario con relaciones
     * @param usuarioId - ID del usuario
     * @return Lista de pedidos del usuario
     */
    @Query("SELECT p FROM PedidoEntity p JOIN FETCH p.cliente JOIN FETCH p.usuario WHERE p.usuario.idUsuario = :usuarioId ORDER BY p.fechaPedido DESC")
    List<PedidoEntity> findByUsuarioIdWithRelations(@Param("usuarioId") Integer usuarioId);
    
    /**
     * Busca pedidos por rango de fechas
     * @param fechaInicio - Fecha de inicio
     * @param fechaFin - Fecha de fin
     * @return Lista de pedidos en el rango
     */
    @Query("SELECT p FROM PedidoEntity p JOIN FETCH p.cliente JOIN FETCH p.usuario WHERE p.fechaPedido BETWEEN :fechaInicio AND :fechaFin ORDER BY p.fechaPedido DESC")
    List<PedidoEntity> findByFechaPedidoBetween(@Param("fechaInicio") LocalDateTime fechaInicio, 
                                               @Param("fechaFin") LocalDateTime fechaFin);
    
    /**
     * Busca pedidos por fecha de entrega
     * @param fechaEntrega - Fecha de entrega
     * @return Lista de pedidos para entregar en esa fecha
     */
    @Query("SELECT p FROM PedidoEntity p JOIN FETCH p.cliente JOIN FETCH p.usuario WHERE p.fechaEntrega = :fechaEntrega ORDER BY p.fechaPedido DESC")
    List<PedidoEntity> findByFechaEntrega(@Param("fechaEntrega") LocalDate fechaEntrega);
    
    /**
     * Busca pedidos por tipo de pago
     * @param tipoPago - Tipo de pago
     * @return Lista de pedidos con ese tipo de pago
     */
    @Query("SELECT p FROM PedidoEntity p JOIN FETCH p.cliente JOIN FETCH p.usuario WHERE p.tipoPago = :tipoPago ORDER BY p.fechaPedido DESC")
    List<PedidoEntity> findByTipoPago(@Param("tipoPago") String tipoPago);
    
    /**
     * Busca pedidos por rango de montos
     * @param montoMinimo - Monto mínimo
     * @param montoMaximo - Monto máximo
     * @return Lista de pedidos en el rango de montos
     */
    @Query("SELECT p FROM PedidoEntity p JOIN FETCH p.cliente JOIN FETCH p.usuario WHERE p.totalPagar BETWEEN :montoMinimo AND :montoMaximo ORDER BY p.totalPagar DESC")
    List<PedidoEntity> findByTotalPagarBetween(@Param("montoMinimo") BigDecimal montoMinimo, 
                                              @Param("montoMaximo") BigDecimal montoMaximo);
    
    /**
     * Obtiene el total de ventas por fecha
     * @param fecha - Fecha a consultar
     * @return Total de ventas del día
     */
    @Query("SELECT COALESCE(SUM(p.totalPagar), 0) FROM PedidoEntity p WHERE DATE(p.fechaPedido) = :fecha")
    BigDecimal getTotalVentasByFecha(@Param("fecha") LocalDate fecha);
    
    /**
     * Obtiene el total de ventas por mes
     * @param year - Año
     * @param month - Mes
     * @return Total de ventas del mes
     */
    @Query("SELECT COALESCE(SUM(p.totalPagar), 0) FROM PedidoEntity p WHERE YEAR(p.fechaPedido) = :year AND MONTH(p.fechaPedido) = :month")
    BigDecimal getTotalVentasByMes(@Param("year") Integer year, @Param("month") Integer month);
    
    /**
     * Cuenta pedidos por cliente
     * @param clienteId - ID del cliente
     * @return Número de pedidos del cliente
     */
    @Query("SELECT COUNT(p) FROM PedidoEntity p WHERE p.cliente.idCliente = :clienteId")
    Long countByClienteId(@Param("clienteId") Integer clienteId);
    
    /**
     * Obtiene los últimos N pedidos
     * @param limit - Número de pedidos a obtener
     * @return Lista de los últimos pedidos
     */
    @Query("SELECT p FROM PedidoEntity p JOIN FETCH p.cliente JOIN FETCH p.usuario ORDER BY p.fechaPedido DESC LIMIT :limit")
    List<PedidoEntity> findTopNByOrderByFechaPedidoDesc(@Param("limit") Integer limit);
    
    /**
     * Busca pedidos del día actual
     * @return Lista de pedidos de hoy
     */
    @Query("SELECT p FROM PedidoEntity p JOIN FETCH p.cliente JOIN FETCH p.usuario WHERE DATE(p.fechaPedido) = CURRENT_DATE ORDER BY p.fechaPedido DESC")
    List<PedidoEntity> findPedidosHoy();
}
