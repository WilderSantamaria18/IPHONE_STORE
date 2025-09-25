package com.idat.continua2.demo.Repository;

import com.idat.continua2.demo.model.ComprobanteEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

/**
 * Repositorio para entidad Comprobante
 * Extiende JpaRepository con Integer como tipo de ID
 * Incluye consultas personalizadas optimizadas
 */
@Repository
public interface ComprobanteRepository extends JpaRepository<ComprobanteEntity, Integer> {
    
    /**
     * ✅ OPTIMIZADO: Busca todos los comprobantes con sus relaciones
     * @return Lista de comprobantes con pedido, cliente y usuario optimizada
     */
    @Query("SELECT c FROM ComprobanteEntity c " +
           "JOIN FETCH c.pedido p " +
           "JOIN FETCH c.cliente cl " +
           "JOIN FETCH c.usuario u " +
           "ORDER BY c.fechaEmision DESC")
    List<ComprobanteEntity> findAllWithRelations();
    
    /**
     * ✅ OPTIMIZADO: Busca comprobante por ID con relaciones
     * @param id - ID del comprobante
     * @return Optional con el comprobante encontrado
     */
    @Query("SELECT c FROM ComprobanteEntity c " +
           "JOIN FETCH c.pedido p " +
           "JOIN FETCH c.cliente cl " +
           "JOIN FETCH c.usuario u " +
           "WHERE c.idComprobante = :id")
    Optional<ComprobanteEntity> findByIdWithRelations(@Param("id") Integer id);
    
    /**
     * Busca comprobante por ID de pedido
     * @param pedidoId - ID del pedido
     * @return Optional con el comprobante del pedido
     */
    @Query("SELECT c FROM ComprobanteEntity c " +
           "JOIN FETCH c.pedido p " +
           "JOIN FETCH c.cliente cl " +
           "JOIN FETCH c.usuario u " +
           "WHERE p.idPedido = :pedidoId")
    Optional<ComprobanteEntity> findByPedidoIdWithRelations(@Param("pedidoId") Integer pedidoId);
    
    /**
     * Busca comprobantes por cliente con relaciones
     * @param clienteId - ID del cliente
     * @return Lista de comprobantes del cliente
     */
    @Query("SELECT c FROM ComprobanteEntity c " +
           "JOIN FETCH c.pedido p " +
           "JOIN FETCH c.cliente cl " +
           "JOIN FETCH c.usuario u " +
           "WHERE cl.idCliente = :clienteId " +
           "ORDER BY c.fechaEmision DESC")
    List<ComprobanteEntity> findByClienteIdWithRelations(@Param("clienteId") Integer clienteId);
    
    /**
     * Busca comprobantes por usuario con relaciones
     * @param usuarioId - ID del usuario
     * @return Lista de comprobantes del usuario
     */
    @Query("SELECT c FROM ComprobanteEntity c " +
           "JOIN FETCH c.pedido p " +
           "JOIN FETCH c.cliente cl " +
           "JOIN FETCH c.usuario u " +
           "WHERE u.idUsuario = :usuarioId " +
           "ORDER BY c.fechaEmision DESC")
    List<ComprobanteEntity> findByUsuarioIdWithRelations(@Param("usuarioId") Integer usuarioId);
    
    /**
     * Busca comprobantes por número de comprobante
     * @param numeroComprobante - Número del comprobante
     * @return Optional con el comprobante encontrado
     */
    @Query("SELECT c FROM ComprobanteEntity c " +
           "JOIN FETCH c.pedido p " +
           "JOIN FETCH c.cliente cl " +
           "JOIN FETCH c.usuario u " +
           "WHERE c.numeroComprobante = :numeroComprobante")
    Optional<ComprobanteEntity> findByNumeroComprobanteWithRelations(@Param("numeroComprobante") String numeroComprobante);
    
    /**
     * Busca comprobantes por tipo de comprobante
     * @param tipoComprobante - Tipo de comprobante
     * @return Lista de comprobantes del tipo especificado
     */
    @Query("SELECT c FROM ComprobanteEntity c " +
           "JOIN FETCH c.pedido p " +
           "JOIN FETCH c.cliente cl " +
           "JOIN FETCH c.usuario u " +
           "WHERE c.tipoComprobante = :tipoComprobante " +
           "ORDER BY c.fechaEmision DESC")
    List<ComprobanteEntity> findByTipoComprobante(@Param("tipoComprobante") String tipoComprobante);
    
    /**
     * Busca comprobantes por tipo de pago
     * @param tipoPago - Tipo de pago
     * @return Lista de comprobantes con ese tipo de pago
     */
    @Query("SELECT c FROM ComprobanteEntity c " +
           "JOIN FETCH c.pedido p " +
           "JOIN FETCH c.cliente cl " +
           "JOIN FETCH c.usuario u " +
           "WHERE c.tipoPago = :tipoPago " +
           "ORDER BY c.fechaEmision DESC")
    List<ComprobanteEntity> findByTipoPago(@Param("tipoPago") String tipoPago);
    
