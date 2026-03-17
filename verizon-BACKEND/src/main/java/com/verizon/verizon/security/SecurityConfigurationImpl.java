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
public class SecurityConfigurationImpl implements SecurityConfigurations{
    private final JwtAuthenticationFilter jwtAuthFilter;
    SecurityConfigurationImpl(JwtAuthenticationFilter jwtAuthFilter){
        this.jwtAuthFilter = jwtAuthFilter;
    }
    @Override
    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity httpSecurity) {
        try {
            return httpSecurity
                    .cors(cors -> cors.configurationSource(corsConfigurationSource()))
                    .csrf(AbstractHttpConfigurer::disable)
                    .authorizeHttpRequests(auth -> auth
                            // PUBLIC ENDPOINTS - No authentication required
                            .requestMatchers(
                                    // Auth endpoints
                                    "/api/auth/**",
                                    "/api/auth/login",
                                    "/api/auth/register",
                                    "/api/auth/refresh",

                                    // User registration - MAKE THIS PUBLIC!
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
                                    "/api/users/**",        // All user operations except register
                                    "/api/users/profile",   // User profile
                                    "/api/users/update",    // Update user
                                    "/api/users/delete",    // Delete user
                                    "/api/roles/**",        // Role management
                                    "/api/admin/**"         // Admin only endpoints
                            ).authenticated()

                            // Allow all other requests (for Flutter routing)
                            .anyRequest().permitAll()
                    )
                    .sessionManagement(session -> session
                            .sessionCreationPolicy(SessionCreationPolicy.STATELESS)
                    )
                    .exceptionHandling(exception -> exception
                            .authenticationEntryPoint((request, response, authException) -> {
                                // Only return 401 for API calls, not for Flutter routes
                                if (request.getRequestURI().startsWith("/api/")) {
                                    response.setContentType("application/json");
                                    response.setStatus(401);
                                    response.getWriter().write(
                                            "{\"error\": \"Unauthorized\", \"message\": \"Authentication required\"}"
                                    );
                                } else {
                                    // For non-API routes, let it through (Flutter will handle)
                                    response.setStatus(200);
                                    // Serve index.html for Flutter routing
                                    request.getRequestDispatcher("/").forward(request, response);
                                }
                            })
                    )
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
        configuration.setAllowedOrigins(
                List.of(
                        "http://localhost:3000",
                        "http://localhost:8080",
                        "http://localhost:5555",  // Flutter web default
                        "http://127.0.0.1:5555",  // Flutter web alternative
                        "http://localhost",        // Flutter web
                        "http://127.0.0.1",
                        "http://localhost:59974" // Flutter web alternative
                ));
        configuration.setAllowedMethods(List.of(
                "GET", "POST", "PUT", "DELETE", "PATCH", "OPTIONS", "HEAD"
        ));
        configuration.setAllowedHeaders(List.of(
                "Authorization",
                "Content-Type",
                "Accept",
                "Origin",
                "X-Requested-With",
                "Access-Control-Request-Method",
                "Access-Control-Request-Headers"
        ));
        configuration.setExposedHeaders(List.of(
                "Authorization",
                "Content-Type"
        ));
        configuration.setAllowCredentials(true);
        configuration.setMaxAge(3600L);

        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", configuration);
        return source;
    }
}
