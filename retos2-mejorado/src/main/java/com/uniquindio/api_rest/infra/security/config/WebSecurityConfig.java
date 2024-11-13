package com.uniquindio.api_rest.infra.security.config;

import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class WebSecurityConfig {

    private final JwtAuthenticationFilter jwtAuthFilter;
    private final JwtAuthenticationEntryPoint jwtEntryPoint;
    private final AuthenticationProvider authenticationProvider;

    private String[] getPermittedUrls() {
        return new String[] {
                "/api/auth/**",
                "/actuator/**",
                "/doc/**",
                "/swagger-ui/**",         // Swagger UI
                "/v3/api-docs/**",         // Documentación de la API
                "/swagger-ui.html",        // Swagger UI principal
                "/swagger-resources/**",   // Recursos de Swagger
                "/webjars/**",             // Dependencias de Swagger (JavaScript, CSS, etc.)
                "/v3/api-docs/swagger-config",  // Configuración de Swagger
                "/openapi.yaml"             // El archivo YAML que Swagger intenta cargar
        };
    }

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http
                .csrf(AbstractHttpConfigurer::disable)
                .cors(Customizer.withDefaults())//defaul significa que va a leer el meth que se llame corsConfigurationSource
                .sessionManagement(sess->sess.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .authorizeHttpRequests(req->
                        req
                                .requestMatchers(getPermittedUrls()).permitAll()
                                .anyRequest().authenticated()
                )
                .exceptionHandling(ex -> ex.authenticationEntryPoint(jwtEntryPoint))
                .authenticationProvider(authenticationProvider)
                .addFilterBefore(jwtAuthFilter, UsernamePasswordAuthenticationFilter.class);

        return http.build();
    }

}
