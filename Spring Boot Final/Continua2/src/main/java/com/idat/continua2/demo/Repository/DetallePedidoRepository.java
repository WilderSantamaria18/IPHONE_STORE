package com.idat.continua2.demo.Repository;

import com.idat.continua2.demo.model.DetallePedidoEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.util.List;

/**
 * Repositorio para entidad DetallePedido
 * Extiende JpaRepository con Integer como tipo de ID
 * Incluye consultas personalizadas optimizadas con JOIN FETCH
 */
@Repository
public interface DetallePedidoRepository extends JpaRepository<DetallePedidoEntity, Integer> {
    
    /**
     * ✅ OPTIMIZADO: Busca todos los detalles con sus relaciones
     * @return Lista de detalles con pedido y producto optimizada
     */
    @Query("SELECT d FROM DetallePedidoEntity d JOIN FETCH d.pedido JOIN FETCH d.producto ORDER BY d.idDetalle DESC")
    List<DetallePedidoEntity> findAllWithRelations();
    
    /**
     * ✅ OPTIMIZADO: Busca detalles por pedido con relaciones
     * @param pedidoId - ID del pedido
     * @return Lista de detalles del pedido ordenados por ID
     */
    @Query("SELECT d FROM DetallePedidoEntity d JOIN FETCH d.pedido JOIN FETCH d.producto WHERE d.pedido.idPedido = :pedidoId ORDER BY d.idDetalle")
    List<DetallePedidoEntity> findByPedidoIdWithRelations(@Param("pedidoId") Integer pedidoId);
    
    /**
     * ✅ OPTIMIZADO: Busca detalles por producto con relaciones
     * @param productoId - ID del producto
     * @return Lista de detalles del producto
     */
    @Query("SELECT d FROM DetallePedidoEntity d JOIN FETCH d.pedido JOIN FETCH d.producto WHERE d.producto.idProducto = :productoId ORDER BY d.idDetalle DESC")
    List<DetallePedidoEntity> findByProductoIdWithRelations(@Param("productoId") Integer productoId);
    
    /**
     * Busca detalles por pedido (método estándar sin optimización)
     * @param pedidoId - ID del pedido
     * @return Lista de detalles ordenados por ID
     */
    @Query("SELECT d FROM DetallePedidoEntity d WHERE d.pedido.idPedido = :pedidoId ORDER BY d.idDetalle")
    List<DetallePedidoEntity> findByIdPedidoOrderByIdDetalle(@Param("pedidoId") Integer pedidoId);
    
    /**
     * Busca detalles por producto
     * @param productoId - ID del producto
     * @return Lista de detalles del producto
     */
    List<DetallePedidoEntity> findByProductoIdProducto(Integer productoId);
    
    /**
     * Cuenta la cantidad total de productos vendidos
     * @param productoId - ID del producto
     * @return Cantidad total vendida del producto
     */
    @Query("SELECT COALESCE(SUM(d.cantidad), 0) FROM DetallePedidoEntity d WHERE d.producto.idProducto = :productoId")
    Long countCantidadByProductoId(@Param("productoId") Integer productoId);
    
    /**
     * Calcula el total vendido de un producto
     * @param productoId - ID del producto
     * @return Total vendido del producto
     */
    @Query("SELECT COALESCE(SUM(d.subtotal), 0) FROM DetallePedidoEntity d WHERE d.producto.idProducto = :productoId")
    BigDecimal getTotalVentasByProductoId(@Param("productoId") Integer productoId);
    
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
     * Cuenta el número de detalles de un pedido
     * @param pedidoId - ID del pedido
     * @return Número de items en el pedido
     */
    @Query("SELECT COUNT(d) FROM DetallePedidoEntity d WHERE d.pedido.idPedido = :pedidoId")
    Long countByPedidoId(@Param("pedidoId") Integer pedidoId);
    
    /**
     * Obtiene los productos más vendidos (Top N)
     * @param limit - Número de productos a obtener
     * @return Lista de productos más vendidos
     */
    @Query("SELECT d.producto.idProducto, d.producto.modelo, SUM(d.cantidad) as totalVendido " +
           "FROM DetallePedidoEntity d " +
           "GROUP BY d.producto.idProducto, d.producto.modelo " +
           "ORDER BY totalVendido DESC")
    List<Object[]> findProductosMasVendidos();
    
    /**
     * Verifica si un producto está siendo usado en algún detalle
     * @param productoId - ID del producto
     * @return true si el producto está en uso
     */
    boolean existsByProductoIdProducto(Integer productoId);
    
    /**
     * Elimina todos los detalles de un pedido específico
     * @param pedidoId - ID del pedido
     */
    void deleteByPedidoIdPedido(Integer pedidoId);
}
