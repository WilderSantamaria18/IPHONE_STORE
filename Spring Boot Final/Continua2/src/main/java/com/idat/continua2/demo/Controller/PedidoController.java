package com.idat.continua2.demo.Controller;

import com.idat.continua2.demo.Service.PedidoService;
import com.idat.continua2.demo.Service.ClienteService;
import com.idat.continua2.demo.Service.UsuarioService;
import com.idat.continua2.demo.Service.ProductoService;
import com.idat.continua2.demo.Service.DetallePedidoService;
import com.idat.continua2.demo.Service.ComprobanteService;
import com.idat.continua2.demo.model.ComprobanteEntity;
import com.idat.continua2.demo.model.PedidoEntity;
import com.idat.continua2.demo.model.ClienteEntity;
import com.idat.continua2.demo.model.UsuarioEntity;
import com.idat.continua2.demo.model.ProductoEntity;
import com.idat.continua2.demo.model.DetallePedidoEntity;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.math.BigDecimal;
import java.security.Principal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

/**
 * Controlador MVC para gestión de Pedidos usando Thymeleaf
 * Implementa Spring Security con @PreAuthorize
 * Siguiendo principios Spring Boot MVC
 */
@Controller
@RequestMapping("/pedidos")
public class PedidoController {

    @Autowired
    private PedidoService pedidoService;
    
    @Autowired
    private ClienteService clienteService;
    
    @Autowired
    private UsuarioService usuarioService;
    
    @Autowired
    private ProductoService productoService;
    
    @Autowired
    private DetallePedidoService detallePedidoService;

    @Autowired
    private ComprobanteService comprobanteService;

    /**
     * ✅ OPTIMIZADO: Lista todos los pedidos con sus relaciones
     * Requiere permiso PEDIDO_READ
     */
    @GetMapping("/listar")
    @PreAuthorize("hasAuthority('PEDIDO_READ')")
    public String listarPedidos(Model model) {
        try {
            // ✅ USANDO MÉTODO OPTIMIZADO para eliminar N+1 queries
            List<PedidoEntity> pedidos = pedidoService.findAllWithRelations();
            model.addAttribute("pedidos", pedidos);
            
            // Estadísticas básicas
            if (!pedidos.isEmpty()) {
                BigDecimal totalVentas = pedidos.stream()
                    .map(PedidoEntity::getTotalPagar)
                    .reduce(BigDecimal.ZERO, BigDecimal::add);
                model.addAttribute("totalVentas", totalVentas);
                model.addAttribute("totalPedidos", pedidos.size());
                
                // Pedidos de hoy
                List<PedidoEntity> pedidosHoy = pedidoService.findPedidosHoy();
                model.addAttribute("pedidosHoy", pedidosHoy.size());
                
                BigDecimal ventasHoy = pedidosHoy.stream()
                    .map(PedidoEntity::getTotalPagar)
                    .reduce(BigDecimal.ZERO, BigDecimal::add);
                model.addAttribute("ventasHoy", ventasHoy);
            }
            
            return "pedido/listar";
        } catch (Exception e) {
            model.addAttribute("error", "Error al cargar pedidos: " + e.getMessage());
            return "error";
        }
    }

    /**
     * Muestra formulario para nuevo pedido
     * Requiere permiso PEDIDO_CREATE
     */
    @GetMapping("/nuevo")
    @PreAuthorize("hasAuthority('PEDIDO_CREATE')")
    public String nuevoPedido(Model model, Principal principal) {
        try {
            // Obtener el usuario autenticado
            String username = principal.getName();
            Optional<UsuarioEntity> usuarioAutenticado = usuarioService.findByUsername(username);
            
            if (usuarioAutenticado.isEmpty()) {
                model.addAttribute("error", "No se pudo encontrar el usuario autenticado");
                return "redirect:/menu";
            }
            
            model.addAttribute("pedido", new PedidoEntity());
            model.addAttribute("clientes", clienteService.findByEstado(ClienteEntity.EstadoCliente.Activo));
            model.addAttribute("usuarioAutenticado", usuarioAutenticado.get());
            model.addAttribute("productos", productoService.findByEstado(ProductoEntity.EstadoProducto.Activo));
            model.addAttribute("fechaActual", LocalDate.now());
            return "pedido/crear";
        } catch (Exception e) {
            model.addAttribute("error", "Error al cargar formulario: " + e.getMessage());
            return "error";
        }
    }

