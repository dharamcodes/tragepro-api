package com.tragepro.api.common.filter;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.UUID;
import lombok.NonNull;
import org.springframework.security.web.csrf.CsrfToken;
import org.springframework.security.web.csrf.DefaultCsrfToken;
import org.springframework.web.filter.OncePerRequestFilter;

public final class CsrfCookieFilter extends OncePerRequestFilter {

    private static final String DEFAULT_HEADER_NAME = "X-XSRF-TOKEN";
    private static final String DEFAULT_PARAMETER_NAME = "_csrf";

    @Override
    protected void doFilterInternal(
            @NonNull HttpServletRequest request,
            @NonNull HttpServletResponse response,
            @NonNull FilterChain filterChain)
            throws ServletException, IOException {
        CsrfToken csrfToken = (CsrfToken) request.getAttribute(CsrfToken.class.getName());
        if (csrfToken == null) {
            csrfToken = new DefaultCsrfToken(
                    DEFAULT_HEADER_NAME,
                    DEFAULT_PARAMETER_NAME,
                    UUID.randomUUID().toString());
            request.setAttribute(CsrfToken.class.getName(), csrfToken);
            request.setAttribute(DEFAULT_PARAMETER_NAME, csrfToken);
        }
        csrfToken.getToken();
        filterChain.doFilter(request, response);
    }
}
