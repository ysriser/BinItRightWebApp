package tech3.binitright.config;

import org.springframework.boot.web.servlet.server.CookieSameSiteSupplier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.LogoutConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.NoOpPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.web.header.writers.StaticHeadersWriter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
import tech3.binitright.JwtAuthFilter;

import java.util.List;

import static org.springframework.security.config.Customizer.withDefaults;


@Configuration
@EnableWebSecurity
public class SecurityConfig {


    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }


    @Bean
    @Order(1)
    public SecurityFilterChain apiChain(HttpSecurity http, JwtAuthFilter jwtAuthFilter) throws Exception {
        http
                .securityMatcher("/api/**")
                .csrf(csrf -> csrf.disable())
                .cors(cors -> cors.configurationSource(corsConfigurationSource()))
                .sessionManagement(sm -> sm.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .authorizeHttpRequests(auth -> auth
                        // ✅ public endpoints for mobile
                        .requestMatchers("/api/auth/login", "/api/admin/create", "/api/bins/**", "/api/checkin", "/api/videos/presign-upload").permitAll()
                        .requestMatchers("/api/auth/login", "/api/admin/create", "/api/bins/**").permitAll()

                        // ✅ everything else in /api needs token
                        .anyRequest().authenticated()
                )
                // ✅ JWT filter checks Authorization header
                .addFilterBefore(jwtAuthFilter, UsernamePasswordAuthenticationFilter.class);

        return http.build();
    }
    @Bean
    @Order(2)
    public SecurityFilterChain filterChain(final HttpSecurity http) throws Exception {
        http
                .csrf(csrf -> csrf
                        .ignoringRequestMatchers("/api/**")
                )
                .headers(headers -> headers
                        .frameOptions(frame -> frame.deny())
                        .contentTypeOptions(withDefaults())
                        .contentSecurityPolicy(csp -> csp
                                .policyDirectives("default-src 'self'; "
                                        + "style-src 'self' https://fonts.googleapis.com https://cdnjs.cloudflare.com; " // Allow FontAwesome & Google Styles
                                        + "font-src 'self' https://fonts.gstatic.com https://cdnjs.cloudflare.com; "
                                        +"script-src 'self' 'unsafe-inline' https://cdnjs.cloudflare.com; "
                                        + "img-src 'self' data: https:; "
                                        + "media-src 'self' https://*.digitaloceanspaces.com; "
                                        + "connect-src 'self' https://*.digitaloceanspaces.com; "
                                        +"style-src 'self' 'unsafe-inline' https://fonts.googleapis.com https://cdnjs.cloudflare.com;"// Allow FontAwesome & Google Fonts
                                        + "frame-ancestors 'none'; form-action 'self';")
                        )
                        .addHeaderWriter(new StaticHeadersWriter(
                                "Permissions-Policy",
                                "camera=(), microphone=(), geolocation=()"))
                        .addHeaderWriter(new StaticHeadersWriter("Cross-Origin-Resource-Policy", "same-origin"))
                        .addHeaderWriter(new StaticHeadersWriter("Cross-Origin-Embedder-Policy", "require-corp"))
                        .addHeaderWriter(new StaticHeadersWriter("Cross-Origin-Opener-Policy", "same-origin"))

                        /* * "FIX" for Plugin ID 10049: Non-Storable Content
                         * This ensures that cache-control headers are explicitly managed.
                         * While Spring disables caching for secure requests by default,
                         * explicitly defining it clears the "heuristic" warnings in ZAP.
                         */
                        .cacheControl(withDefaults())
                )
                .authorizeHttpRequests(auth -> auth
                        .requestMatchers("/login", "/css/**", "/js/**", "/api/admin/create", "/api/**").permitAll()
                        .requestMatchers("/admin/**").hasRole("admin") // Only admins allowed
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
                        .invalidateHttpSession(true)   // Explicitly kill the session
                        .clearAuthentication(true)     // Clear security context
                        .deleteCookies("JSESSIONID")   // Remove the session cookie
                        .permitAll()
                );

        return http.build();

    }
    @Bean
    public CorsConfigurationSource corsConfigurationSource() {
        CorsConfiguration config = new CorsConfiguration();
        // For dev: ok. In production, replace with your real domain(s).
        config.setAllowedOrigins(List.of("*"));
        config.setAllowedMethods(List.of("GET", "POST", "PUT", "DELETE", "OPTIONS"));
        config.setAllowedHeaders(List.of("Authorization", "Content-Type"));
        config.setExposedHeaders(List.of("Authorization")); // optional
        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", config);
        return source;
    }

    /* * "FIX" for Plugin ID 10112: Session Management Response
     * This ensures the JSESSIONID is handled securely with SameSite attributes.
     */
    @Bean
    public CookieSameSiteSupplier applicationCookieSameSiteSupplier() {
        return CookieSameSiteSupplier.ofLax();
    }
}