    /**
     * Procesa el guardado de un nuevo pedido con detalles
     * Requiere permiso PEDIDO_CREATE
     */
    @PostMapping("/guardar")
    @PreAuthorize("hasAuthority('PEDIDO_CREATE')")
    public String guardarPedido(@ModelAttribute("pedido") PedidoEntity pedido,
                            @RequestParam(value = "cliente.idCliente", required = false) Integer clienteId,
                            @RequestParam(value = "productos[]", required = false) Integer[] productosIds,
                            @RequestParam(value = "cantidades[]", required = false) Integer[] cantidades,
                            @RequestParam(value = "precios[]", required = false) BigDecimal[] precios,
                            Principal principal,
                            BindingResult result,
                            RedirectAttributes redirectAttributes) {
        if (result.hasErrors()) {
            return "redirect:/pedidos/nuevo";
        }
        
        try {
            // Obtener el usuario autenticado automáticamente
            String username = principal.getName();
            Optional<UsuarioEntity> usuarioAutenticado = usuarioService.findByUsername(username);
            
            if (usuarioAutenticado.isEmpty()) {
                redirectAttributes.addFlashAttribute("error", "No se pudo identificar el usuario autenticado");
                return "redirect:/pedidos/nuevo";
            }
            
            // Validaciones básicas
            if (clienteId == null) {
                redirectAttributes.addFlashAttribute("error", "Debe seleccionar un cliente");
                return "redirect:/pedidos/nuevo";
            }
            
            // Validar que haya productos
            if (productosIds == null || productosIds.length == 0) {
                redirectAttributes.addFlashAttribute("error", "Debe agregar al menos un producto al pedido");
                return "redirect:/pedidos/nuevo";
            }
            
            // Asignar relaciones
            Optional<ClienteEntity> cliente = clienteService.findById(clienteId);
            
            if (cliente.isEmpty()) {
                redirectAttributes.addFlashAttribute("error", "Cliente no encontrado");
                return "redirect:/pedidos/nuevo";
            }
            
            pedido.setCliente(cliente.get());
            pedido.setUsuario(usuarioAutenticado.get()); // Usuario autenticado automáticamente
            
            // Establecer fecha de pedido
            LocalDateTime ahora = LocalDateTime.now();
            pedido.setFechaPedido(ahora);
            pedido.setFechaEntrega(ahora.toLocalDate());
            
            // Calcular total inicial
            BigDecimal totalCalculado = BigDecimal.ZERO;
            for (int i = 0; i < productosIds.length; i++) {
                if (cantidades[i] != null && precios[i] != null) {
                    BigDecimal subtotal = precios[i].multiply(new BigDecimal(cantidades[i]));
                    totalCalculado = totalCalculado.add(subtotal);
                }
            }
            pedido.setTotalPagar(totalCalculado);
            
            // Guardar el pedido primero
            PedidoEntity pedidoGuardado = pedidoService.add(pedido);
            
            // Crear los detalles del pedido
            for (int i = 0; i < productosIds.length; i++) {
                if (productosIds[i] != null && cantidades[i] != null && precios[i] != null) {
                    Optional<ProductoEntity> producto = productoService.findById(productosIds[i]);
                    if (producto.isPresent()) {
                        DetallePedidoEntity detalle = new DetallePedidoEntity();
                        detalle.setPedido(pedidoGuardado);
                        detalle.setProducto(producto.get());
                        detalle.setCantidad(cantidades[i]);
                        detalle.setPrecioUnitario(precios[i]);
                        detallePedidoService.add(detalle);

                        // ↓↓↓ DESCONTAR STOCK AQUÍ ↓↓↓
                        productoService.reducirStock(productosIds[i], cantidades[i]);
                    }
                }
            }
            
            redirectAttributes.addFlashAttribute("success", "Pedido registrado exitosamente");
            return "redirect:/pedidos/ver/" + pedidoGuardado.getIdPedido();
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", "Error al crear pedido: " + e.getMessage());
            return "redirect:/pedidos/nuevo";
        }
    }

