package com.verizon.verizon.security;

import com.verizon.verizon.service.jwtutils.JwtAuthenticationFilter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import java.util.List;

@Configuration
public class SecurityConfigurationImpl implements SecurityConfigurations {
    private final JwtAuthenticationFilter jwtAuthFilter;

    SecurityConfigurationImpl(JwtAuthenticationFilter jwtAuthFilter) {
        this.jwtAuthFilter = jwtAuthFilter;
    }

    @Override
    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity httpSecurity) {
        try {
            return httpSecurity
                    // Disable CSRF
                    .csrf(AbstractHttpConfigurer::disable)

                    // Configure CORS
                    .cors(cors -> cors.configurationSource(corsConfigurationSource()))

                    // Configure authorization
                    .authorizeHttpRequests(auth -> auth
                            // IMPORTANT: Allow OPTIONS requests for all endpoints
                            .requestMatchers(org.springframework.http.HttpMethod.OPTIONS, "/**").permitAll()

                            // PUBLIC ENDPOINTS - No authentication required
                            .requestMatchers(
                                    // Auth endpoints
                                    "/api/auth/**",
                                    "/api/auth/login",
                                    "/api/auth/register",
                                    "/api/auth/refresh",
                                    "/api/auth/test",
                                    "/api/auth/test-cors",
                                    "/api/auth/test-post",

                                    // User registration
                                    "/api/users/register",
                                    "/api/public/**",
                                    "/api/public/users",

                                    // Flutter web resources
                                    "/",
                                    "/index.html",
                                    "/static/**",
                                    "/assets/**",
                                    "/main.dart.js",
                                    "/flutter_service_worker.js",
                                    "/manifest.json",
                                    "/icons/**",
                                    "/*.js",
                                    "/*.css",
                                    "/*.png",
                                    "/*.jpg",

                                    // Swagger/API docs
                                    "/swagger-ui/**",
                                    "/v3/api-docs/**",

                                    // Error handling
                                    "/error",
                                    "/favicon.ico"
                            ).permitAll()

                            // PROTECTED ENDPOINTS - Need JWT token
                            .requestMatchers(
                                    "/api/users/**",
                                    "/api/users/profile",
                                    "/api/users/update",
                                    "/api/users/delete",
                                    "/api/roles/**",
                                    "/api/admin/**"
                            ).authenticated()

                            // Allow all other requests
                            .anyRequest().permitAll()
                    )

                    // Use stateless session
                    .sessionManagement(session -> session
                            .sessionCreationPolicy(SessionCreationPolicy.STATELESS)
                    )

                    // Add JWT filter
                    .addFilterBefore(jwtAuthFilter, UsernamePasswordAuthenticationFilter.class)

                    .build();

        } catch (Exception e) {
            throw new RuntimeException("Security configuration failed", e);
        }
    }

    @Override
    @Bean
    public PasswordEncoder encodePassword() {
        return new BCryptPasswordEncoder();
    }

    @Override
    @Bean
    public CorsConfigurationSource corsConfigurationSource() {
        CorsConfiguration configuration = new CorsConfiguration();

        // Allow all origins (you can restrict this later)
        configuration.setAllowedOrigins(List.of("*"));

        // Allow all methods
        configuration.setAllowedMethods(List.of(
                "GET", "POST", "PUT", "DELETE", "PATCH", "OPTIONS", "HEAD"
        ));

        // Allow all headers
        configuration.setAllowedHeaders(List.of("*"));

        // Expose headers to browser
        configuration.setExposedHeaders(List.of(
                "Authorization",
                "Content-Type"
        ));

        // Allow credentials
        configuration.setAllowCredentials(true);

        // Cache preflight response for 1 hour
        configuration.setMaxAge(3600L);

        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", configuration);
        return source;
    }
}