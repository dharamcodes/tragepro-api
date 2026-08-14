package com.tragepro.api.core.web;

import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;

import com.tragepro.api.common.filter.SwaggerFilter;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import java.io.IOException;
import org.junit.jupiter.api.Test;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.mock.web.MockHttpServletResponse;

class SwaggerFilterTest {

  private final SwaggerFilter swaggerFilter = new SwaggerFilter();

  @Test
  void testDoFilterInternal_NotSwaggerInitializer() throws ServletException, IOException {
    MockHttpServletRequest request = new MockHttpServletRequest("GET", "/api/v1/health");
    MockHttpServletResponse response = new MockHttpServletResponse();
    FilterChain filterChain = mock(FilterChain.class);

    swaggerFilter.doFilterInternal(request, response, filterChain);

    verify(filterChain).doFilter(request, response);
  }

  @Test
  void testDoFilterInternal_SwaggerInitializer() throws ServletException, IOException {
    MockHttpServletRequest request =
        new MockHttpServletRequest("GET", "/swagger-ui/swagger-initializer.js");
    MockHttpServletResponse response = new MockHttpServletResponse();

    FilterChain filterChain =
        (req, res) -> {
          res.getWriter().write("window.ui = SwaggerUIBundle({ operationsSorter: \"custom\" });");
        };

    swaggerFilter.doFilterInternal(request, response, filterChain);

    String content = response.getContentAsString();
    assertTrue(content.contains("window.__operationsSorter"));
    assertTrue(content.contains("operationsSorter: window.__operationsSorter"));
  }
}
