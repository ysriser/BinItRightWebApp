package tech3.binitright;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.header.writers.StaticHeadersWriter;

// THIS IS THE MISSING PIECE:
import static org.springframework.security.config.Customizer.withDefaults;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http
                // Fixes "Absence of Anti-CSRF Tokens"
                .csrf(withDefaults())

                .headers(headers -> headers
                        // Fixes "Missing Anti-clickjacking Header"
                        .frameOptions(frame -> frame.deny())

                        // Fixes "X-Content-Type-Options Header Missing"
                        .contentTypeOptions(withDefaults())

                        // Fixes "Content Security Policy (CSP) Header Not Set"
                        .contentSecurityPolicy(csp -> csp
                                .policyDirectives("default-src 'self';")
                        )

                        // Fixes "Insufficient Site Isolation Against Spectre"
                        .addHeaderWriter(new StaticHeadersWriter("Cross-Origin-Resource-Policy", "same-origin"))
                        .addHeaderWriter(new StaticHeadersWriter("Cross-Origin-Embedder-Policy", "require-corp"))
                        .addHeaderWriter(new StaticHeadersWriter("Cross-Origin-Opener-Policy", "same-origin"))
                )
                .authorizeHttpRequests(auth -> auth
                        .anyRequest().permitAll()
                );

        return http.build();
    }
}