    /**
     * Busca comprobantes por rango de fechas
     * @param fechaInicio - Fecha de inicio
     * @param fechaFin - Fecha de fin
     * @return Lista de comprobantes en el rango
     */
    @Query("SELECT c FROM ComprobanteEntity c " +
           "JOIN FETCH c.pedido p " +
           "JOIN FETCH c.cliente cl " +
           "JOIN FETCH c.usuario u " +
           "WHERE c.fechaEmision BETWEEN :fechaInicio AND :fechaFin " +
           "ORDER BY c.fechaEmision DESC")
    List<ComprobanteEntity> findByFechaEmisionBetween(@Param("fechaInicio") LocalDateTime fechaInicio, 
                                                     @Param("fechaFin") LocalDateTime fechaFin);
    
    /**
     * Busca comprobantes por rango de montos
     * @param montoMinimo - Monto mínimo
     * @param montoMaximo - Monto máximo
     * @return Lista de comprobantes en el rango de montos
     */
    @Query("SELECT c FROM ComprobanteEntity c " +
           "JOIN FETCH c.pedido p " +
           "JOIN FETCH c.cliente cl " +
           "JOIN FETCH c.usuario u " +
           "WHERE c.totalPagar BETWEEN :montoMinimo AND :montoMaximo " +
           "ORDER BY c.totalPagar DESC")
    List<ComprobanteEntity> findByTotalPagarBetween(@Param("montoMinimo") BigDecimal montoMinimo, 
                                                   @Param("montoMaximo") BigDecimal montoMaximo);
    
    /**
     * Obtiene el total de ventas por fecha
     * @param fecha - Fecha a consultar
     * @return Total de ventas del día
     */
    @Query("SELECT COALESCE(SUM(c.totalPagar), 0) FROM ComprobanteEntity c WHERE DATE(c.fechaEmision) = :fecha")
    BigDecimal getTotalVentasByFecha(@Param("fecha") LocalDate fecha);
    
    /**
     * Obtiene el total de ventas por mes
     * @param year - Año
     * @param month - Mes
     * @return Total de ventas del mes
     */
    @Query("SELECT COALESCE(SUM(c.totalPagar), 0) FROM ComprobanteEntity c WHERE YEAR(c.fechaEmision) = :year AND MONTH(c.fechaEmision) = :month")
    BigDecimal getTotalVentasByMes(@Param("year") Integer year, @Param("month") Integer month);
    
    /**
     * Cuenta comprobantes por cliente
     * @param clienteId - ID del cliente
     * @return Número de comprobantes del cliente
     */
    @Query("SELECT COUNT(c) FROM ComprobanteEntity c WHERE c.cliente.idCliente = :clienteId")
    Long countByClienteId(@Param("clienteId") Integer clienteId);
    
    /**
     * Obtiene los últimos N comprobantes
     * @param limit - Número de comprobantes a obtener
     * @return Lista de los últimos comprobantes
     */
    @Query("SELECT c FROM ComprobanteEntity c " +
           "JOIN FETCH c.pedido p " +
           "JOIN FETCH c.cliente cl " +
           "JOIN FETCH c.usuario u " +
           "ORDER BY c.fechaEmision DESC LIMIT :limit")
    List<ComprobanteEntity> findTopNByOrderByFechaEmisionDesc(@Param("limit") Integer limit);
    
    /**
     * Busca comprobantes del día actual
     * @return Lista de comprobantes de hoy
     */
    @Query("SELECT c FROM ComprobanteEntity c " +
           "JOIN FETCH c.pedido p " +
           "JOIN FETCH c.cliente cl " +
           "JOIN FETCH c.usuario u " +
           "WHERE DATE(c.fechaEmision) = CURRENT_DATE " +
           "ORDER BY c.fechaEmision DESC")
    List<ComprobanteEntity> findComprobantesHoy();
    
    /**
     * Genera el siguiente número de comprobante por tipo
     * @param tipoComprobante - Tipo de comprobante
     * @return Siguiente número disponible
     */
    @Query("SELECT COALESCE(MAX(CAST(SUBSTRING(c.numeroComprobante,4) AS integer)),0) + 1 " +
           "FROM ComprobanteEntity c " +
           "WHERE UPPER(SUBSTRING(c.numeroComprobante,1,3)) = UPPER(:prefijo)")
    Integer getNextNumeroComprobanteByPrefijo(@Param("prefijo") String prefijo);

    boolean existsByNumeroComprobante(String numeroComprobante);
    
    /**
     * Verifica si existe un comprobante para un pedido específico
     * @param pedidoId - ID del pedido
     * @return true si existe, false si no
     */
    @Query("SELECT COUNT(c) > 0 FROM ComprobanteEntity c WHERE c.pedido.idPedido = :pedidoId")
    boolean existsByPedidoId(@Param("pedidoId") Integer pedidoId);
}
