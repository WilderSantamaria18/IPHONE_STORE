package com.idat.continua2.demo.Service;

import com.idat.continua2.demo.model.UsuarioEntity;
import java.util.List;
import java.util.Optional;

/**
 * Interfaz de servicio para gestión de Usuarios
 * Define operaciones CRUD y consultas de negocio
 * Siguiendo principios SOLID y Spring Data JPA
 */
public interface UsuarioService {
    
    /**
     * ✅ OPTIMIZADO: Obtiene todos los usuarios con sus cargos
     * @return Lista de todos los usuarios con cargos optimizada
     */
    List<UsuarioEntity> findAllWithCargos();
    
    /**
     * Obtiene todos los usuarios
     * @return Lista de todos los usuarios
     */
    List<UsuarioEntity> findAll();
    
    /**
     * Busca un usuario por ID
     * @param id - ID del usuario
     * @return Optional con el usuario encontrado
     */
    Optional<UsuarioEntity> findById(Integer id);
    
    /**
     * Crea un nuevo usuario
     * @param usuario - Datos del usuario a crear
     * @return Usuario creado
     */
    UsuarioEntity add(UsuarioEntity usuario);
    
    /**
     * Actualiza un usuario existente
     * @param usuario - Datos del usuario a actualizar
     * @return Usuario actualizado
     */
    UsuarioEntity update(UsuarioEntity usuario);
    
    /**
     * Elimina un usuario (soft delete cambiando estado)
     * @param usuario - Usuario a eliminar
     * @return Usuario eliminado
     */
    UsuarioEntity delete(UsuarioEntity usuario);
    
    /**
     * Cambia el estado de un usuario
     * @param id - ID del usuario
     * @param estado - Nuevo estado del usuario
     */
    void cambiarEstado(Integer id, UsuarioEntity.EstadoUsuario estado);
    
    /**
     * ✅ OPTIMIZADO: Busca usuarios por estado con sus cargos
     * @param estado - Estado del usuario (Activo/Inactivo)
     * @return Lista de usuarios con el estado especificado
     */
    List<UsuarioEntity> findByEstadoWithCargos(UsuarioEntity.EstadoUsuario estado);
    
    /**
     * Busca usuarios por estado
     * @param estado - Estado del usuario (Activo/Inactivo)
     * @return Lista de usuarios con el estado especificado
     */
    List<UsuarioEntity> findByEstado(UsuarioEntity.EstadoUsuario estado);
    
    /**
     * ✅ OPTIMIZADO: Busca usuario por username con cargo
     * @param username - Nombre de usuario
     * @return Optional con el usuario encontrado
     */
    Optional<UsuarioEntity> findByUsernameWithCargo(String username);
    
    /**
     * Busca usuario por username
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
     * ✅ OPTIMIZADO: Busca usuarios por cargo con JOIN FETCH
     * @param cargoId - ID del cargo
     * @return Lista de usuarios del cargo especificado
     */
    List<UsuarioEntity> findByCargoIdWithCargo(Integer cargoId);
    
    /**
     * Busca usuarios por cargo
     * @param cargoId - ID del cargo
     * @return Lista de usuarios del cargo especificado
     */
    List<UsuarioEntity> findByCargoId(Integer cargoId);
}
