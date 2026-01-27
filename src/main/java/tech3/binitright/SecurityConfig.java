package tech3.binitright;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.header.writers.StaticHeadersWriter;
import org.springframework.boot.web.servlet.server.CookieSameSiteSupplier;

import static org.springframework.security.config.Customizer.withDefaults;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http
                .csrf(withDefaults())
                .headers(headers -> headers
                        .frameOptions(frame -> frame.deny())
                        .contentTypeOptions(withDefaults())
                        .contentSecurityPolicy(csp -> csp
                                .policyDirectives("default-src 'self'; frame-ancestors 'none'; form-action 'self';")
                        )
                        .addHeaderWriter(new StaticHeadersWriter("Permissions-Policy", "camera=(), microphone=(), geolocation=()"))
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
                        .requestMatchers("/robots.txt", "/sitemap.xml", "/css/**", "/js/**").permitAll()
                        .anyRequest().permitAll()
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