    /**
     * Muestra formulario para editar pedido
     * Requiere permiso PEDIDO_UPDATE
     */
    @GetMapping("/editar/{id}")
    @PreAuthorize("hasAuthority('PEDIDO_UPDATE')")
    public String editarPedido(@PathVariable Integer id, Model model) {
        try {
            Optional<PedidoEntity> pedido = pedidoService.findById(id);
            if (pedido.isPresent()) {
                model.addAttribute("pedido", pedido.get());
                model.addAttribute("clientes", clienteService.findByEstado(ClienteEntity.EstadoCliente.Activo));
                model.addAttribute("usuarios", usuarioService.findByEstadoWithCargos(UsuarioEntity.EstadoUsuario.Activo));
                model.addAttribute("productos", productoService.findByEstado(ProductoEntity.EstadoProducto.Activo));
                
                // Cargar los detalles del pedido con productos
                var detalles = detallePedidoService.findByPedidoIdWithRelations(id);
                model.addAttribute("detalles", detalles);
                
                return "pedido/editar";
            } else {
                model.addAttribute("error", "Pedido no encontrado");
                return "redirect:/pedidos/listar";
            }
        } catch (Exception e) {
            model.addAttribute("error", "Error al cargar pedido: " + e.getMessage());
            return "redirect:/pedidos/listar";
        }
    }

    /**
     * Procesa la actualización de un pedido con sus detalles
     * Requiere permiso PEDIDO_UPDATE
     */
    @PostMapping("/editar/{id}")
    @PreAuthorize("hasAuthority('PEDIDO_UPDATE')")
    public String actualizarPedido(@PathVariable Integer id, 
                                  @ModelAttribute("pedido") PedidoEntity pedido,
                                  @RequestParam(value = "cliente.idCliente", required = false) Integer clienteId,
                                  @RequestParam(value = "usuario.idUsuario", required = false) Integer usuarioId,
                                  @RequestParam Map<String, String> allParams,
                                  BindingResult result,
                                  RedirectAttributes redirectAttributes) {
        if (result.hasErrors()) {
            return "pedido/editar";
        }
        
        try {
            Optional<PedidoEntity> pedidoActual = pedidoService.findById(id);
            if (pedidoActual.isPresent()) {
                PedidoEntity pedidoToUpdate = pedidoActual.get();
                
                // Actualizar campos básicos del pedido
                pedidoToUpdate.setFechaEntrega(pedido.getFechaEntrega());
                pedidoToUpdate.setTipoPago(pedido.getTipoPago());
                
                // Actualizar relaciones si se proporcionan
                if (clienteId != null) {
                    clienteService.findById(clienteId).ifPresent(pedidoToUpdate::setCliente);
                }
                
                if (usuarioId != null) {
                    usuarioService.findById(usuarioId).ifPresent(pedidoToUpdate::setUsuario);
                }
                
                // Extraer arrays de productos, cantidades y precios de los parámetros
                List<Integer> productosIds = new ArrayList<>();
                List<Integer> cantidades = new ArrayList<>();
                List<BigDecimal> precios = new ArrayList<>();
                
                // Buscar todos los parámetros que siguen el patrón productos[n], cantidades[n], precios[n]
                for (Map.Entry<String, String> entry : allParams.entrySet()) {
                    String key = entry.getKey();
                    String value = entry.getValue();
                    
                    if (key.matches("productos\\[\\d+\\]") && value != null && !value.trim().isEmpty()) {
                        try {
                            productosIds.add(Integer.valueOf(value));
                        } catch (NumberFormatException e) {
                            System.err.println("Error parsing producto ID: " + value);
                        }
                    } else if (key.matches("cantidades\\[\\d+\\]") && value != null && !value.trim().isEmpty()) {
                        try {
                            cantidades.add(Integer.valueOf(value));
                        } catch (NumberFormatException e) {
                            System.err.println("Error parsing cantidad: " + value);
                        }
                    } else if (key.matches("precios\\[\\d+\\]") && value != null && !value.trim().isEmpty()) {
                        try {
                            precios.add(new BigDecimal(value));
                        } catch (NumberFormatException e) {
                            System.err.println("Error parsing precio: " + value);
                        }
                    }
                }
                
                // Primero, eliminar todos los detalles existentes
                detallePedidoService.deleteByPedidoId(id);
                
                // Crear nuevos detalles con los datos enviados del formulario
                if (!productosIds.isEmpty() && productosIds.size() == cantidades.size() && cantidades.size() == precios.size()) {
                    for (int i = 0; i < productosIds.size(); i++) {
                        if (productosIds.get(i) != null && cantidades.get(i) != null && precios.get(i) != null && cantidades.get(i) > 0) {
                            Optional<ProductoEntity> producto = productoService.findById(productosIds.get(i));
                            if (producto.isPresent()) {
                                DetallePedidoEntity nuevoDetalle = new DetallePedidoEntity();
                                nuevoDetalle.setPedido(pedidoToUpdate);
                                nuevoDetalle.setProducto(producto.get());
                                nuevoDetalle.setCantidad(cantidades.get(i));
                                nuevoDetalle.setPrecioUnitario(precios.get(i));
                                detallePedidoService.add(nuevoDetalle);
                            }
                        }
                    }
                }
                
                // Recalcular el total del pedido
                BigDecimal nuevoTotal = detallePedidoService.calcularTotalPedido(id);
                pedidoToUpdate.setTotalPagar(nuevoTotal);
                
                pedidoService.update(pedidoToUpdate);
                redirectAttributes.addFlashAttribute("success", "Pedido y sus productos actualizados exitosamente");
            } else {
                redirectAttributes.addFlashAttribute("error", "Pedido no encontrado");
            }
            
            return "redirect:/pedidos/listar";
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", "Error al actualizar pedido: " + e.getMessage());
            return "redirect:/pedidos/editar/" + id;
        }
    }

