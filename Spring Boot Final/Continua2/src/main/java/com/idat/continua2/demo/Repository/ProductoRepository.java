package com.idat.continua2.demo.Repository;

import com.idat.continua2.demo.model.ProductoEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.util.List;

/**
 * Repositorio para entidad Producto
 * Extiende JpaRepository con Integer como tipo de ID
 * Incluye consultas personalizadas con JPQL
 */
@Repository
public interface ProductoRepository extends JpaRepository<ProductoEntity, Integer> {

    /**
     * Busca productos por estado
     * @param estado - Estado del producto (Activo/Inactivo)
     * @return Lista de productos con el estado especificado
     */
    List<ProductoEntity> findByEstado(ProductoEntity.EstadoProducto estado);

    /**
     * Busca productos por proveedor
     * @param proveedorId - ID del proveedor
     * @return Lista de productos del proveedor
     */
    @Query("SELECT p FROM ProductoEntity p WHERE p.proveedor.idProveedor = :proveedorId")
    List<ProductoEntity> findByProveedorId(@Param("proveedorId") Integer proveedorId);

    /**
     * Busca productos activos
     * @return Lista de productos activos
     */
    @Query("SELECT p FROM ProductoEntity p WHERE p.estado = 'Activo'")
    List<ProductoEntity> findProductosActivos();

    /**
     * Busca productos por modelo (ignorando mayúsculas/minúsculas)
     * @param modelo - Modelo a buscar
     * @return Lista de productos que contengan el modelo
     */
    @Query("SELECT p FROM ProductoEntity p WHERE LOWER(p.modelo) LIKE LOWER(CONCAT('%', :modelo, '%'))")
    List<ProductoEntity> findByModeloContainingIgnoreCase(@Param("modelo") String modelo);
    
    /**
     * Busca productos por rango de precio
     * @param precioMin - Precio mínimo
     * @param precioMax - Precio máximo
     * @return Lista de productos en el rango de precio
     */
    @Query("SELECT p FROM ProductoEntity p WHERE p.precioVenta BETWEEN :precioMin AND :precioMax")
    List<ProductoEntity> findByPrecioVentaBetween(@Param("precioMin") BigDecimal precioMin, @Param("precioMax") BigDecimal precioMax);
    
    /**
     * Busca productos con stock bajo
     * @param stockMinimo - Stock mínimo
     * @return Lista de productos con stock menor al mínimo
     */
    @Query("SELECT p FROM ProductoEntity p WHERE p.stock < :stockMinimo AND p.estado = 'Activo'")
    List<ProductoEntity> findProductosConStockBajo(@Param("stockMinimo") Integer stockMinimo);
    
    /**
     * Busca productos por año de lanzamiento
     * @param año - Año de lanzamiento
     * @return Lista de productos del año especificado
     */
    List<ProductoEntity> findByLanzamiento(String año);
    
    /**
     * Cuenta productos activos
     * @return Número de productos activos
     */
    @Query("SELECT COUNT(p) FROM ProductoEntity p WHERE p.estado = 'Activo'")
    Long countProductosActivos();
}