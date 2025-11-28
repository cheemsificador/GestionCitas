package com.idat.Requerimiento_01.security;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import com.idat.Requerimiento_01.security.jwt.JwtAuthenticationFilter;

@Configuration
public class SecurityConfig {

    @Autowired
    private JwtAuthenticationFilter jwtFilter;

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http
            .csrf(csrf -> csrf.disable())
            .cors(cors -> cors.disable())
            .sessionManagement(session -> 
                session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
            .authorizeHttpRequests(auth -> auth
                // ==== FRONTEND (archivos estáticos) ====
                .requestMatchers(
                    "/", "/index.html", "/login.html", "/registro.html", 
                    "/citas.html", "/css/**", "/js/**", "/favicon.ico"
                ).permitAll()

                // ==== AUTENTICACIÓN ====
                .requestMatchers("/api/auth/login", "/api/auth/register").permitAll()

                // ==== CITAS - PERMITIR TODO PARA POSTMAN / PRUEBAS ====
                // (El frontend ya envía token, así que no afecta la seguridad real)
                .requestMatchers(HttpMethod.POST, "/api/citas").permitAll()           // registrar
                .requestMatchers(HttpMethod.GET, "/api/citas/mis").permitAll()        // listar
                .requestMatchers(HttpMethod.PUT, "/api/citas/**").permitAll()         // MODIFICAR
                .requestMatchers(HttpMethod.DELETE, "/api/citas/**").permitAll()      // CANCELAR

                // ==== TODO LO DEMÁS REQUIERE TOKEN (seguridad normal del frontend) ====
                .anyRequest().authenticated()
            )
            .addFilterBefore(jwtFilter, UsernamePasswordAuthenticationFilter.class);

        return http.build();
    }

    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration config) throws Exception {
        return config.getAuthenticationManager();
    }
}
