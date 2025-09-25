package com.idat.continua2.demo.Service;

import com.idat.continua2.demo.model.ClienteEntity;
import java.util.List;
import java.util.Optional;

/**
 * Interfaz de servicio para gestión de Clientes
 * Define operaciones CRUD y consultas de negocio
 * Siguiendo principios SOLID y Spring Data JPA
 */
public interface ClienteService {
    
    /**
     * Obtiene todos los clientes
     * @return Lista de todos los clientes
     */
    List<ClienteEntity> findAll();
    
    /**
     * Busca un cliente por ID
     * @param id - ID del cliente
     * @return Optional con el cliente encontrado
     */
    Optional<ClienteEntity> findById(Integer id);
    
    /**
     * Crea un nuevo cliente
     * @param cliente - Datos del cliente a crear
     * @return Cliente creado
     */
    ClienteEntity add(ClienteEntity cliente);
    
    /**
     * Actualiza un cliente existente
     * @param cliente - Datos del cliente a actualizar
     * @return Cliente actualizado
     */
    ClienteEntity update(ClienteEntity cliente);
    
    /**
     * Elimina un cliente (soft delete cambiando estado)
     * @param cliente - Cliente a eliminar
     * @return Cliente eliminado
     */
    ClienteEntity delete(ClienteEntity cliente);
    
    /**
     * Cambia el estado de un cliente
     * @param id - ID del cliente
     * @param estado - Nuevo estado del cliente
     */
    void cambiarEstado(Integer id, ClienteEntity.EstadoCliente estado);
    
    /**
     * Busca clientes por estado
     * @param estado - Estado a buscar (Activo/Inactivo)
     * @return Lista de clientes con el estado especificado
     */
    List<ClienteEntity> findByEstado(ClienteEntity.EstadoCliente estado);
    
    /**
     * Busca cliente por documento
     * @param documento - Documento del cliente
     * @return Optional con el cliente encontrado
     */
    Optional<ClienteEntity> findByDocumento(String documento);
    
    /**
     * Busca cliente por email
     * @param email - Email del cliente
     * @return Optional con el cliente encontrado
     */
    Optional<ClienteEntity> findByEmail(String email);
    
    /**
     * Busca clientes por nombre (búsqueda parcial)
     * @param nombre - Nombre a buscar
     * @return Lista de clientes que contengan el nombre
     */
    List<ClienteEntity> findByNombre(String nombre);
    
    /**
     * Cuenta el número de clientes activos
     * @return Número de clientes activos
     */
    Long countClientesActivos();
}