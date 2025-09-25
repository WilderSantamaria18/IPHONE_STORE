package com.idat.continua2.demo.Repository;

import com.idat.continua2.demo.model.CargoEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface CargoRepository extends JpaRepository<CargoEntity, Integer> {

    // Buscar por nombre exacto
    Optional<CargoEntity> findByNombre(String nombre);

    // Buscar por nombre parcial (case insensitive)
    List<CargoEntity> findByNombreContainingIgnoreCase(String nombre);

    // Obtener todos los cargos ordenados por nombre
    @Query("SELECT c FROM CargoEntity c ORDER BY c.nombre")
    List<CargoEntity> findAllOrderByNombre();
}