package com.idat.continua2.demo.Service;

import com.idat.continua2.demo.model.DetalleComprobanteEntity;
import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

/**
 * Interfaz de servicio para gestión de Detalles de Comprobante
 * Define operaciones CRUD y consultas de negocio
 * Siguiendo principios SOLID y Spring Data JPA
 */
public interface DetalleComprobanteService {
    
    /**
     * ✅ OPTIMIZADO: Obtiene todos los detalles con sus relaciones
     * @return Lista de todos los detalles con comprobante y producto optimizada
     */
    List<DetalleComprobanteEntity> findAllWithRelations();
    
    /**
     * Obtiene todos los detalles (sin optimización)
     * @return Lista de todos los detalles
     */
    List<DetalleComprobanteEntity> findAll();
    
    /**
     * Busca un detalle por ID
     * @param id - ID del detalle
     * @return Optional con el detalle encontrado
     */
    Optional<DetalleComprobanteEntity> findById(Integer id);
    
    /**
     * Crea un nuevo detalle de comprobante
     * @param detalle - Datos del detalle a crear
     * @return Detalle creado
     */
    DetalleComprobanteEntity add(DetalleComprobanteEntity detalle);
    
    /**
     * Actualiza un detalle existente
     * @param detalle - Datos del detalle a actualizar
     * @return Detalle actualizado
     */
    DetalleComprobanteEntity update(DetalleComprobanteEntity detalle);
    
    /**
     * Elimina un detalle por ID
     * @param id - ID del detalle a eliminar
     */
    void delete(Integer id);
    
    /**
     * ✅ OPTIMIZADO: Busca detalles por comprobante con relaciones
     * @param comprobanteId - ID del comprobante
     * @return Lista de detalles con comprobante y producto optimizada
     */
    List<DetalleComprobanteEntity> findByComprobanteIdWithRelations(Integer comprobanteId);
    
    /**
     * Busca detalles por producto
     * @param productoId - ID del producto
     * @return Lista de detalles del producto
     */
    List<DetalleComprobanteEntity> findByProductoIdWithRelations(Integer productoId);
    
    /**
     * Calcula el total de un comprobante basado en sus detalles
     * @param comprobanteId - ID del comprobante
     * @return Total calculado
     */
    BigDecimal calculateTotalByComprobanteId(Integer comprobanteId);
    
    /**
     * Cuenta la cantidad de items en un comprobante
     * @param comprobanteId - ID del comprobante
     * @return Número de items
     */
    Long countByComprobanteId(Integer comprobanteId);
    
    /**
     * Obtiene la cantidad total vendida de un producto
     * @param productoId - ID del producto
     * @return Cantidad total vendida
     */
    Integer getTotalCantidadVendidaByProducto(Integer productoId);
    
    /**
     * Obtiene el ingreso total por producto
     * @param productoId - ID del producto
     * @return Ingreso total del producto
     */
    BigDecimal getTotalIngresosByProducto(Integer productoId);
    
    /**
     * Busca los productos más vendidos
     * @param limit - Número de productos a retornar
     * @return Lista de productos más vendidos
     */
    List<Object[]> findTopProductosMasVendidos(Integer limit);
    
    /**
     * Busca los productos que más ingresos han generado
     * @param limit - Número de productos a retornar
     * @return Lista de productos con mayores ingresos
     */
    List<Object[]> findTopProductosMayorIngreso(Integer limit);
    
    /**
     * Elimina todos los detalles de un comprobante
     * @param comprobanteId - ID del comprobante
     */
    void deleteByComprobanteId(Integer comprobanteId);
    
    /**
     * Verifica si un comprobante tiene detalles
     * @param comprobanteId - ID del comprobante
     * @return true si tiene detalles, false si no
     */
    boolean existsByComprobanteId(Integer comprobanteId);
    
    /**
     * Crea múltiples detalles de comprobante
     * @param detalles - Lista de detalles a crear
     * @return Lista de detalles creados
     */
    List<DetalleComprobanteEntity> addAll(List<DetalleComprobanteEntity> detalles);
    
    /**
     * Actualiza el stock de productos basado en los detalles del comprobante
     * @param comprobanteId - ID del comprobante
     */
    void actualizarStockProductos(Integer comprobanteId);
}
