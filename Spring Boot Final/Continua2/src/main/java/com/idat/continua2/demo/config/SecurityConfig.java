package com.idat.continua2.demo.config;

import com.idat.continua2.demo.Service.MyUserDetailsService;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

/**
 * Configuración de Spring Security para el sistema iPhone Store
 * Define autenticación, autorización y configuración de filtros
 * Utiliza autorización basada en permisos granulares
 */
@Configuration
@EnableWebSecurity
@EnableMethodSecurity(prePostEnabled = true)
public class SecurityConfig {

    private final MyUserDetailsService userDetailsService;

    public SecurityConfig(MyUserDetailsService userDetailsService) {
        this.userDetailsService = userDetailsService;
    }

    /**
     * Configuración del encoder de contraseñas usando BCrypt
     * @return PasswordEncoder configurado
     */
    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    /**
     * Configuración principal de la cadena de filtros de seguridad
     * Define rutas protegidas y permisos requeridos
     * @param http HttpSecurity para configurar
     * @return SecurityFilterChain configurado
     * @throws Exception si hay error en la configuración
     */
    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http
            .authorizeHttpRequests(authz -> authz
                // Recursos públicos
                .requestMatchers("/login", "/css/**", "/js/**", "/images/**", "/favicon.ico").permitAll()
                
                // Protección granular por permisos específicos - PRODUCTOS
                .requestMatchers("/productos/nuevo", "/productos/guardar").hasAuthority("PRODUCTO_CREATE")
                .requestMatchers("/productos/editar/**").hasAuthority("PRODUCTO_UPDATE")
                .requestMatchers("/productos/eliminar/**").hasAuthority("PRODUCTO_DELETE")
                .requestMatchers("/productos/listar", "/productos/**").hasAuthority("PRODUCTO_READ")
                
                // Protección granular por permisos específicos - PROVEEDORES
                .requestMatchers("/proveedores/nuevo", "/proveedores/guardar").hasAuthority("PROVEEDOR_CREATE")
                .requestMatchers("/proveedores/editar/**").hasAuthority("PROVEEDOR_UPDATE")
                .requestMatchers("/proveedores/eliminar/**").hasAuthority("PROVEEDOR_DELETE")
                .requestMatchers("/proveedores/listar", "/proveedores/**").hasAuthority("PROVEEDOR_READ")
                
                // Protección granular por permisos específicos - CLIENTES
                .requestMatchers("/Cliente/nuevo", "/Cliente/guardar").hasAuthority("CLIENTE_CREATE")
                .requestMatchers("/Cliente/editar/**").hasAuthority("CLIENTE_UPDATE")
                .requestMatchers("/Cliente/eliminar/**").hasAuthority("CLIENTE_DELETE")
                .requestMatchers("/Cliente/listar", "/Cliente/**").hasAuthority("CLIENTE_READ")

                // Protección granular por permisos específicos - COMPROBANTES
                .requestMatchers("/comprobantes/crear/**", "/comprobantes/generar/**", "/comprobantes/guardar").hasAuthority("COMPROBANTE_CREATE")
                .requestMatchers("/comprobantes/editar/**", "/comprobantes/anular/**").hasAuthority("COMPROBANTE_UPDATE")
                .requestMatchers("/comprobantes/eliminar/**").hasAuthority("COMPROBANTE_DELETE")
                .requestMatchers("/comprobantes/listar", "/comprobantes/detalle/**", "/comprobantes/imprimir/**", "/comprobantes/**").hasAuthority("COMPROBANTE_READ")   
                
                // Protección granular por permisos específicos - PEDIDOS
                .requestMatchers("/pedidos/nuevo", "/pedidos/guardar").hasAuthority("PEDIDO_CREATE")
                .requestMatchers("/pedidos/editar/**").hasAuthority("PEDIDO_UPDATE")
                .requestMatchers("/pedidos/eliminar/**").hasAuthority("PEDIDO_DELETE")
                .requestMatchers("/pedidos/listar", "/pedidos/**").hasAuthority("PEDIDO_READ")
                
                // Protección granular por permisos específicos - USUARIOS
                .requestMatchers("/usuarios/nuevo", "/usuarios/guardar").hasAuthority("USUARIO_CREATE")
                .requestMatchers("/usuarios/editar/**", "/usuarios/activar/**", "/usuarios/desactivar/**").hasAuthority("USUARIO_UPDATE")
                .requestMatchers("/usuarios/eliminar/**").hasAuthority("USUARIO_UPDATE")
                .requestMatchers("/usuarios/listar", "/usuarios/**").hasAuthority("USUARIO_READ")
                
                // Menú principal accesible para todos los usuarios autenticados
                .requestMatchers("/menu", "/").authenticated()
                
                // Cualquier otra ruta requiere autenticación
                .anyRequest().authenticated()
            )
            .formLogin(form -> form
                .loginPage("/login")
                .defaultSuccessUrl("/menu", true)
                .failureUrl("/login?error=true")
                .usernameParameter("username")
                .passwordParameter("password")
                .permitAll()
            )
            .logout(logout -> logout
                .logoutUrl("/logout")
                .logoutSuccessUrl("/login?logout=true")
                .invalidateHttpSession(true)
                .deleteCookies("JSESSIONID")
                .permitAll()
            )
            .sessionManagement(session -> session
                .maximumSessions(1)
                .maxSessionsPreventsLogin(false)
            )
            .csrf(csrf -> csrf.disable()) // Deshabilitado para simplificar desarrollo
            .userDetailsService(userDetailsService);

        return http.build();
    }
}
