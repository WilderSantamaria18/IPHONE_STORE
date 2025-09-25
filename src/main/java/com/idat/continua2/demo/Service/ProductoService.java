package com.idat.continua2.demo.Service;

import com.idat.continua2.demo.model.ProductoEntity;
import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

/**
 * Interfaz de servicio para gestión de Productos
 * Define operaciones CRUD y consultas específicas de negocio
 * Incluye gestión de stock, precios y categorías
 */
public interface ProductoService {
    
    /**
     * Obtiene todos los productos
     * @return Lista de todos los productos
     */
    List<ProductoEntity> findAll();
    
    /**
     * Busca un producto por ID
     * @param id - ID del producto
     * @return Optional con el producto encontrado
     */
    Optional<ProductoEntity> findById(Integer id);
    
    /**
     * Crea un nuevo producto
     * @param producto - Datos del producto a crear
     * @return Producto creado
     */
    ProductoEntity add(ProductoEntity producto);
    
    /**
     * Actualiza un producto existente
     * @param producto - Datos del producto a actualizar
     * @return Producto actualizado
     */
    ProductoEntity update(ProductoEntity producto);
    
    /**
     * Elimina un producto (soft delete cambiando estado)
     * @param producto - Producto a eliminar
     * @return Producto eliminado
     */
    ProductoEntity delete(ProductoEntity producto);
    
    /**
     * Cambia el estado de un producto
     * @param id - ID del producto
     * @param estado - Nuevo estado del producto
     */
    void cambiarEstado(Integer id, ProductoEntity.EstadoProducto estado);
    
    /**
     * Busca productos por estado
     * @param estado - Estado a buscar (Activo/Inactivo)
     * @return Lista de productos con el estado especificado
     */
    List<ProductoEntity> findByEstado(ProductoEntity.EstadoProducto estado);
    
    /**
     * Busca productos por nombre (búsqueda parcial)
     * @param nombre - Nombre a buscar
     * @return Lista de productos que contengan el nombre
     */
    List<ProductoEntity> findByNombre(String nombre);
    
    /**
     * Busca productos por modelo (búsqueda parcial)
     * @param modelo - Modelo a buscar
     * @return Lista de productos que contengan el modelo
     */
    List<ProductoEntity> findByModelo(String modelo);
    
    /**
     * Busca productos en un rango de precios
     * @param precioMin - Precio mínimo
     * @param precioMax - Precio máximo
     * @return Lista de productos en el rango
     */
    List<ProductoEntity> findByPrecioBetween(BigDecimal precioMin, BigDecimal precioMax);
    
    /**
     * Busca productos con stock disponible
     * @return Lista de productos con stock > 0
     */
    List<ProductoEntity> findWithStock();
    
    /**
     * Actualiza el stock de un producto
     * @param id - ID del producto
     * @param cantidad - Nueva cantidad de stock
     */
    void actualizarStock(Integer id, Integer cantidad);
    
    /**
     * Reduce el stock por venta
     * @param id - ID del producto
     * @param cantidad - Cantidad vendida
     * @throws IllegalArgumentException si no hay stock suficiente
     */
    void reducirStock(Integer id, Integer cantidad);
    
    /**
     * Busca productos con stock bajo (menos de 10 unidades)
     * @return Lista de productos con stock bajo
     */
    List<ProductoEntity> findProductosStockBajo();
    
    /**
     * Cuenta el número de productos activos
     * @return Número de productos activos
     */
    Long countProductosActivos();

    /**
     * Incrementa el stock (ej. al anular un pedido)
     * @param id ID del producto
     * @param cantidad Cantidad a sumar (debe ser > 0)
     */
    void aumentarStock(Integer id, Integer cantidad);

}