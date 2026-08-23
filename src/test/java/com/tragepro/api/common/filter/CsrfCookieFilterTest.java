package com.tragepro.api.common.filter;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.Mockito.verify;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import java.io.IOException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.security.web.csrf.CsrfToken;
import org.springframework.security.web.csrf.DefaultCsrfToken;

@ExtendWith(MockitoExtension.class)
class CsrfCookieFilterTest {

    private CsrfCookieFilter csrfCookieFilter;
    private MockHttpServletRequest request;
    private MockHttpServletResponse response;

    @Mock
    private FilterChain filterChain;

    @BeforeEach
    void setUp() {
        csrfCookieFilter = new CsrfCookieFilter();
        request = new MockHttpServletRequest();
        response = new MockHttpServletResponse();
    }

    @Test
    void testDoFilterInternal_WhenCsrfTokenIsNull_SetsDefaultToken() throws ServletException, IOException {
        csrfCookieFilter.doFilterInternal(request, response, filterChain);

        CsrfToken setToken = (CsrfToken) request.getAttribute(CsrfToken.class.getName());
        assertNotNull(setToken);
        assertEquals("X-XSRF-TOKEN", setToken.getHeaderName());
        assertEquals("_csrf", setToken.getParameterName());
        assertNotNull(setToken.getToken());
        assertEquals(setToken, request.getAttribute("_csrf"));

        verify(filterChain).doFilter(request, response);
    }

    @Test
    void testDoFilterInternal_WhenCsrfTokenExists_UsesExistingToken() throws ServletException, IOException {
        DefaultCsrfToken existingToken = new DefaultCsrfToken("X-XSRF-TOKEN", "_csrf", "custom-token-value");
        request.setAttribute(CsrfToken.class.getName(), existingToken);

        csrfCookieFilter.doFilterInternal(request, response, filterChain);

        CsrfToken setToken = (CsrfToken) request.getAttribute(CsrfToken.class.getName());
        assertEquals(existingToken, setToken);
        assertEquals("custom-token-value", setToken.getToken());

        verify(filterChain).doFilter(request, response);
    }
}
