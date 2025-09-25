package com.idat.continua2.demo.Controller;

import com.idat.continua2.demo.Service.DetallePedidoService;
import com.idat.continua2.demo.Service.PedidoService;
import com.idat.continua2.demo.Service.ProductoService;
import com.idat.continua2.demo.model.DetallePedidoEntity;
import com.idat.continua2.demo.model.PedidoEntity;
import com.idat.continua2.demo.model.ProductoEntity;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

/**
 * Controlador MVC para gestión de DetallePedido usando Thymeleaf
 * Implementa Spring Security con @PreAuthorize
 * Siguiendo principios Spring Boot MVC
 * 🔗 INTEGRADO: Gestión asociada con Pedidos y Productos
 */
@Controller
@RequestMapping("/detalle-pedidos")
public class DetallePedidoController {

    @Autowired
    private DetallePedidoService detallePedidoService;
    
    @Autowired
    private PedidoService pedidoService;
    
    @Autowired
    private ProductoService productoService;

    /**
     * Lista todos los detalles de pedido
     * Requiere permiso DETALLE_PEDIDO_READ
     */
    @GetMapping("/listar")
    @PreAuthorize("hasAuthority('DETALLE_PEDIDO_READ')")
    public String listarDetalles(Model model) {
        try {
            List<DetallePedidoEntity> detalles = detallePedidoService.findAllWithRelations();
            model.addAttribute("detalles", detalles);
            
            // Estadísticas
            Long totalDetalles = (long) detalles.size();
            BigDecimal totalVentas = detalles.stream()
                .map(DetallePedidoEntity::calcularSubtotal)
                .reduce(BigDecimal.ZERO, BigDecimal::add);
            
            model.addAttribute("totalDetalles", totalDetalles);
            model.addAttribute("totalVentas", totalVentas);
            
            return "detalle-pedido/listar";
        } catch (Exception e) {
            model.addAttribute("error", "Error al cargar detalles: " + e.getMessage());
            return "detalle-pedido/listar";
        }
    }

    /**
     * Lista detalles por pedido específico
     * Requiere permiso DETALLE_PEDIDO_READ
     */
    @GetMapping("/pedido/{pedidoId}")
    @PreAuthorize("hasAuthority('DETALLE_PEDIDO_READ')")
    public String listarDetallesPorPedido(@PathVariable Integer pedidoId, Model model) {
        try {
            List<DetallePedidoEntity> detalles = detallePedidoService.findByPedidoIdWithRelations(pedidoId);
            Optional<PedidoEntity> pedido = pedidoService.findById(pedidoId);
            
            if (pedido.isPresent()) {
                model.addAttribute("pedido", pedido.get());
                model.addAttribute("detalles", detalles);
                
                // Estadísticas del pedido
                Long totalItems = detallePedidoService.countByPedidoId(pedidoId);
                BigDecimal totalPedido = detallePedidoService.calcularTotalPedido(pedidoId);
                
                model.addAttribute("totalItems", totalItems);
                model.addAttribute("totalPedido", totalPedido);
                
                return "detalle-pedido/por-pedido";
            } else {
                model.addAttribute("error", "Pedido no encontrado");
                return "redirect:/pedidos/listar";
            }
        } catch (Exception e) {
            model.addAttribute("error", "Error al cargar detalles del pedido: " + e.getMessage());
            return "redirect:/pedidos/listar";
        }
    }

