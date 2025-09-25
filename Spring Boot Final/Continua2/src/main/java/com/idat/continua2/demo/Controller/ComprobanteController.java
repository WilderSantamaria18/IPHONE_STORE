package com.idat.continua2.demo.Controller;

import com.idat.continua2.demo.Service.PedidoService;
import com.idat.continua2.demo.Service.DetallePedidoService;
import com.idat.continua2.demo.Service.ComprobanteService;
import com.idat.continua2.demo.Service.DetalleComprobanteService;
import com.idat.continua2.demo.model.PedidoEntity;
import com.idat.continua2.demo.model.ComprobanteEntity;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.google.zxing.qrcode.QRCodeWriter;
import com.google.zxing.BarcodeFormat;
import com.google.zxing.common.BitMatrix;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Map;
import java.util.Optional;

import java.awt.image.BufferedImage;
import javax.imageio.ImageIO;
import java.io.ByteArrayOutputStream;

@Controller
@RequestMapping("/comprobantes")
public class ComprobanteController {

    @Autowired
    private PedidoService pedidoService;

    @Autowired
    private DetallePedidoService detallePedidoService;

    @Autowired
    private ComprobanteService comprobanteService;

    @Autowired
    private DetalleComprobanteService detalleComprobanteService;



    @GetMapping("/detalle/{id}")
    @PreAuthorize("hasAuthority('PEDIDO_READ')")
    public String detalleComprobante(@PathVariable Integer id, Model model) {
        try {
            Optional<ComprobanteEntity> comprobanteOpt = comprobanteService.findByIdWithRelations(id);
            if (comprobanteOpt.isPresent()) {
                ComprobanteEntity comprobante = comprobanteOpt.get();
                model.addAttribute("comprobante", comprobante);
                model.addAttribute("pedido", comprobante.getPedido());
                var detalles = detalleComprobanteService.findByComprobanteIdWithRelations(id);
                model.addAttribute("detalles", detalles);
                return "comprobante/detalle";
            } else {
                model.addAttribute("error", "Comprobante no encontrado");
                return "redirect:/comprobantes/listar";
            }
        } catch (Exception e) {
            model.addAttribute("error", "Error al cargar comprobante: " + e.getMessage());
            return "redirect:/comprobantes/listar";
        }
    }

    @GetMapping("/imprimir/{id}")
    @PreAuthorize("hasAuthority('PEDIDO_READ')")
    public String imprimirComprobante(@PathVariable Integer id, Model model) {
        try {
            Optional<ComprobanteEntity> comprobanteOpt = comprobanteService.findByIdWithRelations(id);
            if (comprobanteOpt.isPresent()) {
                ComprobanteEntity comprobante = comprobanteOpt.get();
                model.addAttribute("comprobante", comprobante);
                model.addAttribute("pedido", comprobante.getPedido());
                var detalles = detalleComprobanteService.findByComprobanteIdWithRelations(id);
                model.addAttribute("detalles", detalles);
                return "comprobante/imprimir";
            } else {
                model.addAttribute("error", "Comprobante no encontrado");
                return "redirect:/comprobantes/listar";
            }
        } catch (Exception e) {
            model.addAttribute("error", "Error al generar comprobante: " + e.getMessage());
            return "redirect:/comprobantes/listar";
        }
    }

    @GetMapping("/listar")
    @PreAuthorize("hasAuthority('PEDIDO_READ')")
    public String listarComprobantes(Model model) {
        try {
            var comprobantes = comprobanteService.findAllWithRelations();
            model.addAttribute("comprobantes", comprobantes);
            return "comprobante/listar";
        } catch (Exception e) {
            model.addAttribute("error", "Error al cargar comprobantes: " + e.getMessage());
            return "redirect:/menu";
        }
    }

