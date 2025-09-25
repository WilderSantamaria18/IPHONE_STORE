package com.idat.continua2.demo.Impl;

import com.idat.continua2.demo.Repository.ProveedorRepository;
import com.idat.continua2.demo.Service.ProveedorService;
import com.idat.continua2.demo.model.ProveedorEntity;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

/**
 * Implementación del servicio para gestión de Proveedores
 * Proporciona operaciones CRUD y lógica de negocio
 * Integra con ProveedorRepository para persistencia
 */
@Service
@Transactional
public class ProveedorServiceImpl implements ProveedorService {

    @Autowired
    private ProveedorRepository proveedorRepository;

    @Override
    @Transactional(readOnly = true)
    public List<ProveedorEntity> findAll() {
        return proveedorRepository.findAll();
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<ProveedorEntity> findById(Integer id) {
        return proveedorRepository.findById(id);
    }

    @Override
    public ProveedorEntity add(ProveedorEntity proveedor) {
        // Establecer estado activo por defecto
        if (proveedor.getEstado() == null) {
            proveedor.setEstado(ProveedorEntity.EstadoProveedor.Activo);
        }
        return proveedorRepository.save(proveedor);
    }

    @Override
    public ProveedorEntity update(ProveedorEntity proveedor) {
        return proveedorRepository.save(proveedor);
    }

    @Override
    public ProveedorEntity delete(ProveedorEntity proveedor) {
        // Soft delete - cambiar estado a Inactivo
        proveedor.setEstado(ProveedorEntity.EstadoProveedor.Inactivo);
        return proveedorRepository.save(proveedor);
    }

    @Override
    public void cambiarEstado(Integer id, ProveedorEntity.EstadoProveedor estado) {
        Optional<ProveedorEntity> proveedorOpt = proveedorRepository.findById(id);
        if (proveedorOpt.isPresent()) {
            ProveedorEntity proveedor = proveedorOpt.get();
            proveedor.setEstado(estado);
            proveedorRepository.save(proveedor);
        }
    }

    @Override
    @Transactional(readOnly = true)
    public List<ProveedorEntity> findByEstado(ProveedorEntity.EstadoProveedor estado) {
        return proveedorRepository.findByEstado(estado);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<ProveedorEntity> findByRuc(String ruc) {
        return proveedorRepository.findByRuc(ruc);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<ProveedorEntity> findByEmail(String email) {
        return proveedorRepository.findByEmail(email);
    }

    @Override
    @Transactional(readOnly = true)
    public List<ProveedorEntity> findByNombre(String nombre) {
        return proveedorRepository.findByNombreContainingIgnoreCase(nombre);
    }

    @Override
    @Transactional(readOnly = true)
    public List<ProveedorEntity> findByTelefono(String telefono) {
        // Implementación usando consulta personalizada o devolver lista vacía
        return proveedorRepository.findAll().stream()
                .filter(p -> p.getTelefono() != null && p.getTelefono().contains(telefono))
                .toList();
    }

    @Override
    @Transactional(readOnly = true)
    public Long countProveedoresActivos() {
        return proveedorRepository.countProveedoresActivos();
    }

    @Override
    public List<ProveedorEntity> findAllActivos() {
        return proveedorRepository.findByEstado(ProveedorEntity.EstadoProveedor.Activo);
    }
}