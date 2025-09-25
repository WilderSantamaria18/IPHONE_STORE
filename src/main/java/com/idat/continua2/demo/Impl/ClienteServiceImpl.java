package com.idat.continua2.demo.Impl;

import com.idat.continua2.demo.Repository.ClienteRepository;
import com.idat.continua2.demo.Service.ClienteService;
import com.idat.continua2.demo.model.ClienteEntity;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

/**
 * Implementación del servicio para gestión de Clientes
 * Proporciona operaciones CRUD y lógica de negocio
 * Integra con ClienteRepository para persistencia
 */
@Service
@Transactional
public class ClienteServiceImpl implements ClienteService {

    @Autowired
    private ClienteRepository clienteRepository;

    @Override
    @Transactional(readOnly = true)
    public List<ClienteEntity> findAll() {
        return clienteRepository.findAll();
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<ClienteEntity> findById(Integer id) {
        return clienteRepository.findById(id);
    }

    @Override
    public ClienteEntity add(ClienteEntity cliente) {
        // Establecer estado activo por defecto
        if (cliente.getEstado() == null) {
            cliente.setEstado(ClienteEntity.EstadoCliente.Activo);
        }
        return clienteRepository.save(cliente);
    }

    @Override
    public ClienteEntity update(ClienteEntity cliente) {
        return clienteRepository.save(cliente);
    }

    @Override
    public ClienteEntity delete(ClienteEntity cliente) {
        // Soft delete - cambiar estado a Inactivo
        cliente.setEstado(ClienteEntity.EstadoCliente.Inactivo);
        return clienteRepository.save(cliente);
    }

    @Override
    public void cambiarEstado(Integer id, ClienteEntity.EstadoCliente estado) {
        Optional<ClienteEntity> clienteOpt = clienteRepository.findById(id);
        if (clienteOpt.isPresent()) {
            ClienteEntity cliente = clienteOpt.get();
            cliente.setEstado(estado);
            clienteRepository.save(cliente);
        }
    }

    @Override
    @Transactional(readOnly = true)
    public List<ClienteEntity> findByEstado(ClienteEntity.EstadoCliente estado) {
        return clienteRepository.findByEstado(estado);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<ClienteEntity> findByDocumento(String documento) {
        return clienteRepository.findByDocumento(documento);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<ClienteEntity> findByEmail(String email) {
        return clienteRepository.findByEmail(email);
    }

    @Override
    @Transactional(readOnly = true)
    public List<ClienteEntity> findByNombre(String nombre) {
        return clienteRepository.findByNombreContainingIgnoreCase(nombre);
    }

    @Override
    @Transactional(readOnly = true)
    public Long countClientesActivos() {
        return clienteRepository.countClientesActivos();
    }
}