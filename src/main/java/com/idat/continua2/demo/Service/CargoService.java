package com.idat.continua2.demo.Service;

import com.idat.continua2.demo.model.CargoEntity;
import java.util.List;
import java.util.Optional;

/**
 * Interfaz de servicio para gestión de Cargos
 * Define operaciones CRUD básicas para la entidad Cargo
 */
public interface CargoService {
    
    /**
     * Obtiene todos los cargos
     * @return Lista de todos los cargos
     */
    List<CargoEntity> findAll();
    
    /**
     * Busca un cargo por ID
     * @param id - ID del cargo
     * @return Optional con el cargo encontrado
     */
    Optional<CargoEntity> findById(Integer id);
    
    /**
     * Crea un nuevo cargo
     * @param cargo - Datos del cargo a crear
     * @return Cargo creado
     */
    CargoEntity add(CargoEntity cargo);
    
    /**
     * Actualiza un cargo existente
     * @param cargo - Datos del cargo a actualizar
     * @return Cargo actualizado
     */
    CargoEntity update(CargoEntity cargo);
    
    /**
     * Elimina un cargo por ID
     * @param id - ID del cargo a eliminar
     */
    void delete(Integer id);
    
    /**
     * Busca cargo por nombre
     * @param nombre - Nombre del cargo
     * @return Optional con el cargo encontrado
     */
    Optional<CargoEntity> findByNombre(String nombre);
    
    /**
     * Cuenta el número total de cargos
     * @return Número total de cargos
     */
    Long countAll();
}
