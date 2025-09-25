package com.idat.continua2.demo.Impl;

import com.idat.continua2.demo.Repository.UsuarioRepository;
import com.idat.continua2.demo.Service.UsuarioService;
import com.idat.continua2.demo.model.UsuarioEntity;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

/**
 * Implementación del servicio para gestión de Usuarios
 * Proporciona operaciones CRUD y lógica de negocio
 * Integra con UsuarioRepository para persistencia
 * ✅ OPTIMIZADO: Métodos con JOIN FETCH para eliminar N+1 queries
 */
@Service
@Transactional
public class UsuarioServiceImpl implements UsuarioService {

    @Autowired
    private UsuarioRepository usuarioRepository;
    
    @Autowired
    private PasswordEncoder passwordEncoder;

    @Override
    @Transactional(readOnly = true)
    public List<UsuarioEntity> findAllWithCargos() {
        return usuarioRepository.findAllWithCargos();
    }

    @Override
    @Transactional(readOnly = true)
    public List<UsuarioEntity> findAll() {
        return usuarioRepository.findAll();
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<UsuarioEntity> findById(Integer id) {
        return usuarioRepository.findById(id);
    }

    @Override
    public UsuarioEntity add(UsuarioEntity usuario) {
        // Establecer estado activo por defecto
        if (usuario.getEstado() == null) {
            usuario.setEstado(UsuarioEntity.EstadoUsuario.Activo);
        }
        
        // Encriptar la contraseña antes de guardar
        if (usuario.getContrasena() != null && !usuario.getContrasena().isEmpty()) {
            usuario.setContrasena(passwordEncoder.encode(usuario.getContrasena()));
        }
        
        return usuarioRepository.save(usuario);
    }

    @Override
    public UsuarioEntity update(UsuarioEntity usuario) {
        // Si se actualiza la contraseña, encriptarla
        if (usuario.getContrasena() != null && !usuario.getContrasena().isEmpty()) {
            // Verificar si la contraseña ya está encriptada (comienza con $2a$)
            if (!usuario.getContrasena().startsWith("$2a$")) {
                usuario.setContrasena(passwordEncoder.encode(usuario.getContrasena()));
            }
        }
        return usuarioRepository.save(usuario);
    }

    @Override
    public UsuarioEntity delete(UsuarioEntity usuario) {
        // Soft delete - cambiar estado a Inactivo
        usuario.setEstado(UsuarioEntity.EstadoUsuario.Inactivo);
        return usuarioRepository.save(usuario);
    }

    @Override
    public void cambiarEstado(Integer id, UsuarioEntity.EstadoUsuario estado) {
        Optional<UsuarioEntity> usuarioOpt = usuarioRepository.findById(id);
        if (usuarioOpt.isPresent()) {
            UsuarioEntity usuario = usuarioOpt.get();
            usuario.setEstado(estado);
            usuarioRepository.save(usuario);
        }
    }

    @Override
    @Transactional(readOnly = true)
    public List<UsuarioEntity> findByEstadoWithCargos(UsuarioEntity.EstadoUsuario estado) {
        return usuarioRepository.findByEstadoWithCargos(estado);
    }

    @Override
    @Transactional(readOnly = true)
    public List<UsuarioEntity> findByEstado(UsuarioEntity.EstadoUsuario estado) {
        return usuarioRepository.findByEstado(estado);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<UsuarioEntity> findByUsernameWithCargo(String username) {
        return usuarioRepository.findByUsernameWithCargo(username);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<UsuarioEntity> findByUsername(String username) {
        return usuarioRepository.findByUsername(username);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<UsuarioEntity> findByEmail(String email) {
        return usuarioRepository.findByEmail(email);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<UsuarioEntity> findByDni(String dni) {
        return usuarioRepository.findByDni(dni);
    }

    @Override
    @Transactional(readOnly = true)
    public List<UsuarioEntity> findByCargoIdWithCargo(Integer cargoId) {
        return usuarioRepository.findByCargoIdWithCargo(cargoId);
    }

    @Override
    @Transactional(readOnly = true)
    public List<UsuarioEntity> findByCargoId(Integer cargoId) {
        return usuarioRepository.findByCargoId(cargoId);
    }
}
