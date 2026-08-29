package com.verizon.verizon.service.jwtutils;

import com.verizon.verizon.entity.Roles;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.jspecify.annotations.NonNull;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.List;

@Component
public class JwtAuthenticationFilter extends OncePerRequestFilter {

    private final JwtTokenProviderImpl jwtTokenProviderImpl;

    public JwtAuthenticationFilter(JwtTokenProviderImpl jwtTokenProviderImpl) {
        this.jwtTokenProviderImpl = jwtTokenProviderImpl;
    }

    @Override
    protected void doFilterInternal(@NonNull HttpServletRequest request,
                                    @NonNull HttpServletResponse response,
                                    @NonNull FilterChain filterChain)
            throws ServletException, IOException {

        try {
            // a. Extract Token from request
            String token = extractTokenFromRequest(request);

            // b. Validate Token
            if (token != null && jwtTokenProviderImpl.validateToken(token)) {

                // c. Extract subject and roles
                String email = jwtTokenProviderImpl.extractEmail(token);
                Roles roles = jwtTokenProviderImpl.extractRoleFromToken(token);

                // d. Create Authentication Object
                UsernamePasswordAuthenticationToken auth =
                        new UsernamePasswordAuthenticationToken(
                                email,
                                null,
                                List.of(new SimpleGrantedAuthority("ROLE_" + roles.getName()))
                        );

                // e. Add Authentication Object to Security Context
                SecurityContextHolder.getContext().setAuthentication(auth);
            }

            // continue the filter chain
            filterChain.doFilter(request, response);

        } catch (Exception e) {
            SecurityContextHolder.clearContext();
            //Still need to continue the chain even on error
            filterChain.doFilter(request, response);
        }
    }

    // Add this to exclude login endpoints
    @Override
    protected boolean shouldNotFilter(@NonNull HttpServletRequest request) {
        String path = request.getRequestURI();
        return path.equals("/api/auth/signin") ||
                path.equals("/api/auth/signup");
    }

    // Extract Token from request - FIXED
    private String extractTokenFromRequest(HttpServletRequest request) {
        String bearerToken = request.getHeader("Authorization");
        if (bearerToken != null && bearerToken.startsWith("Bearer ")) {
            return bearerToken.substring(7);
        }
        return null;
    }


}