    /**
     * Elimina un pedido
     * Requiere permiso PEDIDO_DELETE
     */
    @PostMapping("/eliminar/{id}")
    @PreAuthorize("hasAuthority('PEDIDO_DELETE')")
    public String eliminarPedido(@PathVariable Integer id, RedirectAttributes redirectAttributes) {
        try {
            pedidoService.delete(id);
            redirectAttributes.addFlashAttribute("success", "Pedido eliminado exitosamente");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", "Error al eliminar pedido: " + e.getMessage());
        }
        return "redirect:/pedidos/listar";
    }

    /**
     * Muestra detalles de un pedido
     * Requiere permiso PEDIDO_READ
     */
    @GetMapping("/ver/{id}")
    @PreAuthorize("hasAuthority('PEDIDO_READ')")
    public String verPedido(@PathVariable Integer id, Model model) {
        try {
            Optional<PedidoEntity> pedido = pedidoService.findById(id);
            if (pedido.isPresent()) {
                model.addAttribute("pedido", pedido.get());
                // Cargar los detalles del pedido con productos
                var detalles = detallePedidoService.findByPedidoIdWithRelations(id);
                model.addAttribute("detalles", detalles);
                return "pedido/ver";
            } else {
                model.addAttribute("error", "Pedido no encontrado");
                return "redirect:/pedidos/listar";
            }
        } catch (Exception e) {
            model.addAttribute("error", "Error al cargar pedido: " + e.getMessage());
            return "redirect:/pedidos/listar";
        }
    }

    /**
     * Endpoint AJAX para obtener precio de producto
     * Requiere permiso PEDIDO_CREATE
     */
    @GetMapping("/producto-precio/{id}")
    @PreAuthorize("hasAuthority('PEDIDO_CREATE')")
    @ResponseBody
    public Map<String, Object> obtenerPrecioProducto(@PathVariable Integer id) {
        Map<String, Object> response = new HashMap<>();
        try {
            System.out.println("Buscando producto con ID: " + id); // Debug
            Optional<ProductoEntity> producto = productoService.findById(id);
            if (producto.isPresent()) {
                ProductoEntity prod = producto.get();
                response.put("success", true);
                response.put("precio", prod.getPrecioVenta());
                response.put("nombre", prod.getModelo());
                response.put("stock", prod.getStock());
                System.out.println("Producto encontrado: " + prod.getModelo() + " - Precio: " + prod.getPrecioVenta()); // Debug
            } else {
                response.put("success", false);
                response.put("message", "Producto no encontrado con ID: " + id);
                System.out.println("Producto no encontrado con ID: " + id); // Debug
            }
        } catch (Exception e) {
            response.put("success", false);
            response.put("message", "Error al obtener precio: " + e.getMessage());
            System.err.println("Error al obtener precio del producto: " + e.getMessage());
            e.printStackTrace();
        }
        return response;
    }

