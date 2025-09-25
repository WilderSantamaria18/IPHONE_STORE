package com.idat.continua2.demo.Controller;

import com.idat.continua2.demo.Service.ProductoService;
import com.idat.continua2.demo.Service.ProveedorService;
import com.idat.continua2.demo.model.ProductoEntity;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

/**
 * Controlador MVC para gestión de Productos usando Thymeleaf
 * Implementa Spring Security con @PreAuthorize
 * Siguiendo principios Spring Boot MVC
 */
@Controller
@RequestMapping("/productos")
public class ProductoController {

    @Autowired
    private ProductoService productoService;

    @Autowired
    private ProveedorService proveedorService;

    /**
     * Lista todos los productos activos
     * Requiere permiso PRODUCTO_READ
     */
    @GetMapping("/listar")
    @PreAuthorize("hasAuthority('PRODUCTO_READ')")
    public String listarProductos(Model model) {
        try {
            var activos = productoService.findByEstado(ProductoEntity.EstadoProducto.Activo);
            var inactivos = productoService.findByEstado(ProductoEntity.EstadoProducto.Inactivo);
            activos.addAll(inactivos); // primero activos
            model.addAttribute("productos", activos);
            return "producto/listar";
        } catch (Exception e) {
            model.addAttribute("error", "Error al cargar productos: " + e.getMessage());
            return "error";
        }
    }

    /**
     * Muestra formulario para nuevo producto
     * Requiere permiso PRODUCTO_CREATE
     */
    @GetMapping("/nuevo")
    @PreAuthorize("hasAuthority('PRODUCTO_CREATE')")
    public String nuevoProducto(Model model) {
        try {
            model.addAttribute("producto", new ProductoEntity());
            model.addAttribute("proveedores", proveedorService.findAllActivos());
            return "producto/crear";
        } catch (Exception e) {
            model.addAttribute("error", "Error al cargar formulario: " + e.getMessage());
            return "error";
        }
    }

    /**
     * Procesa el guardado de un nuevo producto
     * Requiere permiso PRODUCTO_CREATE
     */
    @PostMapping("/guardar")
    @PreAuthorize("hasAuthority('PRODUCTO_CREATE')")
    public String guardarProducto(@ModelAttribute("producto") ProductoEntity producto, 
                                 BindingResult result,
                                 RedirectAttributes redirectAttributes) {
        if (result.hasErrors()) {
            return "producto/crear";
        }
        
        try {
            producto.setEstado(ProductoEntity.EstadoProducto.Activo);
            productoService.add(producto);
            redirectAttributes.addFlashAttribute("success", "Producto creado exitosamente");
            return "redirect:/productos/listar";
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", "Error al crear producto: " + e.getMessage());
            return "producto/crear";
        }
    }

    /**
     * Muestra formulario para editar producto
     * Requiere permiso PRODUCTO_UPDATE
     */
    @GetMapping("/editar/{id}")
    @PreAuthorize("hasAuthority('PRODUCTO_UPDATE')")
    public String editarProducto(@PathVariable Integer id, Model model) {
        try {
            model.addAttribute("producto", productoService.findById(id).orElse(new ProductoEntity()));
            model.addAttribute("proveedores", proveedorService.findAll());
            return "producto/editar";
        } catch (Exception e) {
            model.addAttribute("error", "Error al cargar producto: " + e.getMessage());
            return "redirect:/productos/listar";
        }
    }

    /**
     * Procesa la actualización de un producto
     * Requiere permiso PRODUCTO_UPDATE
     */
    @PostMapping("/editar/{id}")
    @PreAuthorize("hasAuthority('PRODUCTO_UPDATE')")
    public String actualizarProducto(@PathVariable Integer id, 
                                   @ModelAttribute("producto") ProductoEntity producto,
                                   BindingResult result,
                                   RedirectAttributes redirectAttributes) {
        if (result.hasErrors()) {
            return "producto/editar";
        }
        
        try {
            producto.setIdProducto(id);
            productoService.update(producto);
            redirectAttributes.addFlashAttribute("success", "Producto actualizado exitosamente");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", "Error al actualizar producto: " + e.getMessage());
        }
        return "redirect:/productos/listar";
    }

    /**
     * Desactiva un producto (soft delete)
     * Requiere permiso PRODUCTO_UPDATE
     */
    @PostMapping("/desactivar/{id}")
    @PreAuthorize("hasAuthority('PRODUCTO_UPDATE')")
    public String desactivarProducto(@PathVariable Integer id, RedirectAttributes redirectAttributes) {
        try {
            productoService.cambiarEstado(id, ProductoEntity.EstadoProducto.Inactivo);
            redirectAttributes.addFlashAttribute("success", "Producto desactivado exitosamente");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", "Error al desactivar producto: " + e.getMessage());
        }
        return "redirect:/productos/listar";
    }

    /**
     * Reactiva un producto
     * Requiere permiso PRODUCTO_UPDATE
     */
    @PostMapping("/activar/{id}")
    @PreAuthorize("hasAuthority('PRODUCTO_UPDATE')")
    public String activarProducto(@PathVariable Integer id, RedirectAttributes redirectAttributes) {
        try {
            productoService.cambiarEstado(id, ProductoEntity.EstadoProducto.Activo);
            redirectAttributes.addFlashAttribute("success", "Producto activado exitosamente");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", "Error al activar producto: " + e.getMessage());
        }
        return "redirect:/productos/listar";
    }

    @GetMapping("/eliminar/{id}")
    @PreAuthorize("hasAuthority('PRODUCTO_UPDATE')")
    public String mostrarEliminarProducto(@PathVariable Integer id, Model model) {
        model.addAttribute("producto", productoService.findById(id).orElse(null));
        return "producto/eliminar";
    }
}