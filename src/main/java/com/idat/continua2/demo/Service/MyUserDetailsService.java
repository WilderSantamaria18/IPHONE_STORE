package com.idat.continua2.demo.Service;

import com.idat.continua2.demo.Repository.UsuarioRepository;
import com.idat.continua2.demo.model.UsuarioEntity;
import com.idat.continua2.demo.model.CargoEntity;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Component;

import java.util.HashSet;
import java.util.Optional;
import java.util.Set;

@Component
public class MyUserDetailsService implements UserDetailsService {

    private final UsuarioRepository usuarioRepository;

    public MyUserDetailsService(UsuarioRepository usuarioRepository) {
        this.usuarioRepository = usuarioRepository;
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        // ✅ OPTIMIZADO: Usar consulta con JOIN FETCH para eliminar N+1 queries
        Optional<UsuarioEntity> userDb = usuarioRepository.findByUsernameWithCargo(username);

        if (userDb.isPresent()) {
            UsuarioEntity usuario = userDb.get();

            Set<String> authorities = new HashSet<>();
            
            // Obtener el cargo del usuario y asignar roles basados en el cargo
            CargoEntity cargo = usuario.getCargo();
            if (cargo != null) {
                String nombreCargo = cargo.getNombre();
                
                // Asignar roles según el cargo
                switch (nombreCargo.toLowerCase()) {
                    case "administracion":
                        authorities.add("ROLE_ADMIN");
                        // Permisos completos para todos los módulos
                        authorities.add("PRODUCTO_CREATE");
                        authorities.add("PRODUCTO_READ");
                        authorities.add("PRODUCTO_UPDATE");
                        authorities.add("PRODUCTO_DELETE");
                        authorities.add("PROVEEDOR_CREATE");
                        authorities.add("PROVEEDOR_READ");
                        authorities.add("PROVEEDOR_UPDATE");
                        authorities.add("PROVEEDOR_DELETE");
                        authorities.add("CLIENTE_CREATE");
                        authorities.add("CLIENTE_READ");
                        authorities.add("CLIENTE_UPDATE");
                        authorities.add("CLIENTE_DELETE");
                        authorities.add("PEDIDO_CREATE");
                        authorities.add("PEDIDO_READ");
                        authorities.add("PEDIDO_UPDATE");
                        authorities.add("PEDIDO_DELETE");
                        authorities.add("COMPROBANTE_CREATE");
                        authorities.add("COMPROBANTE_READ");
                        authorities.add("COMPROBANTE_UPDATE");
                        authorities.add("COMPROBANTE_DELETE");
                        authorities.add("USUARIO_CREATE");
                        authorities.add("USUARIO_READ");
                        authorities.add("USUARIO_UPDATE");
                        authorities.add("USUARIO_DELETE");
                        break;
                    case "ventas":
                        authorities.add("ROLE_VENDEDOR");
                        // Permisos para gestión de clientes y pedidos
                        authorities.add("CLIENTE_CREATE");
                        authorities.add("CLIENTE_READ");
                        authorities.add("CLIENTE_UPDATE");
                        authorities.add("PEDIDO_CREATE");
                        authorities.add("PEDIDO_READ");
                        authorities.add("PEDIDO_UPDATE");
                        // Permisos para gestión de comprobantes
                        authorities.add("COMPROBANTE_CREATE");
                        authorities.add("COMPROBANTE_READ");
                        authorities.add("COMPROBANTE_UPDATE");
                        // Solo lectura de productos (para consultar en ventas)
                        authorities.add("PRODUCTO_READ");
                        break;
                    case "almacen":
                        authorities.add("ROLE_ALMACEN");
                        // Permisos para gestión de productos y proveedores únicamente
                        authorities.add("PRODUCTO_CREATE");
                        authorities.add("PRODUCTO_READ");
                        authorities.add("PRODUCTO_UPDATE");
                        authorities.add("PRODUCTO_DELETE");
                        authorities.add("PROVEEDOR_CREATE");
                        authorities.add("PROVEEDOR_READ");
                        authorities.add("PROVEEDOR_UPDATE");
                        authorities.add("PROVEEDOR_DELETE");
                        break;
                    default:
                        authorities.add("ROLE_USER");
                        break;
                }
            } else {
                authorities.add("ROLE_USER");
            }

            return User.withUsername(usuario.getUsername())
                    .password(usuario.getContrasena())
                    .authorities(authorities.toArray(new String[0]))
                    .build();
        } else {
            throw new UsernameNotFoundException("Usuario no encontrado: " + username);
        }
    }
}