package com.idat.continua2.demo.Repository;

import com.idat.continua2.demo.model.ClienteEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

/**
 * Repositorio para entidad Cliente
 * Extiende JpaRepository con Integer como tipo de ID
 * Incluye consultas personalizadas con JPQL
 */
@Repository
public interface ClienteRepository extends JpaRepository<ClienteEntity, Integer> {
    
    /**
     * Busca clientes por estado
     * @param estado - Estado del cliente (Activo/Inactivo)
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
     * Busca clientes por tipo
     * @param tipoCliente - Tipo de cliente (Natural/Juridica)
     * @return Lista de clientes del tipo especificado
     */
    List<ClienteEntity> findByTipoCliente(String tipoCliente);
    
    /**
     * Busca clientes por nombre (ignorando mayúsculas/minúsculas)
     * @param nombre - Nombre a buscar
     * @return Lista de clientes que contengan el nombre
     */
    @Query("SELECT c FROM ClienteEntity c WHERE LOWER(c.nombre) LIKE LOWER(CONCAT('%', :nombre, '%'))")
    List<ClienteEntity> findByNombreContainingIgnoreCase(@Param("nombre") String nombre);
    
    /**
     * Cuenta clientes activos
     * @return Número de clientes activos
     */
    @Query("SELECT COUNT(c) FROM ClienteEntity c WHERE c.estado = 'Activo'")
    Long countClientesActivos();
}