package tech3.binitright.config;

import org.springframework.boot.web.servlet.server.CookieSameSiteSupplier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.LogoutConfigurer;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.NoOpPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.header.writers.StaticHeadersWriter;

import static org.springframework.security.config.Customizer.withDefaults;


@Configuration
@EnableWebSecurity
public class SecurityConfig {


    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
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
                        .requestMatchers("/login", "/css/**", "/js/**", "/api/admin/create","/api/**").permitAll()
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

    /* * "FIX" for Plugin ID 10112: Session Management Response
     * This ensures the JSESSIONID is handled securely with SameSite attributes.
     */
    @Bean
    public CookieSameSiteSupplier applicationCookieSameSiteSupplier() {
        return CookieSameSiteSupplier.ofLax();
    }
}