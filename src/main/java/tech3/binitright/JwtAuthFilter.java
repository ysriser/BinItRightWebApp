package tech3.binitright;

import java.io.IOException;
import java.util.List;

import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import tech3.binitright.util.JwtUtil;

@Component
public final class JwtAuthFilter extends OncePerRequestFilter {
    private final JwtUtil jwtUtil;

    public JwtAuthFilter(final JwtUtil jwtUtil) {
        this.jwtUtil = jwtUtil;
    }

    @Override
    protected void doFilterInternal(
            final HttpServletRequest request,
            final HttpServletResponse response,
            final FilterChain filterChain
    ) throws ServletException, IOException {

        final String path = request.getRequestURI();

        // ✅ Only handle /api/** (ignore web admin and others)
        if (!path.startsWith("/api/") || path.equals("/api/auth/login") || path.equals("/api/auth/register")) {
            filterChain.doFilter(request, response);
            return;
        }

        // ✅ Extract JWT from Authorization header
        final String auth = request.getHeader("Authorization");

        if (auth != null && auth.startsWith("Bearer ")) {
            final String token = auth.substring(7);

            try {
                final String username = jwtUtil.extractUsername(token);

                if (username != null && SecurityContextHolder.getContext().getAuthentication() == null) {

                    // if you want role-based:
                    // String role = jwtUtil.extractRole(token);

                    final var authorities = List.of(new SimpleGrantedAuthority("ROLEUUSER"));

                    final var authentication = new UsernamePasswordAuthenticationToken(
                            username,
                            null,
                            authorities
                    );

                    SecurityContextHolder.getContext().setAuthentication(authentication);
                }
            } catch (final Exception e) {
                SecurityContextHolder.clearContext();
            }
        }

        filterChain.doFilter(request, response);
    }
}

