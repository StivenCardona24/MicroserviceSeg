package com.uniquindio.api_rest.infra.security.config;

import com.uniquindio.api_rest.infra.security.services.JwtService;
import com.uniquindio.api_rest.infra.security.services.UserDetailsServiceImpl;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

@Component
@RequiredArgsConstructor
public class JwtAuthenticationFilter extends OncePerRequestFilter {
    private final JwtService jwtService;
    private final UserDetailsServiceImpl userDetailsServiceImpl;

    @Override
    protected void doFilterInternal(HttpServletRequest req, HttpServletResponse res, FilterChain chain) throws ServletException, IOException {

        // Obtener el token del encabezado de la solicitud
        String token = getToken(req);
        try {
            if (token != null) {
                System.out.println("token nul");
                String username = jwtService.extractUsername(token);

                // Asegurarse de que no haya un usuario autenticado ya en el contexto
                if (username != null && SecurityContextHolder.getContext().getAuthentication() == null) {
                    System.out.println("get auth");
                    UserDetails userDetails = userDetailsServiceImpl.loadUserByUsername(username);

                    //Verificar si el token es válido
                    if (jwtService.isTokenValid(token, userDetails)) {
                        System.out.println("valid");
                        setAuthenticationContext(userDetails);
                    }
                }
            }
        } catch (UsernameNotFoundException e) {
            System.out.println("gripa");
            // Manejo de excepción si el usuario no es encontrado
            e.printStackTrace();
        }
        // Continuar con la cadena de filtros
        chain.doFilter(req, res);
    }

    private void setAuthenticationContext(UserDetails userDetails) {
        UsernamePasswordAuthenticationToken authenticationToken =
                new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities());
        SecurityContextHolder.getContext().setAuthentication(authenticationToken);
    }


    private String getToken(HttpServletRequest req) {
        String header = req.getHeader("Authorization");
        if (header != null && header.startsWith("Bearer "))
            return header.replace("Bearer ", "");
        return null;
    }
}
