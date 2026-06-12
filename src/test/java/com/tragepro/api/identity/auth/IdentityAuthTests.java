package com.tragepro.api.identity.auth;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import com.tragepro.api.common.exception.AppException;
import com.tragepro.api.common.exception.constant.ErrorType;
import com.tragepro.api.identity.auth.constant.RoleType;
import com.tragepro.api.identity.auth.filter.JWTAuthFilter;
import com.tragepro.api.identity.auth.helper.EmailHelper;
import com.tragepro.api.identity.auth.helper.JwtTokenHelper;
import com.tragepro.api.identity.auth.model.entity.AuthenticationEntity;
import com.tragepro.api.identity.auth.model.request.AuthenticationRequest;
import com.tragepro.api.identity.auth.model.response.AuthenticationResponse;
import com.tragepro.api.identity.auth.service.UserDetailService;
import com.tragepro.api.identity.auth.service.mapper.AuthenticationMapper;
import com.tragepro.api.identity.auth.service.mapper.AuthenticationMapperImpl;
import jakarta.servlet.FilterChain;
import jakarta.servlet.http.HttpServletResponse;
import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.Map;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;

@ExtendWith(MockitoExtension.class)
class IdentityAuthTests {

    @Mock
    private UserDetailService userDetailService;

    @Mock
    private FilterChain filterChain;

    private JWTAuthFilter jwtAuthFilter;

    @BeforeEach
    void setUp() {
        jwtAuthFilter = new JWTAuthFilter(userDetailService);
        SecurityContextHolder.clearContext();
    }

    @Test
    void testJwtTokenHelperPrivateConstructor() throws Exception {
        Constructor<JwtTokenHelper> constructor = JwtTokenHelper.class.getDeclaredConstructor();
        constructor.setAccessible(true);
        try {
            constructor.newInstance();
            fail("Expected InvocationTargetException");
        } catch (InvocationTargetException e) {
            assertTrue(e.getTargetException() instanceof UnsupportedOperationException);
        }
    }

    @Test
    void testEmailHelperPrivateConstructor() throws Exception {
        Constructor<EmailHelper> constructor = EmailHelper.class.getDeclaredConstructor();
        constructor.setAccessible(true);
        try {
            constructor.newInstance();
            fail("Expected InvocationTargetException");
        } catch (InvocationTargetException e) {
            assertTrue(e.getTargetException() instanceof UnsupportedOperationException);
        }
    }

    @Test
    void testEmailHelperMethods() {
        EmailHelper.sendEmail("recipient@example.com", "subject", "body");
        assertTrue(EmailHelper.sendPasswordResetEmail("recipient@example.com", "token"));
    }

    @Test
    void testJwtTokenHelperLifecycleAndValidation() {
        String username = "testUser";
        Map<String, String> claims = Map.of("role", "APP_USER");

        String token = JwtTokenHelper.generateToken(username, claims);
        assertNotNull(token);
        assertEquals(username, JwtTokenHelper.extractUsername(token));

        UserDetails userDetails = new User(username, "password", new ArrayList<>());
        assertTrue(JwtTokenHelper.validateToken(token, userDetails));

        String resetToken = JwtTokenHelper.generateResetPasswordToken(username, claims);
        assertNotNull(resetToken);
        assertEquals(username, JwtTokenHelper.extractUsername(resetToken));

        String invalidToken = token + "modifiedSignature";
        AppException exception = assertThrows(AppException.class, () -> JwtTokenHelper.getTokenBody(invalidToken));
        assertEquals(ErrorType.DATA_NOT_FOUND, exception.getErrorType());
    }

    @Test
    void testJWTAuthFilterPublicEndpoints() throws Exception {
        String[] publicEndpoints = {
            "/swagger-ui/index.html",
            "/swagger-ui.html",
            "/v3/api-docs",
            "/swagger-resources/configuration/ui",
            "/webjars/springfox-swagger-ui/springfox.css",
            "/api/v1/auth/login",
            "/api/v1/auth/signup"
        };

        for (String endpoint : publicEndpoints) {
            MockHttpServletRequest request = new MockHttpServletRequest();
            request.setRequestURI(endpoint);
            MockHttpServletResponse response = new MockHttpServletResponse();
            FilterChain mockChain = mock(FilterChain.class);

            jwtAuthFilter.doFilter(request, response, mockChain);
            verify(mockChain).doFilter(request, response);
        }
    }

    @Test
    void testJWTAuthFilterMissingOrInvalidHeader() throws Exception {
        MockHttpServletRequest request = new MockHttpServletRequest();
        request.setRequestURI("/api/v1/candles");
        MockHttpServletResponse response = new MockHttpServletResponse();

        jwtAuthFilter.doFilter(request, response, filterChain);
        verify(filterChain, times(1)).doFilter(request, response);

        request.addHeader("Authorization", "Basic dXNlcjpwYXNz");
        jwtAuthFilter.doFilter(request, response, filterChain);
        verify(filterChain, times(2)).doFilter(request, response);
    }

