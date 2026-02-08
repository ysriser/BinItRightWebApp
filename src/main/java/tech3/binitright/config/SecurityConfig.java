package tech3.binitright.config;

import static org.springframework.security.config.Customizer.withDefaults;

import java.util.List;

import org.springframework.boot.web.servlet.server.CookieSameSiteSupplier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.web.header.writers.StaticHeadersWriter;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;
import org.springframework.security.web.util.matcher.NegatedRequestMatcher;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import jakarta.servlet.http.HttpServletResponse;
import tech3.binitright.JwtAuthFilter;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    @Order(1)
    public SecurityFilterChain apiChain(
            final HttpSecurity http,
            final JwtAuthFilter jwtAuthFilter
    ) throws Exception {
        applyGlobalSecurityHeaders(http); // Apply shared headers
        http
                .securityMatcher("/api/**", "/error", "/v3/api-docs/**", "/swagger-ui/**", "/swagger-ui.html")
                .csrf(csrf -> csrf.disable())
                .cors(cors -> cors.configurationSource(corsConfigurationSource()))
                .sessionManagement(sm -> sm.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .authorizeHttpRequests(auth -> auth
                        .requestMatchers(
                                "/api/auth/login",
                                "/api/admin/create",
                                "/api/bins/**",
                                "/api/bins/nearby",
                                "/api/bins/all",
                                "/api/checkin",
                                "/api/videos/presign-upload",
                                "/api/recycle-history",
                                "/api/auth/register",
                                "/api/user/profile/**",
                                "/api/chat",
                                "/api/ready",
                                "/error",
                                "/api/forecast",
                                "/v3/api-docs/**",
                                "/swagger-ui/**",
                                "/swagger-ui.html"// CRITICAL: Permit /error
                        ).permitAll()
                        .anyRequest().authenticated()
                )
                .exceptionHandling(ex -> ex
                        .authenticationEntryPoint((request, response, authException) -> {
                            response.sendError(401, "Unauthorized");
                        })
                )
                .addFilterBefore(jwtAuthFilter, UsernamePasswordAuthenticationFilter.class)
                .exceptionHandling(ex -> ex
                        .authenticationEntryPoint((request, response, authException) -> {
                            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
                            response.setContentType("application/json");
                            response.getWriter().write("{\"error\": \"Unauthorized\"}");
                        })
                );

        return http.build();
    }

    @Bean
    @Order(2)
    public SecurityFilterChain filterChain(final HttpSecurity http) throws Exception {
        applyGlobalSecurityHeaders(http); // Apply shared headers
        http
                .securityMatcher(new NegatedRequestMatcher(new AntPathRequestMatcher("/api/**")))
                .csrf(csrf -> csrf.ignoringRequestMatchers("/api/**"))
                .headers(headers -> headers
                        .frameOptions(frame -> frame.deny())
                        .contentTypeOptions(withDefaults())
                        .contentSecurityPolicy(csp -> csp.policyDirectives(
                                "default-src 'self'; "
                                        + "style-src 'self' https://fonts.googleapis.com https://cdnjs.cloudflare.com; "
                                        + "font-src 'self' https://fonts.gstatic.com https://cdnjs.cloudflare.com; "
                                        + "script-src 'self' 'unsafe-inline' https://cdnjs.cloudflare.com; "
                                        + "img-src 'self' data: https:; "
                                        + "media-src 'self' https://*.digitaloceanspaces.com; "
                                        + "connect-src 'self' https://*.digitaloceanspaces.com; "
                                        + "style-src 'self' 'unsafe-inline' https://fonts.googleapis.com https://cdnjs.cloudflare.com;"
                                        + "frame-ancestors 'none'; form-action 'self';")
                        )
                        .addHeaderWriter(new StaticHeadersWriter(
                                "Permissions-Policy",
                                "camera=(), microphone=(), geolocation=()"
                        ))
                        .addHeaderWriter(new StaticHeadersWriter("Cross-Origin-Resource-Policy", "same-origin"))
                        .addHeaderWriter(new StaticHeadersWriter("Cross-Origin-Embedder-Policy", "require-corp"))
                        .addHeaderWriter(new StaticHeadersWriter("Cross-Origin-Opener-Policy", "same-origin"))
                        .cacheControl(withDefaults())
                )
                .authorizeHttpRequests(auth -> auth
                        .requestMatchers("/login", "/css/**", "/js/**", "/api/admin/create", "/v3/api-docs/**", "/swagger-ui/**", "/swagger-ui.html").permitAll()
                        .requestMatchers("/admin/**").hasRole("admin")
                        .anyRequest().authenticated()
                )
                .formLogin(form -> form
                        .loginPage("/login")
                        .defaultSuccessUrl("/admin/dashboard", true)
                        .permitAll()
                )
                .logout(logout -> logout
                        .logoutUrl("/logout")
                        .logoutSuccessUrl("/login?logout")
                        .invalidateHttpSession(true)
                        .clearAuthentication(true)
                        .deleteCookies("JSESSIONID")
                        .permitAll()
                );

        return http.build();
    }

    @Bean
    public CorsConfigurationSource corsConfigurationSource() {
        final CorsConfiguration config = new CorsConfiguration();
        config.setAllowedOriginPatterns(List.of("*"));
        config.setAllowedMethods(List.of("GET", "POST", "PUT", "DELETE", "OPTIONS"));
        config.setAllowedHeaders(List.of("*"));
        config.setExposedHeaders(List.of("Authorization"));
        config.setAllowCredentials(false);
        config.setMaxAge(3600L);

        final UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", config);
        return source;
    }

    @Bean
    public CookieSameSiteSupplier applicationCookieSameSiteSupplier() {
        return CookieSameSiteSupplier.ofLax();
    }

    private void applyGlobalSecurityHeaders(final HttpSecurity http) throws Exception {
        http.headers(headers -> headers
                .frameOptions(frame -> frame.deny())
                .contentTypeOptions(withDefaults())
                .contentSecurityPolicy(csp -> csp.policyDirectives(
                        "default-src 'self'; " +
                                // Combined style-src to fix "Duplicate directive" Low risk
                                "style-src 'self' 'unsafe-inline' https://fonts.googleapis.com https://cdnjs.cloudflare.com; " +
                                "font-src 'self' https://fonts.gstatic.com https://cdnjs.cloudflare.com; " +
                                // Removed 'unsafe-inline' to fix Medium risk
                                "script-src 'self' https://cdnjs.cloudflare.com; " +
                                // Fixed Wildcard: restricted to specific DigitalOcean storage
                                "img-src 'self' data: https://*.digitaloceanspaces.com; " +
                                "media-src 'self' https://*.digitaloceanspaces.com; " +
                                "connect-src 'self' https://*.digitaloceanspaces.com; " +
                                "frame-ancestors 'none'; form-action 'self';")
                )
                .addHeaderWriter(new StaticHeadersWriter("Permissions-Policy",
                        "camera=(), microphone=(), geolocation=()"))
                // Spectre isolation headers
                .addHeaderWriter(new StaticHeadersWriter("Cross-Origin-Resource-Policy", "same-origin"))
                .addHeaderWriter(new StaticHeadersWriter("Cross-Origin-Embedder-Policy", "require-corp"))
                .addHeaderWriter(new StaticHeadersWriter("Cross-Origin-Opener-Policy", "same-origin"))
        );
    }
}

