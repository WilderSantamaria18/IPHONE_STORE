package com.idat.continua2.demo.Controller;

import com.idat.continua2.demo.Service.UsuarioService;
import com.idat.continua2.demo.Service.CargoService;
import com.idat.continua2.demo.model.UsuarioEntity;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.Optional;

/**
 * Controlador MVC para gestión de Usuarios usando Thymeleaf
 * Implementa Spring Security con @PreAuthorize
 * Siguiendo principios Spring Boot MVC
 */
@Controller
@RequestMapping("/usuarios")
public class UsuarioController {

    @Autowired
    private UsuarioService usuarioService;
    
    @Autowired
    private CargoService cargoService;

    /**
     * ✅ OPTIMIZADO: Lista todos los usuarios activos con sus cargos
     * Requiere permiso USUARIO_READ
     */
    @GetMapping("/listar")
    @PreAuthorize("hasAuthority('USUARIO_READ')")
    public String listarUsuarios(Model model) {
        try {
            // Mostrar activos e inactivos (mismo estilo que otros listados)
            var activos = usuarioService.findByEstadoWithCargos(UsuarioEntity.EstadoUsuario.Activo);
            var inactivos = usuarioService.findByEstadoWithCargos(UsuarioEntity.EstadoUsuario.Inactivo);
            activos.addAll(inactivos); // concatenar
            model.addAttribute("usuarios", activos);
            return "usuario/listar";
        } catch (Exception e) {
            model.addAttribute("error", "Error al cargar usuarios: " + e.getMessage());
            return "error";
        }
    }

    /**
     * Muestra formulario para nuevo usuario
     * Requiere permiso USUARIO_CREATE
     */
    @GetMapping("/nuevo")
    @PreAuthorize("hasAuthority('USUARIO_CREATE')")
    public String nuevoUsuario(Model model) {
        try {
            model.addAttribute("usuario", new UsuarioEntity());
            model.addAttribute("cargos", cargoService.findAll());
            return "usuario/registrar";
        } catch (Exception e) {
            model.addAttribute("error", "Error al cargar formulario: " + e.getMessage());
            return "error";
        }
    }

    /**
     * Procesa el guardado de un nuevo usuario
     * Requiere permiso USUARIO_CREATE
     */
    @PostMapping("/guardar")
    @PreAuthorize("hasAuthority('USUARIO_CREATE')")
    public String guardarUsuario(@ModelAttribute("usuario") UsuarioEntity usuario, 
                               @RequestParam(value = "cargo.idCargo", required = false) Integer cargoId,
                               BindingResult result,
                               RedirectAttributes redirectAttributes) {
        if (result.hasErrors()) {
            return "usuario/registrar";
        }
        
        try {
            // Validar que no exista otro usuario con el mismo username, email o DNI
            if (usuarioService.findByUsername(usuario.getUsername()).isPresent()) {
                redirectAttributes.addFlashAttribute("error", "Ya existe un usuario con ese nombre de usuario");
                return "redirect:/usuarios/nuevo";
            }
            
            if (usuarioService.findByEmail(usuario.getEmail()).isPresent()) {
                redirectAttributes.addFlashAttribute("error", "Ya existe un usuario con ese email");
                return "redirect:/usuarios/nuevo";
            }
            
            if (usuarioService.findByDni(usuario.getDni()).isPresent()) {
                redirectAttributes.addFlashAttribute("error", "Ya existe un usuario con ese DNI");
                return "redirect:/usuarios/nuevo";
            }
            
            // Asignar el cargo al usuario
            if (cargoId != null) {
                cargoService.findById(cargoId).ifPresent(usuario::setCargo);
            }
            
            usuario.setEstado(UsuarioEntity.EstadoUsuario.Activo);
            usuarioService.add(usuario);
            redirectAttributes.addFlashAttribute("success", "Usuario creado exitosamente");
            return "redirect:/usuarios/listar";
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", "Error al crear usuario: " + e.getMessage());
            return "usuario/registrar";
        }
    }

    /**
     * Muestra formulario para editar usuario
     * Requiere permiso USUARIO_UPDATE
     */
    @GetMapping("/editar/{id}")
    @PreAuthorize("hasAuthority('USUARIO_UPDATE')")
    public String editarUsuario(@PathVariable Integer id, Model model) {
        try {
            Optional<UsuarioEntity> usuario = usuarioService.findById(id);
            if (usuario.isPresent()) {
                model.addAttribute("usuario", usuario.get());
                model.addAttribute("cargos", cargoService.findAll());
                return "usuario/editar";
            } else {
                model.addAttribute("error", "Usuario no encontrado");
                return "redirect:/usuarios/listar";
            }
        } catch (Exception e) {
            model.addAttribute("error", "Error al cargar usuario: " + e.getMessage());
            return "redirect:/usuarios/listar";
        }
    }