    /**
     * Actualiza el total de un pedido basado en sus detalles
     * Requiere permiso PEDIDO_UPDATE
     */
    @PostMapping("/actualizar-total/{id}")
    @PreAuthorize("hasAuthority('PEDIDO_UPDATE')")
    public String actualizarTotalPedido(@PathVariable Integer id, RedirectAttributes redirectAttributes) {
        try {
            PedidoEntity pedidoActualizado = pedidoService.actualizarTotalPedido(id);
            redirectAttributes.addFlashAttribute("success", 
                "Total actualizado: S/ " + pedidoActualizado.getTotalPagar());
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", "Error al actualizar total: " + e.getMessage());
        }
        return "redirect:/pedidos/ver/" + id;
    }

    /**
     * Mostrar pantalla de confirmación de anulación
     * Requiere permiso PEDIDO_DELETE
     */
    @GetMapping("/anular/{id}")
    @PreAuthorize("hasAuthority('PEDIDO_DELETE')")
    public String mostrarAnulacion(@PathVariable Integer id, Model model, RedirectAttributes ra) {
        Optional<PedidoEntity> pedidoOpt = pedidoService.findById(id);
        if (pedidoOpt.isEmpty()) {
            ra.addFlashAttribute("error","Pedido no encontrado");
            return "redirect:/pedidos/listar";
        }
        PedidoEntity pedido = pedidoOpt.get();
        if (pedido.getEstado() != null && pedido.getEstado().name().equalsIgnoreCase("Anulado")) {
            ra.addFlashAttribute("info","El pedido ya está anulado");
            return "redirect:/pedidos/ver/" + id;
        }
        model.addAttribute("pedido", pedido);
        model.addAttribute("detalles", detallePedidoService.findByPedidoIdWithRelations(id));
        comprobanteService.findByPedidoIdWithRelations(id)
                .ifPresent(c -> model.addAttribute("comprobante", c));
        return "pedido/anular";
    }

    /**
     * Procesar anulación: cambia estado del pedido, anula comprobante y revierte stock
     * Requiere permiso PEDIDO_DELETE
     */
    @PostMapping("/anular/{id}")
    @PreAuthorize("hasAuthority('PEDIDO_DELETE')")
    public String anularPedido(@PathVariable Integer id, RedirectAttributes ra) {
        try {
            Optional<PedidoEntity> pedidoOpt = pedidoService.findById(id);
            if (pedidoOpt.isEmpty()) {
                ra.addFlashAttribute("error","Pedido no encontrado");
                return "redirect:/pedidos/listar";
            }
            PedidoEntity pedido = pedidoOpt.get();
            if (pedido.getEstado() != null && pedido.getEstado().name().equalsIgnoreCase("Anulado")) {
                ra.addFlashAttribute("info","El pedido ya estaba anulado");
                return "redirect:/pedidos/ver/" + id;
            }

            // Recuperar detalles para revertir stock
            var detalles = detallePedidoService.findByPedidoIdWithRelations(id);

            // Cambiar estado del pedido
            pedido.setEstado(PedidoEntity.EstadoPedido.Anulado);
            pedidoService.update(pedido);

            // Anular comprobante si existe
            comprobanteService.findByPedidoIdWithRelations(id).ifPresent(c -> {
                if (c.getEstado() != ComprobanteEntity.EstadoComprobante.Anulado) {
                    c.setEstado(ComprobanteEntity.EstadoComprobante.Anulado);
                    comprobanteService.update(c);
                }
            });

            // Revertir stock de productos
            detalles.forEach(d -> {
                productoService.aumentarStock(d.getProducto().getIdProducto(), d.getCantidad());
            });

            ra.addFlashAttribute("success","Pedido anulado correctamente. Stock y comprobante revertidos.");
            return "redirect:/pedidos/listar"; 
        } catch (Exception ex) {
            ra.addFlashAttribute("error","Error al anular pedido: " + ex.getMessage());
            return "redirect:/pedidos/listar";
        }
    }
}