package com.tragepro.api.common.filter;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import org.jspecify.annotations.NonNull;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;
import org.springframework.web.util.ContentCachingResponseWrapper;

@Component
@Order(Ordered.HIGHEST_PRECEDENCE)
public class SwaggerFilter extends OncePerRequestFilter {

    private static final String CUSTOM_SORTER_SCRIPT = """
            const __methodOrder = { post: 1, get: 2, put: 3, patch: 4, delete: 5 };
            window.__operationsSorter = function (a, b) {
              var orderA = __methodOrder[a.get('method')] ?? 99;
              var orderB = __methodOrder[b.get('method')] ?? 99;
              if (orderA !== orderB) return orderA - orderB;
              return a.get('path').localeCompare(b.get('path'));
            };
            """;

    @Override
    public void doFilterInternal(
            HttpServletRequest request, @NonNull HttpServletResponse response, @NonNull FilterChain filterChain)
            throws ServletException, IOException {
        if (!request.getRequestURI().endsWith("swagger-initializer.js")) {
            filterChain.doFilter(request, response);
            return;
        }
        ContentCachingResponseWrapper responseWrapper = new ContentCachingResponseWrapper(response);
        filterChain.doFilter(request, responseWrapper);
        String originalJs = new String(responseWrapper.getContentAsByteArray(), StandardCharsets.UTF_8);
        String modifiedJs = CUSTOM_SORTER_SCRIPT
                + originalJs.replace("operationsSorter: \"custom\"", "operationsSorter: window.__operationsSorter");
        byte[] modifiedBytes = modifiedJs.getBytes(StandardCharsets.UTF_8);
        response.setContentLength(modifiedBytes.length);
        response.getOutputStream().write(modifiedBytes);
        response.flushBuffer();
    }
}