    @GetMapping("/crear/{pedidoId}")
    @PreAuthorize("hasAuthority('PEDIDO_READ')")
    public String mostrarCrearComprobante(@PathVariable Integer pedidoId, Model model, RedirectAttributes redirectAttributes) {
        try {
            Optional<PedidoEntity> pedidoOpt = pedidoService.findById(pedidoId);
            if (!pedidoOpt.isPresent()) {
                redirectAttributes.addFlashAttribute("error", "Pedido no encontrado");
                return "redirect:/pedidos/listar";
            }
            PedidoEntity pedido = pedidoOpt.get();
            if (comprobanteService.existsByPedidoId(pedidoId)) {
                redirectAttributes.addFlashAttribute("error", "Ya existe un comprobante para este pedido");
                return "redirect:/pedidos/ver/" + pedidoId;
            }
            var detalles = detallePedidoService.findByPedidoIdWithRelations(pedidoId);
            if (detalles.isEmpty()) {
                redirectAttributes.addFlashAttribute("error", "El pedido no tiene productos. No se puede generar comprobante");
                return "redirect:/pedidos/ver/" + pedidoId;
            }
            model.addAttribute("pedido", pedido);
            model.addAttribute("detalles", detalles);
            return "comprobante/crear";
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", "Error al cargar formulario de comprobante: " + e.getMessage());
            return "redirect:/pedidos/ver/" + pedidoId;
        }
    }

    @PostMapping("/guardar")
    @PreAuthorize("hasAuthority('PEDIDO_READ')")
    public String guardarComprobante(@RequestParam Integer pedidoId,
                                   @RequestParam String tipoComprobante,
                                   @RequestParam BigDecimal subtotal,
                                   @RequestParam BigDecimal igv,
                                   @RequestParam BigDecimal totalPagar,
                                   RedirectAttributes redirectAttributes) {
        try {
            Optional<PedidoEntity> pedidoOpt = pedidoService.findById(pedidoId);
            if (!pedidoOpt.isPresent()) {
                redirectAttributes.addFlashAttribute("error", "Pedido no encontrado");
                return "redirect:/pedidos/listar";
            }
            PedidoEntity pedido = pedidoOpt.get();
            if (comprobanteService.existsByPedidoId(pedidoId)) {
                redirectAttributes.addFlashAttribute("error", "Ya existe un comprobante para este pedido");
                return "redirect:/pedidos/ver/" + pedidoId;
            }
            ComprobanteEntity comprobante = comprobanteService.generarComprobanteFromPedido(pedido, tipoComprobante);
            redirectAttributes.addFlashAttribute("success",
                "Comprobante " + comprobante.getNumeroComprobante() + " generado exitosamente");
            return "redirect:/comprobantes/detalle/" + comprobante.getIdComprobante();
        } catch (Exception e) {
            System.err.println("[ERROR guardarComprobante] " + e.getClass().getName() + ": " + e.getMessage());
            e.printStackTrace();
            redirectAttributes.addFlashAttribute("error",
                "Error al generar comprobante: " +
                (e.getMessage() != null ? e.getMessage() : e.getClass().getSimpleName()));
            return "redirect:/comprobantes/crear/" + pedidoId;
        }
    }

    @GetMapping("/generar/{pedidoId}")
    @PreAuthorize("hasAuthority('PEDIDO_READ')")
    public String generarComprobanteGet(@PathVariable Integer pedidoId,
                                      @RequestParam(defaultValue = "BOLETA") String tipoComprobante,
                                      RedirectAttributes redirectAttributes) {
        return "redirect:/comprobantes/crear/" + pedidoId;
    }

    @PostMapping("/generar/{pedidoId}")
    @PreAuthorize("hasAuthority('PEDIDO_READ')")
    public String generarComprobante(@PathVariable Integer pedidoId,
                                   @RequestParam(defaultValue = "BOLETA") String tipoComprobante,
                                   RedirectAttributes redirectAttributes) {
        try {
            Optional<PedidoEntity> pedidoOpt = pedidoService.findById(pedidoId);
            if (!pedidoOpt.isPresent()) {
                redirectAttributes.addFlashAttribute("error", "Pedido no encontrado");
                return "redirect:/pedidos/listar";
            }
            PedidoEntity pedido = pedidoOpt.get();
            if (comprobanteService.existsByPedidoId(pedidoId)) {
                redirectAttributes.addFlashAttribute("error", "Ya existe un comprobante para este pedido");
                return "redirect:/pedidos/ver/" + pedidoId;
            }
            ComprobanteEntity comprobante = comprobanteService.generarComprobanteFromPedido(pedido, tipoComprobante);
            redirectAttributes.addFlashAttribute("success",
                "Comprobante " + comprobante.getNumeroComprobante() + " generado exitosamente");
            return "redirect:/comprobantes/detalle/" + comprobante.getIdComprobante();
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", "Error al generar comprobante: " + e.getMessage());
            return "redirect:/pedidos/ver/" + pedidoId;
        }
    }

