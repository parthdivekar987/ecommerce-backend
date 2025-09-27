package com.zosh.config;

import jakarta.servlet.http.HttpServletRequest;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.www.BasicAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;

import java.util.Arrays;
import java.util.Collections;

@Configuration
public class AppConfig {

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                // Use stateless session management as we are using JWT
                .sessionManagement(management -> management.sessionCreationPolicy(SessionCreationPolicy.STATELESS))

                // Define authorization rules for different endpoints
                .authorizeHttpRequests(authorize -> authorize
                        // --- Public Endpoints ---
                        // Anyone can access authentication endpoints (login, register)
                        .requestMatchers("/auth/**").permitAll()
                        // Anyone can view products
                        .requestMatchers("/api/products/**").permitAll()

                        // --- Admin-Only Endpoints ---
                        // Only users with the 'ADMIN' role can access admin endpoints
                        .requestMatchers("/api/admin/**").hasRole("ADMIN")

                        // --- All Other Requests ---
                        // Any other request to the API must be authenticated
                        .anyRequest().authenticated()
                )

                // Add custom JWT validator filter before the default basic auth filter
                .addFilterBefore(new JwtValidator(), BasicAuthenticationFilter.class)

                // Disable CSRF protection (common for stateless APIs)
                .csrf(csrf -> csrf.disable())

                // Configure CORS
                .cors(cors -> cors.configurationSource(corsConfigurationSource()))

                // Disable default HTTP Basic and form login
                .httpBasic(basic -> basic.disable())
                .formLogin(form -> form.disable());

        return http.build();
    }

    // A helper method to configure CORS settings
    private CorsConfigurationSource corsConfigurationSource() {
        return request -> {
            CorsConfiguration cfg = new CorsConfiguration();

            // Define allowed origins (your frontend URLs)
            cfg.setAllowedOrigins(Arrays.asList(
                    "http://localhost:3000",
                    "https://insaneshopcart-bycart.vercel.app"
            ));

            // Allow all standard HTTP methods
            cfg.setAllowedMethods(Collections.singletonList("*"));
            // Allow credentials (cookies, authorization headers)
            cfg.setAllowCredentials(true);
            // Allow all headers
            cfg.setAllowedHeaders(Collections.singletonList("*"));
            // Expose the 'Authorization' header so the frontend can read the JWT
            cfg.setExposedHeaders(Arrays.asList("Authorization"));
            cfg.setMaxAge(3600L);
            return cfg;
        };
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
}