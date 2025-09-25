package com.idat.continua2.demo.Repository;

import com.idat.continua2.demo.model.DetalleComprobanteEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.util.List;

/**
 * Repositorio para entidad DetalleComprobante
 * Extiende JpaRepository con Integer como tipo de ID
 * Incluye consultas personalizadas optimizadas
 */
@Repository
public interface DetalleComprobanteRepository extends JpaRepository<DetalleComprobanteEntity, Integer> {
    
    /**
     * ✅ OPTIMIZADO: Busca detalles por comprobante con relaciones
     * @param comprobanteId - ID del comprobante
     * @return Lista de detalles con comprobante y producto optimizada
     */
    @Query("SELECT dc FROM DetalleComprobanteEntity dc " +
           "JOIN FETCH dc.comprobante c " +
           "JOIN FETCH dc.producto p " +
           "JOIN FETCH p.proveedor " +
           "WHERE c.idComprobante = :comprobanteId " +
           "ORDER BY dc.idDetalleComprobante")
    List<DetalleComprobanteEntity> findByComprobanteIdWithRelations(@Param("comprobanteId") Integer comprobanteId);
    
    /**
     * ✅ OPTIMIZADO: Busca todos los detalles con relaciones
     * @return Lista de todos los detalles con comprobante y producto optimizada
     */
    @Query("SELECT dc FROM DetalleComprobanteEntity dc " +
           "JOIN FETCH dc.comprobante c " +
           "JOIN FETCH dc.producto p " +
           "JOIN FETCH p.proveedor " +
           "ORDER BY c.fechaEmision DESC, dc.idDetalleComprobante")
    List<DetalleComprobanteEntity> findAllWithRelations();
    
    /**
     * Busca detalles por producto
     * @param productoId - ID del producto
     * @return Lista de detalles del producto
     */
    @Query("SELECT dc FROM DetalleComprobanteEntity dc " +
           "JOIN FETCH dc.comprobante c " +
           "JOIN FETCH dc.producto p " +
           "WHERE p.idProducto = :productoId " +
           "ORDER BY c.fechaEmision DESC")
    List<DetalleComprobanteEntity> findByProductoIdWithRelations(@Param("productoId") Integer productoId);
    
    /**
     * Calcula el total de un comprobante basado en sus detalles
     * @param comprobanteId - ID del comprobante
     * @return Total calculado
     */
    @Query("SELECT COALESCE(SUM(dc.cantidad * dc.precioUnitario), 0) FROM DetalleComprobanteEntity dc WHERE dc.comprobante.idComprobante = :comprobanteId")
    BigDecimal calculateTotalByComprobanteId(@Param("comprobanteId") Integer comprobanteId);
    
    /**
     * Cuenta la cantidad de items en un comprobante
     * @param comprobanteId - ID del comprobante
     * @return Número de items
     */
    @Query("SELECT COUNT(dc) FROM DetalleComprobanteEntity dc WHERE dc.comprobante.idComprobante = :comprobanteId")
    Long countByComprobanteId(@Param("comprobanteId") Integer comprobanteId);
    
    /**
     * Obtiene la cantidad total vendida de un producto
     * @param productoId - ID del producto
     * @return Cantidad total vendida
     */
    @Query("SELECT COALESCE(SUM(dc.cantidad), 0) FROM DetalleComprobanteEntity dc WHERE dc.producto.idProducto = :productoId")
    Integer getTotalCantidadVendidaByProducto(@Param("productoId") Integer productoId);
    
    /**
     * Obtiene el ingreso total por producto
     * @param productoId - ID del producto
     * @return Ingreso total del producto
     */
    @Query("SELECT COALESCE(SUM(dc.cantidad * dc.precioUnitario), 0) FROM DetalleComprobanteEntity dc WHERE dc.producto.idProducto = :productoId")
    BigDecimal getTotalIngresosByProducto(@Param("productoId") Integer productoId);
    
    /**
     * Busca los productos más vendidos
     * @param limit - Número de productos a retornar
     * @return Lista de productos más vendidos
     */
    @Query("SELECT dc.producto, SUM(dc.cantidad) as totalVendido " +
           "FROM DetalleComprobanteEntity dc " +
           "GROUP BY dc.producto " +
           "ORDER BY totalVendido DESC " +
           "LIMIT :limit")
    List<Object[]> findTopProductosMasVendidos(@Param("limit") Integer limit);
    
    /**
     * Busca los productos que más ingresos han generado
     * @param limit - Número de productos a retornar
     * @return Lista de productos con mayores ingresos
     */
    @Query("SELECT dc.producto, SUM(dc.cantidad * dc.precioUnitario) as totalIngresos " +
           "FROM DetalleComprobanteEntity dc " +
           "GROUP BY dc.producto " +
           "ORDER BY totalIngresos DESC " +
           "LIMIT :limit")
    List<Object[]> findTopProductosMayorIngreso(@Param("limit") Integer limit);
    
    /**
     * Elimina todos los detalles de un comprobante
     * @param comprobanteId - ID del comprobante
     */
    @Query("DELETE FROM DetalleComprobanteEntity dc WHERE dc.comprobante.idComprobante = :comprobanteId")
    void deleteByComprobanteId(@Param("comprobanteId") Integer comprobanteId);
    
    /**
     * Verifica si un comprobante tiene detalles
     * @param comprobanteId - ID del comprobante
     * @return true si tiene detalles, false si no
     */
    @Query("SELECT COUNT(dc) > 0 FROM DetalleComprobanteEntity dc WHERE dc.comprobante.idComprobante = :comprobanteId")
    boolean existsByComprobanteId(@Param("comprobanteId") Integer comprobanteId);
}
