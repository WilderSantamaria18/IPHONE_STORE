package com.idat.continua2.demo.Repository;

import com.idat.continua2.demo.model.ProveedorEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

/**
 * Repositorio para entidad Proveedor
 * Extiende JpaRepository con Integer como tipo de ID
 * Incluye consultas personalizadas con JPQL
 */
@Repository
public interface ProveedorRepository extends JpaRepository<ProveedorEntity, Integer> {
    
    /**
     * Busca proveedores por estado
     * @param estado - Estado del proveedor (Activo/Inactivo)
     * @return Lista de proveedores con el estado especificado
     */
    List<ProveedorEntity> findByEstado(ProveedorEntity.EstadoProveedor estado);
    
    /**
     * Busca proveedor por RUC
     * @param ruc - RUC del proveedor
     * @return Optional con el proveedor encontrado
     */
    Optional<ProveedorEntity> findByRuc(String ruc);
    
    /**
     * Busca proveedor por email
     * @param email - Email del proveedor
     * @return Optional con el proveedor encontrado
     */
    Optional<ProveedorEntity> findByEmail(String email);
    
    /**
     * Busca proveedores por nombre (ignorando mayúsculas/minúsculas)
     * @param nombre - Nombre a buscar
     * @return Lista de proveedores que contengan el nombre
     */
    @Query("SELECT p FROM ProveedorEntity p WHERE LOWER(p.nombre) LIKE LOWER(CONCAT('%', :nombre, '%'))")
    List<ProveedorEntity> findByNombreContainingIgnoreCase(@Param("nombre") String nombre);
    
    /**
     * Cuenta proveedores activos
     * @return Número de proveedores activos
     */
    @Query("SELECT COUNT(p) FROM ProveedorEntity p WHERE p.estado = 'Activo'")
    Long countProveedoresActivos();
    
    /**
     * Busca proveedores que tienen productos
     * @return Lista de proveedores con productos asociados
     */
    @Query("SELECT DISTINCT p FROM ProveedorEntity p JOIN p.productos pr WHERE pr.estado = 'Activo'")
    List<ProveedorEntity> findProveedoresConProductos();
}