package com.idat.continua2.demo.Service;

import com.idat.continua2.demo.model.DetallePedidoEntity;
import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

/**
 * Interfaz de servicio para gestión de DetallePedido
 * Define operaciones CRUD y consultas de negocio
 * Siguiendo principios SOLID y Spring Data JPA
 * Integración con módulo Pedido para cálculos automáticos
 */
public interface DetallePedidoService {
    
    /**
     * ✅ OPTIMIZADO: Obtiene todos los detalles con sus relaciones
     * @return Lista de todos los detalles con pedido y producto optimizada
     */
    List<DetallePedidoEntity> findAllWithRelations();
    
    /**
     * Obtiene todos los detalles (sin optimización)
     * @return Lista de todos los detalles
     */
    List<DetallePedidoEntity> findAll();
    
    /**
     * Busca un detalle por ID
     * @param id - ID del detalle
     * @return Optional con el detalle encontrado
     */
    Optional<DetallePedidoEntity> findById(Integer id);
    
    /**
     * Crea un nuevo detalle de pedido
     * Recalcula automáticamente el total del pedido
     * @param detalle - Datos del detalle a crear
     * @return Detalle creado
     */
    DetallePedidoEntity add(DetallePedidoEntity detalle);
    
    /**
     * Actualiza un detalle existente
     * Recalcula automáticamente el total del pedido
     * @param detalle - Datos del detalle a actualizar
     * @return Detalle actualizado
     */
    DetallePedidoEntity update(DetallePedidoEntity detalle);
    
    /**
     * Elimina un detalle por ID
     * Recalcula automáticamente el total del pedido
     * @param id - ID del detalle a eliminar
     */
    void delete(Integer id);
    
    /**
     * ✅ OPTIMIZADO: Busca detalles por pedido con relaciones
     * @param pedidoId - ID del pedido
     * @return Lista de detalles del pedido
     */
    List<DetallePedidoEntity> findByPedidoIdWithRelations(Integer pedidoId);
    
    /**
     * ✅ OPTIMIZADO: Busca detalles por producto con relaciones
     * @param productoId - ID del producto
     * @return Lista de detalles del producto
     */
    List<DetallePedidoEntity> findByProductoIdWithRelations(Integer productoId);
    
    /**
     * Busca detalles por pedido (método estándar)
     * @param pedidoId - ID del pedido
     * @return Lista de detalles ordenados por ID
     */
    List<DetallePedidoEntity> findByPedidoId(Integer pedidoId);
    
    /**
     * Busca detalles por producto
     * @param productoId - ID del producto
     * @return Lista de detalles del producto
     */
    List<DetallePedidoEntity> findByProductoId(Integer productoId);
    
    /**
     * Busca detalles por rango de cantidad
     * @param cantidadMinima - Cantidad mínima
     * @param cantidadMaxima - Cantidad máxima
     * @return Lista de detalles en el rango
     */
    List<DetallePedidoEntity> findByCantidadBetween(Integer cantidadMinima, Integer cantidadMaxima);
    
    /**
     * Busca detalles por rango de precio unitario
     * @param precioMinimo - Precio mínimo
     * @param precioMaximo - Precio máximo
     * @return Lista de detalles en el rango de precio
     */
    List<DetallePedidoEntity> findByPrecioUnitarioBetween(BigDecimal precioMinimo, BigDecimal precioMaximo);
    
    /**
     * Busca detalles con cantidad mayor a un valor específico
     * @param cantidad - Cantidad mínima
     * @return Lista de detalles con cantidad mayor
     */
    List<DetallePedidoEntity> findByCantidadGreaterThan(Integer cantidad);
    
    /**
     * Cuenta la cantidad total de productos vendidos
     * @param productoId - ID del producto
     * @return Cantidad total vendida del producto
     */
    Long countCantidadByProductoId(Integer productoId);
    
    /**
     * Calcula el total vendido de un producto
     * @param productoId - ID del producto
     * @return Total vendido del producto
     */
    BigDecimal getTotalVentasByProductoId(Integer productoId);
    
    /**
     * Cuenta el número de detalles de un pedido
     * @param pedidoId - ID del pedido
     * @return Número de items en el pedido
     */
    Long countByPedidoId(Integer pedidoId);
    
    /**
     * Obtiene los productos más vendidos
     * @return Lista de productos más vendidos con estadísticas
     */
    List<Object[]> findProductosMasVendidos();
    
    /**
     * Verifica si un producto está siendo usado en algún detalle
     * @param productoId - ID del producto
     * @return true si el producto está en uso
     */
    boolean isProductoEnUso(Integer productoId);
    
    /**
     * Elimina todos los detalles de un pedido específico
     * @param pedidoId - ID del pedido
     */
    void deleteByPedidoId(Integer pedidoId);
    
    /**
     * Calcula el total de un pedido sumando sus detalles
     * @param pedidoId - ID del pedido
     * @return Total calculado del pedido
     */
    BigDecimal calcularTotalPedido(Integer pedidoId);
    
    /**
     * Recalcula y actualiza el total de un pedido
     * Método integrado que actualiza automáticamente el pedido
     * @param pedidoId - ID del pedido a recalcular
     * @return Nuevo total del pedido
     */
    BigDecimal recalcularYActualizarTotalPedido(Integer pedidoId);
    
    /**
     * Crea múltiples detalles de pedido en lote
     * Optimiza la operación para múltiples inserts
     * @param detalles - Lista de detalles a crear
     * @return Lista de detalles creados
     */
    List<DetallePedidoEntity> addMultiples(List<DetallePedidoEntity> detalles);
    
    /**
     * Actualiza el stock de productos basado en las ventas
     * Integración con módulo Producto
     * @param detalleId - ID del detalle procesado
     */
    void actualizarStockProducto(Integer detalleId);
}
