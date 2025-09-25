package com.idat.continua2.demo.Repository;

import com.idat.continua2.demo.model.UsuarioEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

/**
 * Repositorio para entidad Usuario
 * Extiende JpaRepository con Integer como tipo de ID
 * Incluye consultas para autenticación y gestión de usuarios
 * ✅ OPTIMIZADO: JOIN FETCH para eliminar N+1 queries
 */
@Repository
public interface UsuarioRepository extends JpaRepository<UsuarioEntity, Integer> {

    /**
     * ✅ OPTIMIZADO: Busca usuario por username con cargo (para autenticación)
     * @param username - Nombre de usuario
     * @return Optional con el usuario encontrado
     */
    @Query("SELECT u FROM UsuarioEntity u JOIN FETCH u.cargo WHERE u.username = :username")
    Optional<UsuarioEntity> findByUsernameWithCargo(@Param("username") String username);
    
    /**
     * Busca usuario por username (para autenticación)
     * @param username - Nombre de usuario
     * @return Optional con el usuario encontrado
     */
    Optional<UsuarioEntity> findByUsername(String username);
    
    /**
     * Busca usuario por email
     * @param email - Email del usuario
     * @return Optional con el usuario encontrado
     */
    Optional<UsuarioEntity> findByEmail(String email);
    
    /**
     * Busca usuario por DNI
     * @param dni - DNI del usuario
     * @return Optional con el usuario encontrado
     */
    Optional<UsuarioEntity> findByDni(String dni);

    /**
     * ✅ OPTIMIZADO: Busca todos los usuarios con sus cargos
     * @return Lista de usuarios con cargos optimizada
     */
    @Query("SELECT u FROM UsuarioEntity u JOIN FETCH u.cargo ORDER BY u.nombres")
    List<UsuarioEntity> findAllWithCargos();

    /**
     * ✅ OPTIMIZADO: Busca usuarios por cargo con JOIN FETCH
     * @param cargoId - ID del cargo
     * @return Lista de usuarios del cargo especificado
     */
    @Query("SELECT u FROM UsuarioEntity u JOIN FETCH u.cargo WHERE u.cargo.idCargo = :cargoId")
    List<UsuarioEntity> findByCargoIdWithCargo(@Param("cargoId") Integer cargoId);

    /**
     * Busca usuarios por cargo (sin optimización)
     * @param cargoId - ID del cargo
     * @return Lista de usuarios del cargo especificado
     */
    @Query("SELECT u FROM UsuarioEntity u WHERE u.cargo.idCargo = :cargoId")
    List<UsuarioEntity> findByCargoId(@Param("cargoId") Integer cargoId);

    /**
     * ✅ OPTIMIZADO: Busca usuarios activos con sus cargos
     * @return Lista de usuarios activos con cargos
     */
    @Query("SELECT u FROM UsuarioEntity u JOIN FETCH u.cargo WHERE u.estado = 'Activo' ORDER BY u.nombres")
    List<UsuarioEntity> findUsuariosActivosWithCargos();

    /**
     * Busca usuarios activos (sin optimización)
     * @return Lista de usuarios activos
     */
    @Query("SELECT u FROM UsuarioEntity u WHERE u.estado = 'Activo'")
    List<UsuarioEntity> findUsuariosActivos();
    
    /**
     * ✅ OPTIMIZADO: Busca usuarios por estado con sus cargos
     * @param estado - Estado del usuario (Activo/Inactivo)
     * @return Lista de usuarios con el estado especificado
     */
    @Query("SELECT u FROM UsuarioEntity u JOIN FETCH u.cargo WHERE u.estado = :estado ORDER BY u.nombres")
    List<UsuarioEntity> findByEstadoWithCargos(@Param("estado") UsuarioEntity.EstadoUsuario estado);
    
    /**
     * Busca usuarios por estado (sin optimización)
     * @param estado - Estado del usuario (Activo/Inactivo)
     * @return Lista de usuarios con el estado especificado
     */
    List<UsuarioEntity> findByEstado(UsuarioEntity.EstadoUsuario estado);
}