    @PostMapping("/eliminar/{id}")
    @PreAuthorize("hasAuthority('PEDIDO_WRITE')")
    public String eliminarComprobante(@PathVariable Integer id, RedirectAttributes redirectAttributes) {
        try {
            Optional<ComprobanteEntity> comprobante = comprobanteService.findById(id);
            if (comprobante.isPresent()) {
                comprobanteService.delete(id);
                redirectAttributes.addFlashAttribute("success", "Comprobante eliminado exitosamente");
            } else {
                redirectAttributes.addFlashAttribute("error", "Comprobante no encontrado");
            }
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", "Error al eliminar comprobante: " + e.getMessage());
        }
        return "redirect:/comprobantes/listar";
    }

    @PostMapping("/anular/{id}")
    @PreAuthorize("hasAuthority('PEDIDO_WRITE')")
    public String anularComprobante(@PathVariable Integer id, RedirectAttributes redirectAttributes) {
        try {
            ComprobanteEntity comprobante = comprobanteService.anularComprobante(id);
            redirectAttributes.addFlashAttribute("success",
                "Comprobante " + comprobante.getNumeroComprobante() + " anulado exitosamente");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", "Error al anular comprobante: " + e.getMessage());
        }
        return "redirect:/comprobantes/listar";
    }

    @GetMapping("/api/existe-por-pedido/{pedidoId}")
    @PreAuthorize("hasAuthority('PEDIDO_READ')")
    @ResponseBody
    public ResponseEntity<Boolean> existeComprobantePorPedido(@PathVariable Integer pedidoId) {
        try {
            boolean existe = comprobanteService.existsByPedidoId(pedidoId);
            return ResponseEntity.ok(existe);
        } catch (Exception e) {
            return ResponseEntity.internalServerError().build();
        }
    }

    @GetMapping("/api/por-pedido/{pedidoId}")
    @PreAuthorize("hasAuthority('PEDIDO_READ')")
    @ResponseBody
    public ResponseEntity<ComprobanteEntity> getComprobantePorPedido(@PathVariable Integer pedidoId) {
        try {
            Optional<ComprobanteEntity> comprobante = comprobanteService.findByPedidoIdWithRelations(pedidoId);
            if (comprobante.isPresent()) {
                return ResponseEntity.ok(comprobante.get());
            } else {
                return ResponseEntity.notFound().build();
            }
        } catch (Exception e) {
            return ResponseEntity.internalServerError().build();
        }
    }

    @GetMapping("/reportes/ventas-dia")
    @PreAuthorize("hasAuthority('PEDIDO_READ')")
    @ResponseBody
    public ResponseEntity<Object> getVentasDelDia(@RequestParam(required = false) String fecha) {
        try {
            LocalDate fechaConsulta = fecha != null ? LocalDate.parse(fecha) : LocalDate.now();
            var totalVentas = comprobanteService.getTotalVentasByFecha(fechaConsulta);
            return ResponseEntity.ok(Map.of(
                "fecha", fechaConsulta,
                "totalVentas", totalVentas
            ));
        } catch (Exception e) {
            return ResponseEntity.internalServerError().build();
        }
    }

    @GetMapping("/ticket/{id}")
    @PreAuthorize("hasAuthority('COMPROBANTE_READ')")
    public String verTicket(@PathVariable Integer id, Model model){
        ComprobanteEntity c = comprobanteService.findByIdWithRelations(id)
                .orElseThrow(()-> new IllegalArgumentException("Comprobante no encontrado"));
        model.addAttribute("comprobante", c);
        model.addAttribute("detalles", detalleComprobanteService.findByComprobanteIdWithRelations(id));
        model.addAttribute("montoEnLetras", numeroALetras(c.getTotalPagar()));
        return "comprobante/ticket";
    }