    /**
     * Procesa la actualización de un usuario
     * Requiere permiso USUARIO_UPDATE
     */
    @PostMapping("/editar/{id}")
    @PreAuthorize("hasAuthority('USUARIO_UPDATE')")
    public String actualizarUsuario(@PathVariable Integer id, 
                                  @ModelAttribute("usuario") UsuarioEntity usuario,
                                  @RequestParam(value = "cargo.idCargo", required = false) Integer cargoId,
                                  BindingResult result,
                                  RedirectAttributes redirectAttributes) {
        if (result.hasErrors()) {
            return "usuario/editar";
        }
        
        try {
            // Validar que no exista otro usuario con el mismo username, email o DNI (excepto el actual)
            Optional<UsuarioEntity> usuarioExistente;
            
            usuarioExistente = usuarioService.findByUsername(usuario.getUsername());
            if (usuarioExistente.isPresent() && !usuarioExistente.get().getIdUsuario().equals(id)) {
                redirectAttributes.addFlashAttribute("error", "Ya existe otro usuario con ese nombre de usuario");
                return "redirect:/usuarios/editar/" + id;
            }
            
            usuarioExistente = usuarioService.findByEmail(usuario.getEmail());
            if (usuarioExistente.isPresent() && !usuarioExistente.get().getIdUsuario().equals(id)) {
                redirectAttributes.addFlashAttribute("error", "Ya existe otro usuario con ese email");
                return "redirect:/usuarios/editar/" + id;
            }
            
            usuarioExistente = usuarioService.findByDni(usuario.getDni());
            if (usuarioExistente.isPresent() && !usuarioExistente.get().getIdUsuario().equals(id)) {
                redirectAttributes.addFlashAttribute("error", "Ya existe otro usuario con ese DNI");
                return "redirect:/usuarios/editar/" + id;
            }
            
            // Obtener el usuario actual para mantener algunos campos
            Optional<UsuarioEntity> usuarioActual = usuarioService.findById(id);
            if (usuarioActual.isPresent()) {
                UsuarioEntity usuarioToUpdate = usuarioActual.get();
                usuarioToUpdate.setNombres(usuario.getNombres());
                usuarioToUpdate.setDni(usuario.getDni());
                usuarioToUpdate.setEmail(usuario.getEmail());
                usuarioToUpdate.setUsername(usuario.getUsername());
                
                // Asignar el cargo al usuario
                if (cargoId != null) {
                    cargoService.findById(cargoId).ifPresent(usuarioToUpdate::setCargo);
                }
                
                // Solo actualizar contraseña si se proporciona una nueva
                if (usuario.getContrasena() != null && !usuario.getContrasena().trim().isEmpty()) {
                    usuarioToUpdate.setContrasena(usuario.getContrasena());
                }
                
                usuarioService.update(usuarioToUpdate);
                redirectAttributes.addFlashAttribute("success", "Usuario actualizado exitosamente");
            }
            return "redirect:/usuarios/listar";
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", "Error al actualizar usuario: " + e.getMessage());
            return "redirect:/usuarios/editar/" + id;
        }
    }

    /**
     * Desactiva un usuario (soft delete)
     * Requiere permiso USUARIO_UPDATE
     */
    @PostMapping("/desactivar/{id}")
    @PreAuthorize("hasAuthority('USUARIO_UPDATE')")
    public String desactivarUsuario(@PathVariable Integer id, RedirectAttributes redirectAttributes) {
        try {
            usuarioService.cambiarEstado(id, UsuarioEntity.EstadoUsuario.Inactivo);
            redirectAttributes.addFlashAttribute("success", "Usuario desactivado exitosamente");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", "Error al desactivar usuario: " + e.getMessage());
        }
        return "redirect:/usuarios/listar";
    }

    /**
     * Reactiva un usuario
     * Requiere permiso USUARIO_UPDATE
     */
    @PostMapping("/activar/{id}")
    @PreAuthorize("hasAuthority('USUARIO_UPDATE')")
    public String activarUsuario(@PathVariable Integer id, RedirectAttributes redirectAttributes) {
        try {
            usuarioService.cambiarEstado(id, UsuarioEntity.EstadoUsuario.Activo);
            redirectAttributes.addFlashAttribute("success", "Usuario activado exitosamente");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", "Error al activar usuario: " + e.getMessage());
        }
        return "redirect:/usuarios/listar";
    }

    /**
     * Muestra formulario de confirmación para desactivar usuario
     * Requiere permiso USUARIO_UPDATE
     */
    @GetMapping("/eliminar/{id}")
    @PreAuthorize("hasAuthority('USUARIO_UPDATE')")
    public String mostrarFormularioEliminar(@PathVariable Integer id, Model model) {
        try {
            Optional<UsuarioEntity> usuario = usuarioService.findById(id);
            if (usuario.isPresent()) {
                model.addAttribute("usuario", usuario.get());
                return "usuario/eliminar";
            } else {
                model.addAttribute("error", "Usuario no encontrado");
                return "redirect:/usuarios/listar";
            }
        } catch (Exception e) {
            model.addAttribute("error", "Error al cargar usuario: " + e.getMessage());
            return "redirect:/usuarios/listar";
        }
    }
}
