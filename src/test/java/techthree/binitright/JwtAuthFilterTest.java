package techthree.binitright;

import jakarta.servlet.ServletException;
import java.io.IOException;
import java.util.Collection;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.mock.web.MockFilterChain;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import techthree.binitright.util.JwtUtil;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

class JwtAuthFilterTest {

    private JwtUtil jwtUtil;
    private JwtAuthFilter jwtAuthFilter;

    @BeforeEach
    void setUp() {
        SecurityContextHolder.clearContext();
        jwtUtil = Mockito.mock(JwtUtil.class);
        jwtAuthFilter = new JwtAuthFilter(jwtUtil);
    }

    @AfterEach
    void tearDown() {
        SecurityContextHolder.clearContext();
    }

    @Test
    void nonApiPathSkipsJwtExtraction() throws ServletException, IOException {
        final MockHttpServletRequest request = new MockHttpServletRequest();
        request.setRequestURI("/home");

        final MockHttpServletResponse response = new MockHttpServletResponse();
        final MockFilterChain chain = new MockFilterChain();

        jwtAuthFilter.doFilter(request, response, chain);

        verify(jwtUtil, never()).extractUsername(Mockito.anyString());
        assertNull(SecurityContextHolder.getContext().getAuthentication());
    }

    @Test
    void loginPathSkipsJwtExtraction() throws ServletException, IOException {
        final MockHttpServletRequest request = new MockHttpServletRequest();
        request.setRequestURI("/api/auth/login");

        final MockHttpServletResponse response = new MockHttpServletResponse();
        final MockFilterChain chain = new MockFilterChain();

        jwtAuthFilter.doFilter(request, response, chain);

        verify(jwtUtil, never()).extractUsername(Mockito.anyString());
        assertNull(SecurityContextHolder.getContext().getAuthentication());
    }

    @Test
    void validBearerTokenSetsAuthentication() throws ServletException, IOException {
        final MockHttpServletRequest request = new MockHttpServletRequest();
        request.setRequestURI("/api/news");
        request.addHeader("Authorization", "Bearer valid-token");

        when(jwtUtil.extractUsername("valid-token")).thenReturn("user1");

        final MockHttpServletResponse response = new MockHttpServletResponse();
        final MockFilterChain chain = new MockFilterChain();

        jwtAuthFilter.doFilter(request, response, chain);

        final Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        assertNotNull(auth);
        assertEquals("user1", auth.getPrincipal());
        final Collection<?> authorities = auth.getAuthorities();
        assertEquals(1, authorities.size());
        assertTrue(authorities.iterator().next().toString().contains("ROLE_USER"));
    }

    @Test
    void invalidBearerTokenKeepsContextEmpty() throws ServletException, IOException {
        final MockHttpServletRequest request = new MockHttpServletRequest();
        request.setRequestURI("/api/news");
        request.addHeader("Authorization", "Bearer invalid-token");

        when(jwtUtil.extractUsername("invalid-token")).thenThrow(new RuntimeException("bad token"));

        final MockHttpServletResponse response = new MockHttpServletResponse();
        final MockFilterChain chain = new MockFilterChain();

        jwtAuthFilter.doFilter(request, response, chain);

        assertNull(SecurityContextHolder.getContext().getAuthentication());
        verify(jwtUtil).extractUsername("invalid-token");
    }
}
