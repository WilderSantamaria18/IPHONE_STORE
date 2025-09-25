package com.idat.continua2.demo.Service;

import com.idat.continua2.demo.model.ComprobanteEntity;
import com.idat.continua2.demo.model.PedidoEntity;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

/**
 * Interfaz de servicio para gestión de Comprobantes
 * Define operaciones CRUD y consultas de negocio
 * Siguiendo principios SOLID y Spring Data JPA
 */
public interface ComprobanteService {
    
    /**
     * ✅ OPTIMIZADO: Obtiene todos los comprobantes con sus relaciones
     * @return Lista de todos los comprobantes con pedido, cliente y usuario optimizada
     */
    List<ComprobanteEntity> findAllWithRelations();
    
    /**
     * Obtiene todos los comprobantes (sin optimización)
     * @return Lista de todos los comprobantes
     */
    List<ComprobanteEntity> findAll();
    
    /**
     * ✅ OPTIMIZADO: Busca un comprobante por ID con relaciones
     * @param id - ID del comprobante
     * @return Optional con el comprobante encontrado
     */
    Optional<ComprobanteEntity> findByIdWithRelations(Integer id);
    
    /**
     * Busca un comprobante por ID
     * @param id - ID del comprobante
     * @return Optional con el comprobante encontrado
     */
    Optional<ComprobanteEntity> findById(Integer id);
    
    /**
     * Crea un nuevo comprobante
     * @param comprobante - Datos del comprobante a crear
     * @return Comprobante creado
     */
    ComprobanteEntity add(ComprobanteEntity comprobante);
    
    /**
     * Actualiza un comprobante existente
     * @param comprobante - Datos del comprobante a actualizar
     * @return Comprobante actualizado
     */
    ComprobanteEntity update(ComprobanteEntity comprobante);
    
    /**
     * Elimina un comprobante por ID
     * @param id - ID del comprobante a eliminar
     */
    void delete(Integer id);
    
    /**
     * ✅ OPTIMIZADO: Busca comprobante por pedido con relaciones
     * @param pedidoId - ID del pedido
     * @return Optional con el comprobante del pedido
     */
    Optional<ComprobanteEntity> findByPedidoIdWithRelations(Integer pedidoId);
    
    /**
     * ✅ OPTIMIZADO: Busca comprobantes por cliente con relaciones
     * @param clienteId - ID del cliente
     * @return Lista de comprobantes del cliente
     */
    List<ComprobanteEntity> findByClienteIdWithRelations(Integer clienteId);
    
    /**
     * ✅ OPTIMIZADO: Busca comprobantes por usuario con relaciones
     * @param usuarioId - ID del usuario
     * @return Lista de comprobantes del usuario
     */
    List<ComprobanteEntity> findByUsuarioIdWithRelations(Integer usuarioId);
    
    /**
     * Busca comprobante por número de comprobante
     * @param numeroComprobante - Número del comprobante
     * @return Optional con el comprobante encontrado
     */
    Optional<ComprobanteEntity> findByNumeroComprobanteWithRelations(String numeroComprobante);
    
    /**
     * Busca comprobantes por tipo de comprobante
     * @param tipoComprobante - Tipo de comprobante
     * @return Lista de comprobantes del tipo especificado
     */
    List<ComprobanteEntity> findByTipoComprobante(String tipoComprobante);
    
    /**
     * Busca comprobantes por tipo de pago
     * @param tipoPago - Tipo de pago
     * @return Lista de comprobantes con ese tipo de pago
     */
    List<ComprobanteEntity> findByTipoPago(String tipoPago);
    
    /**
     * Busca comprobantes del día actual
     * @return Lista de comprobantes creados hoy
     */
    List<ComprobanteEntity> findComprobantesHoy();
    
    /**
     * Busca comprobantes por rango de fechas
     * @param fechaInicio - Fecha de inicio
     * @param fechaFin - Fecha de fin
     * @return Lista de comprobantes en el rango
     */
    List<ComprobanteEntity> findByFechaEmisionBetween(LocalDateTime fechaInicio, LocalDateTime fechaFin);
    
    /**
     * Busca comprobantes por rango de montos
     * @param montoMinimo - Monto mínimo
     * @param montoMaximo - Monto máximo
     * @return Lista de comprobantes en el rango de montos
     */
    List<ComprobanteEntity> findByTotalPagarBetween(BigDecimal montoMinimo, BigDecimal montoMaximo);
    
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
     * Cuenta comprobantes por cliente
     * @param clienteId - ID del cliente
     * @return Número de comprobantes del cliente
     */
    Long countByClienteId(Integer clienteId);
    
    /**
     * Obtiene los últimos N comprobantes
     * @param limit - Número de comprobantes a obtener
     * @return Lista de los últimos comprobantes
     */
    List<ComprobanteEntity> findTopNByOrderByFechaEmisionDesc(Integer limit);
    
    /**
     * Genera un comprobante desde un pedido
     * @param pedido - Pedido origen
     * @param tipoComprobante - Tipo de comprobante a generar
     * @return Comprobante generado
     */
    ComprobanteEntity generarComprobanteFromPedido(PedidoEntity pedido, String tipoComprobante);
    
    /**
     * Genera el siguiente número de comprobante por tipo
     * @param tipoComprobante - Tipo de comprobante
     * @return Siguiente número disponible
     */
    String generateNumeroComprobante(String tipoComprobante);
    
    /**
     * Verifica si existe un comprobante para un pedido específico
     * @param pedidoId - ID del pedido
     * @return true si existe, false si no
     */
    boolean existsByPedidoId(Integer pedidoId);
    
    /**
     * Calcula y actualiza los montos de un comprobante
     * @param comprobante - Comprobante a calcular
     * @return Comprobante con montos calculados
     */
    ComprobanteEntity calcularMontos(ComprobanteEntity comprobante);
    
    /**
     * Anula un comprobante
     * @param id - ID del comprobante a anular
     * @return Comprobante anulado
     */
    ComprobanteEntity anularComprobante(Integer id);
}
