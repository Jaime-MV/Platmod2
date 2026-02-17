package com.prototipo.platmod.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
import org.springframework.web.filter.CorsFilter;

import jakarta.servlet.http.HttpServletResponse;

import java.util.Arrays;
import java.util.List;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

    @Autowired
    private JwtAuthenticationFilter jwtAuthenticationFilter;

    @Autowired
    private AuthenticationProvider authenticationProvider; // <--- INYECTAMOS EL PROVIDER DEL PASO 1

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                .csrf(AbstractHttpConfigurer::disable)
                .cors(AbstractHttpConfigurer::disable)
                .authorizeHttpRequests(auth -> auth
                        // 1. Permitir OPTIONS
                        .requestMatchers(HttpMethod.OPTIONS, "/**").permitAll()

                        // 2. Rutas Públicas
                        .requestMatchers("/api/auth/**").permitAll()
                        .requestMatchers(HttpMethod.POST, "/api/usuarios").permitAll()
                        .requestMatchers(HttpMethod.GET, "/api/cursos/**", "/api/planes/**", "/api/docentes/home")
                        .permitAll()

                        // 3. Admin
                        .requestMatchers("/api/admin/**").hasAuthority("ADMINISTRADOR")

                        // 4. Resto autenticado
                        .anyRequest().authenticated())
                .authenticationProvider(authenticationProvider) // <--- CONECTAMOS EL PROVIDER AQUÍ
                .addFilterBefore(jwtAuthenticationFilter, UsernamePasswordAuthenticationFilter.class)

                // Manejo de errores en consola
                .exceptionHandling(e -> e
                        .authenticationEntryPoint((request, response, authException) -> {
                            String errorMsg = (String) request.getAttribute("auth_error");
                            if (errorMsg == null)
                                errorMsg = authException.getMessage();

                            System.out.println("⛔ ERROR AUTH: " + errorMsg);
                            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
                            response.setContentType("application/json");
                            response.setHeader("X-Auth-Error", errorMsg);
                            response.getWriter().write("{\"error\": \"" + errorMsg + "\"}");
                            response.getWriter().flush();
                        })
                        .accessDeniedHandler((request, response, accessDeniedException) -> {
                            System.out.println("⛔ ACCESO DENEGADO: " + accessDeniedException.getMessage());
                            response.setStatus(HttpServletResponse.SC_FORBIDDEN);
                            response.setContentType("application/json");
                            response.getWriter().write(
                                    "{\"error\": \"Acceso denegado: " + accessDeniedException.getMessage() + "\"}");
                            response.getWriter().flush();
                        }));

        return http.build();
    }

    // El CorsFilter se mantiene igual, es correcto tenerlo aquí
    @Bean
    public CorsFilter corsFilter() {
        CorsConfiguration config = new CorsConfiguration();
        config.setAllowedOriginPatterns(List.of("*"));
        config.setAllowedMethods(Arrays.asList("GET", "POST", "PUT", "DELETE", "OPTIONS"));
        config.setAllowedHeaders(Arrays.asList("Authorization", "Content-Type", "X-Requested-With", "Accept"));
        config.setAllowCredentials(true);
        config.setMaxAge(3600L);

        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", config);

        return new CorsFilter(source);
    }

    // IMPORTANTE: YA NO ponemos el bean de PasswordEncoder aquí,
    // porque ya está en ApplicationConfig.
}