    /**
     * Muestra formulario para crear nuevo detalle
     * Requiere permiso DETALLE_PEDIDO_CREATE
     */
    @GetMapping("/crear")
    @PreAuthorize("hasAuthority('DETALLE_PEDIDO_CREATE')")
    public String mostrarFormularioCrear(@RequestParam(required = false) Integer pedidoId, Model model) {
        try {
            DetallePedidoEntity detalle = new DetallePedidoEntity();
            
            // Si se proporciona un pedido, preseleccionarlo
            if (pedidoId != null) {
                Optional<PedidoEntity> pedido = pedidoService.findById(pedidoId);
                if (pedido.isPresent()) {
                    detalle.setPedido(pedido.get());
                    model.addAttribute("pedidoSeleccionado", pedido.get());
                }
            }
            
            model.addAttribute("detalle", detalle);
            model.addAttribute("pedidos", pedidoService.findAllWithRelations());
            model.addAttribute("productos", productoService.findByEstado(ProductoEntity.EstadoProducto.Activo));
            
            return "detalle-pedido/crear";
        } catch (Exception e) {
            model.addAttribute("error", "Error al cargar formulario: " + e.getMessage());
            return "redirect:/detalle-pedidos/listar";
        }
    }

    /**
     * Procesa la creación de un nuevo detalle
     * Requiere permiso DETALLE_PEDIDO_CREATE
     */
    @PostMapping("/crear")
    @PreAuthorize("hasAuthority('DETALLE_PEDIDO_CREATE')")
    public String crearDetalle(@ModelAttribute("detalle") DetallePedidoEntity detalle,
                              @RequestParam("pedido.idPedido") Integer pedidoId,
                              @RequestParam("producto.idProducto") Integer productoId,
                              BindingResult result,
                              RedirectAttributes redirectAttributes) {
        try {
            // Establecer relaciones
            Optional<PedidoEntity> pedido = pedidoService.findById(pedidoId);
            Optional<ProductoEntity> producto = productoService.findById(productoId);
            
            if (pedido.isPresent() && producto.isPresent()) {
                detalle.setPedido(pedido.get());
                detalle.setProducto(producto.get());
                
                // Si no se especifica precio, usar el precio del producto
                if (detalle.getPrecioUnitario() == null) {
                    detalle.setPrecioUnitario(producto.get().getPrecioVenta());
                }
                
                DetallePedidoEntity detalleCreado = detallePedidoService.add(detalle);
                redirectAttributes.addFlashAttribute("success", "Detalle creado exitosamente");
                return "redirect:/detalle-pedidos/pedido/" + pedidoId;
            } else {
                redirectAttributes.addFlashAttribute("error", "Pedido o producto no encontrado");
                return "redirect:/detalle-pedidos/crear";
            }
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", "Error al crear detalle: " + e.getMessage());
            return "redirect:/detalle-pedidos/crear";
        }
    }

    /**
     * Muestra formulario para editar detalle
     * Requiere permiso DETALLE_PEDIDO_UPDATE
     */
    @GetMapping("/editar/{id}")
    @PreAuthorize("hasAuthority('DETALLE_PEDIDO_UPDATE')")
    public String editarDetalle(@PathVariable Integer id, Model model) {
        try {
            Optional<DetallePedidoEntity> detalle = detallePedidoService.findById(id);
            if (detalle.isPresent()) {
                model.addAttribute("detalle", detalle.get());
                model.addAttribute("pedidos", pedidoService.findAllWithRelations());
                model.addAttribute("productos", productoService.findByEstado(ProductoEntity.EstadoProducto.Activo));
                return "detalle-pedido/editar";
            } else {
                model.addAttribute("error", "Detalle no encontrado");
                return "redirect:/detalle-pedidos/listar";
            }
        } catch (Exception e) {
            model.addAttribute("error", "Error al cargar detalle: " + e.getMessage());
            return "redirect:/detalle-pedidos/listar";
        }
    }