    @GetMapping(value="/qrcode/{id}", produces= MediaType.IMAGE_PNG_VALUE)
    @PreAuthorize("hasAuthority('COMPROBANTE_READ')")
    @ResponseBody
    public byte[] qr(@PathVariable Integer id) throws Exception{
        ComprobanteEntity c = comprobanteService.findByIdWithRelations(id)
                .orElseThrow(()-> new IllegalArgumentException("Comprobante no encontrado"));
        String contenido = "20089762534|" + c.getTipoComprobante() + "|" + c.getNumeroComprobante() + "|" +
                c.getTotalPagar() + "|" + c.getFechaEmision().toLocalDate();
        return generarQR(contenido,120,120);
    }

    // Método para generar QR
    private byte[] generarQR(String text, int w, int h) throws Exception {
        QRCodeWriter writer = new QRCodeWriter();
        BitMatrix bitMatrix = writer.encode(text, BarcodeFormat.QR_CODE, w, h);
        BufferedImage img = new BufferedImage(w, h, BufferedImage.TYPE_INT_RGB);
        for (int x = 0; x < w; x++) {
            for (int y = 0; y < h; y++) {
                img.setRGB(x, y, bitMatrix.get(x, y) ? 0x000000 : 0xFFFFFF);
            }
        }
        try (ByteArrayOutputStream baos = new ByteArrayOutputStream()) {
            ImageIO.write(img, "png", baos);
            return baos.toByteArray();
        }
    }

    // Conversión de monto a letras
    private String numeroALetras(java.math.BigDecimal monto){
        java.math.BigDecimal ent = monto.setScale(2, java.math.RoundingMode.HALF_UP);
        long parteEntera = ent.longValue();
        int centimos = ent.remainder(java.math.BigDecimal.ONE).movePointRight(2).intValue();
        return (convertirEntero(parteEntera)+" CON "+String.format("%02d",centimos)+"/100 SOLES").toUpperCase();
    }
    private String convertirEntero(long n){
        if(n==0) return "CERO";
        String[] UNIDADES={"","UNO","DOS","TRES","CUATRO","CINCO","SEIS","SIETE","OCHO","NUEVE","DIEZ","ONCE","DOCE","TRECE","CATORCE","QUINCE","DIECISEIS","DIECISIETE","DIECIOCHO","DIECINUEVE"};
        String[] DECENAS={"","DIEZ","VEINTE","TREINTA","CUARENTA","CINCUENTA","SESENTA","SETENTA","OCHENTA","NOVENTA"};
        String[] CENTENAS={"","CIENTO","DOSCIENTOS","TRESCIENTOS","CUATROCIENTOS","QUINIENTOS","SEISCIENTOS","SETECIENTOS","OCHOCIENTOS","NOVECIENTOS"};
        if(n==100) return "CIEN";
        if(n<20) return UNIDADES[(int)n];
        if(n<100){
            int d=(int)n/10, u=(int)n%10;
            if(n<30){ return (n==20?"VEINTE":"VEINTI"+(u==2?"DOS": u==3?"TRES": u==6?"SEIS":UNIDADES[u].toLowerCase()));}
            return DECENAS[d]+(u>0?" Y "+UNIDADES[u]:"");
        }
        if(n<1000){
            int c=(int)n/100; long r=n%100;
            return CENTENAS[c]+(r>0?" "+convertirEntero(r):"");
        }
        if(n<1_000_000){
            long m=n/1000; long r=n%1000;
            return (m==1?"MIL":convertirEntero(m)+" MIL")+(r>0?" "+convertirEntero(r):"");
        }
        if(n<1_000_000_000){
            long mi=n/1_000_000; long r=n%1_000_000;
            return (mi==1?"UN MILLÓN":convertirEntero(mi)+" MILLONES")+(r>0?" "+convertirEntero(r):"");
        }
        long b=n/1_000_000_000; long r=n%1_000_000_000;
        return (b==1?"UN BILLÓN":convertirEntero(b)+" BILLONES")+(r>0?" "+convertirEntero(r):"");
    }
}