package com.idat.continua2.demo.Controller;

import com.idat.continua2.demo.Service.ClienteService;
import com.idat.continua2.demo.model.ClienteEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;
import java.util.Optional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

/**
 * Controlador MVC para gestión de Clientes usando Thymeleaf
 * Siguiendo principios Spring Boot MVC con separación clara de responsabilidades
 */
@Controller
@RequestMapping("/Cliente")
public class ClienteController {

    @Autowired
    private ClienteService clienteService;

    // ==================== ENDPOINTS REST API ====================
    
    @GetMapping("/api")
    @ResponseBody
    public List<ClienteEntity> findAll() {
        return clienteService.findAll();
    }

    @GetMapping("/api/{id}")
    @ResponseBody
    public ResponseEntity<ClienteEntity> findById(@PathVariable Integer id) {
        Optional<ClienteEntity> cliente = clienteService.findById(id);
        return cliente.map(ResponseEntity::ok)
                      .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping("/api")
    @ResponseBody
    public ResponseEntity<ClienteEntity> add(@RequestBody ClienteEntity cliente) {
        try {
            ClienteEntity savedCliente = clienteService.add(cliente);
            return ResponseEntity.ok(savedCliente);
        } catch (Exception e) {
            return ResponseEntity.internalServerError().build();
        }
    }

    @PutMapping("/api/{id}")
    @ResponseBody
    public ResponseEntity<ClienteEntity> update(@PathVariable Integer id, @RequestBody ClienteEntity cliente) {
        try {
            cliente.setIdCliente(id);
            ClienteEntity updatedCliente = clienteService.update(cliente);
            return ResponseEntity.ok(updatedCliente);
        } catch (Exception e) {
            return ResponseEntity.internalServerError().build();
        }
    }

    @DeleteMapping("/api/{id}")
    @ResponseBody
    public ResponseEntity<Void> delete(@PathVariable Integer id) {
        try {
            // TODO: Implementar método delete en servicio
            // clienteService.delete(cliente);
            return ResponseEntity.ok().build();
        } catch (Exception e) {
            return ResponseEntity.internalServerError().build();
        }
    }

    // ==================== VISTAS THYMELEAF ====================
    // ==================== VISTAS THYMELEAF ====================
    
    /**
     * Lista todos los clientes activos
     * @param model - Modelo para la vista Thymeleaf
     * @return vista cliente/listar
     */
    @GetMapping("/listar")
    // @PreAuthorize("hasAuthority('CLIENTE_READ')") // Descomenta si usas seguridad por permisos
    public String listarClientes(Model model) {
        try {
            var activos = clienteService.findByEstado(ClienteEntity.EstadoCliente.Activo);
            var inactivos = clienteService.findByEstado(ClienteEntity.EstadoCliente.Inactivo);
            activos.addAll(inactivos); // concatena, mantiene primero los activos
            model.addAttribute("clientes", activos);
            return "cliente/listar";
        } catch (Exception e) {
            model.addAttribute("error", "Error al cargar clientes: " + e.getMessage());
            return "error";
        }
    }

    /**
     * Muestra formulario para nuevo cliente
     * @param model - Modelo para la vista
     * @return vista cliente/crear
     */
    @GetMapping("/nuevo")
    public String nuevoCliente(Model model) {
        model.addAttribute("cliente", new ClienteEntity());
        return "cliente/crear";
    }

    /**
     * Procesa el guardado de un nuevo cliente
     * @param cliente - Datos del cliente desde el formulario
     * @param result - Resultado de validación
     * @param redirectAttributes - Atributos para redirect
     * @return redirect o vista con errores
     */
    @PostMapping("/guardar")
    public String guardarCliente(@ModelAttribute("cliente") ClienteEntity cliente, 
                                BindingResult result, 
                                RedirectAttributes redirectAttributes) {
        if (result.hasErrors()) {
            return "cliente/crear";
        }
        
        try {
            cliente.setEstado(ClienteEntity.EstadoCliente.Activo);
            clienteService.add(cliente);
            redirectAttributes.addFlashAttribute("success", "Cliente creado exitosamente");
            return "redirect:/Cliente/listar";
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", "Error al crear cliente: " + e.getMessage());
            return "cliente/crear";
        }
    }

    /**
     * Muestra formulario para editar cliente
     * @param id - ID del cliente a editar
     * @param model - Modelo para la vista
     * @return vista cliente/editar
     */
    @GetMapping("/editar/{id}")
    public String editarCliente(@PathVariable Integer id, Model model) {
        try {
            Optional<ClienteEntity> cliente = clienteService.findById(id);
            if (cliente.isPresent()) {
                model.addAttribute("cliente", cliente.get());
                return "cliente/editar";
            } else {
                model.addAttribute("error", "Cliente no encontrado");
                return "redirect:/Cliente/listar";
            }
        } catch (Exception e) {
            model.addAttribute("error", "Error al cargar cliente: " + e.getMessage());
            return "redirect:/Cliente/listar";
        }
    }

    /**
     * Procesa la actualización de un cliente
     * @param id - ID del cliente
     * @param cliente - Datos actualizados
     * @param result - Resultado de validación
     * @param redirectAttributes - Atributos para redirect
     * @return redirect
     */
    @PostMapping("/editar/{id}")
    public String actualizarCliente(@PathVariable Integer id, 
                                   @ModelAttribute("cliente") ClienteEntity cliente, 
                                   BindingResult result,
                                   RedirectAttributes redirectAttributes) {
        if (result.hasErrors()) {
            return "cliente/editar";
        }
        
        try {
            cliente.setIdCliente(id);
            clienteService.update(cliente);
            redirectAttributes.addFlashAttribute("success", "Cliente actualizado exitosamente");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", "Error al actualizar cliente: " + e.getMessage());
        }
        return "redirect:/Cliente/listar";
    }

    /**
     * Desactiva un cliente (soft delete)
     * @param id - ID del cliente
     * @param redirectAttributes - Atributos para redirect
     * @return redirect
     */
    @PostMapping("/desactivar/{id}")
    public String desactivarCliente(@PathVariable Integer id, RedirectAttributes redirectAttributes) {
        try {
            clienteService.cambiarEstado(id, ClienteEntity.EstadoCliente.Inactivo);
            redirectAttributes.addFlashAttribute("success", "Cliente desactivado exitosamente");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", "Error al desactivar cliente: " + e.getMessage());
        }
        return "redirect:/Cliente/listar";
    }

    /**
     * Reactiva un cliente
     * @param id - ID del cliente
     * @param redirectAttributes - Atributos para redirect
     * @return redirect
     */
    @PostMapping("/activar/{id}")
    public String activarCliente(@PathVariable Integer id, RedirectAttributes redirectAttributes) {
        try {
            clienteService.cambiarEstado(id, ClienteEntity.EstadoCliente.Activo);
            redirectAttributes.addFlashAttribute("success", "Cliente activado exitosamente");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", "Error al activar cliente: " + e.getMessage());
        }
        return "redirect:/Cliente/listar";
    }

    @GetMapping("/eliminar/{id}")
    public String mostrarEliminarCliente(@PathVariable Integer id, Model model) {
        Optional<ClienteEntity> cliente = clienteService.findById(id);
        if (cliente.isPresent()) {
            model.addAttribute("cliente", cliente.get());
            return "cliente/eliminar";
        } else {
            model.addAttribute("error", "Cliente no encontrado");
            return "redirect:/Cliente/listar";
        }
    }
}