    /**
     * Procesa la actualización de un detalle
     * Requiere permiso DETALLE_PEDIDO_UPDATE
     */
    @PostMapping("/editar/{id}")
    @PreAuthorize("hasAuthority('DETALLE_PEDIDO_UPDATE')")
    public String actualizarDetalle(@PathVariable Integer id, 
                                   @ModelAttribute("detalle") DetallePedidoEntity detalle,
                                   @RequestParam("pedido.idPedido") Integer pedidoId,
                                   @RequestParam("producto.idProducto") Integer productoId,
                                   BindingResult result,
                                   RedirectAttributes redirectAttributes) {
        try {
            detalle.setIdDetalle(id);
            
            // Establecer relaciones
            Optional<PedidoEntity> pedido = pedidoService.findById(pedidoId);
            Optional<ProductoEntity> producto = productoService.findById(productoId);
            
            if (pedido.isPresent() && producto.isPresent()) {
                detalle.setPedido(pedido.get());
                detalle.setProducto(producto.get());
                
                detallePedidoService.update(detalle);
                redirectAttributes.addFlashAttribute("success", "Detalle actualizado exitosamente");
                return "redirect:/detalle-pedidos/pedido/" + pedidoId;
            } else {
                redirectAttributes.addFlashAttribute("error", "Pedido o producto no encontrado");
                return "redirect:/detalle-pedidos/editar/" + id;
            }
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", "Error al actualizar detalle: " + e.getMessage());
            return "redirect:/detalle-pedidos/editar/" + id;
        }
    }

    /**
     * Elimina un detalle
     * Requiere permiso DETALLE_PEDIDO_DELETE
     */
    @PostMapping("/eliminar/{id}")
    @PreAuthorize("hasAuthority('DETALLE_PEDIDO_DELETE')")
    public String eliminarDetalle(@PathVariable Integer id, 
                                 @RequestParam(required = false) Integer pedidoId,
                                 RedirectAttributes redirectAttributes) {
        try {
            // Obtener el pedido antes de eliminar
            if (pedidoId == null) {
                Optional<DetallePedidoEntity> detalle = detallePedidoService.findById(id);
                if (detalle.isPresent()) {
                    pedidoId = detalle.get().getPedido().getIdPedido();
                }
            }
            
            detallePedidoService.delete(id);
            redirectAttributes.addFlashAttribute("success", "Detalle eliminado exitosamente");
            
            if (pedidoId != null) {
                return "redirect:/detalle-pedidos/pedido/" + pedidoId;
            } else {
                return "redirect:/detalle-pedidos/listar";
            }
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", "Error al eliminar detalle: " + e.getMessage());
            return "redirect:/detalle-pedidos/listar";
        }
    }

    /**
     * Muestra estadísticas de productos más vendidos
     * Requiere permiso DETALLE_PEDIDO_READ
     */
    @GetMapping("/estadisticas")
    @PreAuthorize("hasAuthority('DETALLE_PEDIDO_READ')")
    public String mostrarEstadisticas(Model model) {
        try {
            List<Object[]> productosMasVendidos = detallePedidoService.findProductosMasVendidos();
            model.addAttribute("productosMasVendidos", productosMasVendidos);
            
            return "detalle-pedido/estadisticas";
        } catch (Exception e) {
            model.addAttribute("error", "Error al cargar estadísticas: " + e.getMessage());
            return "detalle-pedido/estadisticas";
        }
    }

    /**
     * API para obtener precio de producto (AJAX)
     * Requiere permiso DETALLE_PEDIDO_CREATE
     */
    @GetMapping("/api/producto/{productoId}/precio")
    @PreAuthorize("hasAuthority('DETALLE_PEDIDO_CREATE')")
    @ResponseBody
    public BigDecimal obtenerPrecioProducto(@PathVariable Integer productoId) {
        try {
            Optional<ProductoEntity> producto = productoService.findById(productoId);
            return producto.map(ProductoEntity::getPrecioVenta).orElse(BigDecimal.ZERO);
        } catch (Exception e) {
            return BigDecimal.ZERO;
        }
    }

    /**
     * API para recalcular total de pedido (AJAX)
     * Requiere permiso DETALLE_PEDIDO_UPDATE
     */
    @PostMapping("/api/pedido/{pedidoId}/recalcular")
    @PreAuthorize("hasAuthority('DETALLE_PEDIDO_UPDATE')")
    @ResponseBody
    public BigDecimal recalcularTotalPedido(@PathVariable Integer pedidoId) {
        try {
            return detallePedidoService.recalcularYActualizarTotalPedido(pedidoId);
        } catch (Exception e) {
            return BigDecimal.ZERO;
        }
    }
}
