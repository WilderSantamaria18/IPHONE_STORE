package com.idat.continua2.demo.Controller;

import com.idat.continua2.demo.Service.ProveedorService;
import com.idat.continua2.demo.model.ProveedorEntity;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

/**
 * Controlador MVC para gestión de Proveedores usando Thymeleaf
 * Implementa Spring Security con @PreAuthorize
 * Siguiendo principios Spring Boot MVC
 */
@Controller
@RequestMapping("/proveedores")
public class ProveedorController {

    @Autowired
    private ProveedorService proveedorService;

    /**
     * Lista todos los proveedores activos
     * Requiere permiso PROVEEDOR_READ
     */
    @GetMapping("/listar")
    @PreAuthorize("hasAuthority('PROVEEDOR_READ')")
    public String listarProveedores(Model model) {
        try {
            var activos = proveedorService.findByEstado(ProveedorEntity.EstadoProveedor.Activo);
            var inactivos = proveedorService.findByEstado(ProveedorEntity.EstadoProveedor.Inactivo);
            activos.addAll(inactivos); // mantiene primero activos
            model.addAttribute("proveedores", activos);
            return "proveedor/listar";
        } catch (Exception e) {
            model.addAttribute("error", "Error al cargar proveedores: " + e.getMessage());
            return "error";
        }
    }

    /**
     * Muestra formulario para nuevo proveedor
     * Requiere permiso PROVEEDOR_CREATE
     */
    @GetMapping("/nuevo")
    @PreAuthorize("hasAuthority('PROVEEDOR_CREATE')")
    public String nuevoProveedor(Model model) {
        model.addAttribute("proveedor", new ProveedorEntity());
        return "proveedor/crear";
    }

    /**
     * Procesa el guardado de un nuevo proveedor
     * Requiere permiso PROVEEDOR_CREATE
     */
    @PostMapping("/guardar")
    @PreAuthorize("hasAuthority('PROVEEDOR_CREATE')")
    public String guardarProveedor(@ModelAttribute("proveedor") ProveedorEntity proveedor, 
                                  BindingResult result,
                                  RedirectAttributes redirectAttributes) {
        if (result.hasErrors()) {
            return "proveedor/crear";
        }
        
        try {
            proveedor.setEstado(ProveedorEntity.EstadoProveedor.Activo);
            proveedorService.add(proveedor);
            redirectAttributes.addFlashAttribute("success", "Proveedor creado exitosamente");
            return "redirect:/proveedores/listar";
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", "Error al crear proveedor: " + e.getMessage());
            return "proveedor/crear";
        }
    }

    /**
     * Muestra formulario para editar proveedor
     * Requiere permiso PROVEEDOR_UPDATE
     */
    @GetMapping("/editar/{id}")
    @PreAuthorize("hasAuthority('PROVEEDOR_UPDATE')")
    public String editarProveedor(@PathVariable Integer id, Model model) {
        try {
            // TODO: Corregir cuando el servicio use Integer
            model.addAttribute("proveedor", proveedorService.findById(id).orElse(new ProveedorEntity()));
            return "proveedor/editar";
        } catch (Exception e) {
            model.addAttribute("error", "Error al cargar proveedor: " + e.getMessage());
            return "redirect:/proveedores/listar";
        }
    }

    /**
     * Procesa la actualización de un proveedor
     * Requiere permiso PROVEEDOR_UPDATE
     */
    @PostMapping("/editar/{id}")
    @PreAuthorize("hasAuthority('PROVEEDOR_UPDATE')")
    public String actualizarProveedor(@PathVariable Integer id, 
                                     @ModelAttribute("proveedor") ProveedorEntity proveedor,
                                     BindingResult result,
                                     RedirectAttributes redirectAttributes) {
        if (result.hasErrors()) {
            return "proveedor/editar";
        }
        
        try {
            proveedor.setIdProveedor(id);
            proveedorService.update(proveedor);
            redirectAttributes.addFlashAttribute("success", "Proveedor actualizado exitosamente");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", "Error al actualizar proveedor: " + e.getMessage());
        }
        return "redirect:/proveedores/listar";
    }

    /**
     * Desactiva un proveedor (soft delete)
     * Requiere permiso PROVEEDOR_UPDATE
     */
    @PostMapping("/desactivar/{id}")
    @PreAuthorize("hasAuthority('PROVEEDOR_UPDATE')")
    public String desactivarProveedor(@PathVariable Integer id, RedirectAttributes redirectAttributes) {
        try {
            // TODO: Corregir cuando el servicio use Integer
            proveedorService.cambiarEstado(id, ProveedorEntity.EstadoProveedor.Inactivo);
            redirectAttributes.addFlashAttribute("success", "Proveedor desactivado exitosamente");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", "Error al desactivar proveedor: " + e.getMessage());
        }
        return "redirect:/proveedores/listar";
    }

    /**
     * Reactiva un proveedor
     * Requiere permiso PROVEEDOR_UPDATE
     */
    @PostMapping("/activar/{id}")
    @PreAuthorize("hasAuthority('PROVEEDOR_UPDATE')")
    public String activarProveedor(@PathVariable Integer id, RedirectAttributes redirectAttributes) {
        try {
            // TODO: Corregir cuando el servicio use Integer
            proveedorService.cambiarEstado(id, ProveedorEntity.EstadoProveedor.Activo);
            redirectAttributes.addFlashAttribute("success", "Proveedor activado exitosamente");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", "Error al activar proveedor: " + e.getMessage());
        }
        return "redirect:/proveedores/listar";
    }

    @GetMapping("/eliminar/{id}")
    @PreAuthorize("hasAuthority('PROVEEDOR_DELETE')")
    public String mostrarEliminarProveedor(@PathVariable Integer id, Model model) {
        var proveedorOpt = proveedorService.findById(id);
        if (proveedorOpt.isPresent()) {
            model.addAttribute("proveedor", proveedorOpt.get());
            return "proveedor/eliminar";
        } else {
            model.addAttribute("error", "Proveedor no encontrado");
            return "redirect:/proveedores/listar";
        }
    }
}