    @Test
    void testJWTAuthFilterValidTokenRoles() throws Exception {
        RoleType[] roles = {RoleType.APP_USER, RoleType.APP_MANAGER, RoleType.APP_ADMIN, RoleType.SUPER_USER};

        for (RoleType role : roles) {
            SecurityContextHolder.clearContext();
            String token = JwtTokenHelper.generateToken("testUser", Map.of("role", role.name()));

            MockHttpServletRequest request = new MockHttpServletRequest();
            request.setRequestURI("/api/v1/candles");
            request.addHeader("Authorization", "Bearer " + token);
            MockHttpServletResponse response = new MockHttpServletResponse();
            FilterChain mockChain = mock(FilterChain.class);

            UserDetails userDetails = new User("testUser", "password", new ArrayList<>());
            when(userDetailService.loadUserByUsername("testUser")).thenReturn(userDetails);

            jwtAuthFilter.doFilter(request, response, mockChain);
            verify(mockChain).doFilter(request, response);
            assertNotNull(SecurityContextHolder.getContext().getAuthentication());
        }
    }

    @Test
    void testJWTAuthFilterResetPasswordSuccess() throws Exception {
        String token = JwtTokenHelper.generateResetPasswordToken(
                "testUser",
                Map.of(
                        "role", RoleType.APP_USER.name(),
                        "passwordReset", RoleType.PASSWORD_RESET_CLAIM.getValue()));

        MockHttpServletRequest request = new MockHttpServletRequest();
        request.setRequestURI("/api/v1/reset-password");
        request.addHeader("Authorization", "Bearer " + token);
        MockHttpServletResponse response = new MockHttpServletResponse();
        FilterChain mockChain = mock(FilterChain.class);

        UserDetails userDetails = new User("testUser", "password", new ArrayList<>());
        when(userDetailService.loadUserByUsername("testUser")).thenReturn(userDetails);

        jwtAuthFilter.doFilter(request, response, mockChain);
        verify(mockChain).doFilter(request, response);
    }

    @Test
    void testJWTAuthFilterResetPasswordDenied() throws Exception {
        String token = JwtTokenHelper.generateResetPasswordToken(
                "testUser", Map.of("role", RoleType.APP_USER.name(), "passwordReset", "INVALID_CLAIM_VALUE"));

        MockHttpServletRequest request = new MockHttpServletRequest();
        request.setRequestURI("/api/v1/reset-password");
        request.addHeader("Authorization", "Bearer " + token);
        MockHttpServletResponse response = new MockHttpServletResponse();

        UserDetails userDetails = new User("testUser", "password", new ArrayList<>());
        when(userDetailService.loadUserByUsername("testUser")).thenReturn(userDetails);

        jwtAuthFilter.doFilter(request, response, filterChain);
        assertEquals(HttpServletResponse.SC_UNAUTHORIZED, response.getStatus());
        assertTrue(response.getContentAsString().contains("Invalid or expired token"));
    }

    @Test
    void testJWTAuthFilterInvalidTokenException() throws Exception {
        MockHttpServletRequest request = new MockHttpServletRequest();
        request.setRequestURI("/api/v1/candles");
        request.addHeader("Authorization", "Bearer invalidtokenhere");
        MockHttpServletResponse response = new MockHttpServletResponse();

        jwtAuthFilter.doFilter(request, response, filterChain);
        assertEquals(HttpServletResponse.SC_UNAUTHORIZED, response.getStatus());
    }

    @Test
    void testAuthenticationMapper() {
        AuthenticationMapper mapper = new AuthenticationMapperImpl();
        assertNull(mapper.requestToEntity(null));
        assertNull(mapper.entityToResponse(null));

        AuthenticationEntity target = new AuthenticationEntity(null, null, null, null, null, null, null);
        mapper.merge(null, target);

        AuthenticationRequest request = AuthenticationRequest.builder()
                .userName("username")
                .email("email@example.com")
                .password("password")
                .role(RoleType.APP_USER)
                .isActive(true)
                .build();

        AuthenticationEntity entity = mapper.requestToEntity(request);
        assertNotNull(entity);
        assertEquals("username", entity.getUserName());

        AuthenticationResponse response = mapper.entityToResponse(entity);
        assertNotNull(response);
        assertEquals("username", response.userName());

        AuthenticationEntity merged = new AuthenticationEntity(null, null, null, null, null, null, null);
        mapper.merge(request, merged);
        assertEquals("username", merged.getUserName());
    }
}
