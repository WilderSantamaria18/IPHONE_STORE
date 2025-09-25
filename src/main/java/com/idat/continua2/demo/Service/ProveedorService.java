package com.idat.continua2.demo.Service;

import com.idat.continua2.demo.model.ProveedorEntity;
import java.util.List;
import java.util.Optional;

/**
 * Interfaz de servicio para gestión de Proveedores
 * Define operaciones CRUD y consultas de negocio
 * Incluye gestión de contactos y estados
 */
public interface ProveedorService {
    
    /**
     * Obtiene todos los proveedores
     * @return Lista de todos los proveedores
     */
    List<ProveedorEntity> findAll();
    
    /**
     * Busca un proveedor por ID
     * @param id - ID del proveedor
     * @return Optional con el proveedor encontrado
     */
    Optional<ProveedorEntity> findById(Integer id);
    
    /**
     * Crea un nuevo proveedor
     * @param proveedor - Datos del proveedor a crear
     * @return Proveedor creado
     */
    ProveedorEntity add(ProveedorEntity proveedor);
    
    /**
     * Actualiza un proveedor existente
     * @param proveedor - Datos del proveedor a actualizar
     * @return Proveedor actualizado
     */
    ProveedorEntity update(ProveedorEntity proveedor);
    
    /**
     * Elimina un proveedor (soft delete cambiando estado)
     * @param proveedor - Proveedor a eliminar
     * @return Proveedor eliminado
     */
    ProveedorEntity delete(ProveedorEntity proveedor);
    
    /**
     * Cambia el estado de un proveedor
     * @param id - ID del proveedor
     * @param estado - Nuevo estado del proveedor
     */
    void cambiarEstado(Integer id, ProveedorEntity.EstadoProveedor estado);
    
    /**
     * Busca proveedores por estado
     * @param estado - Estado a buscar (Activo/Inactivo)
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
     * Busca proveedores por nombre (búsqueda parcial)
     * @param nombre - Nombre a buscar
     * @return Lista de proveedores que contengan el nombre
     */
    List<ProveedorEntity> findByNombre(String nombre);
    
    /**
     * Busca proveedores por teléfono
     * @param telefono - Teléfono del proveedor
     * @return Lista de proveedores con el teléfono
     */
    List<ProveedorEntity> findByTelefono(String telefono);
    
    /**
     * Cuenta el número de proveedores activos
     * @return Número de proveedores activos
     */
    Long countProveedoresActivos();

    /**
     * Obtiene todos los proveedores activos
     * @return Lista de proveedores activos
     */
    List<ProveedorEntity> findAllActivos();
}