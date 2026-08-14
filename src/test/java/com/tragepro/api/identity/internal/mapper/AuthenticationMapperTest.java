package com.tragepro.api.identity.internal.mapper;

import static org.junit.jupiter.api.Assertions.*;

import com.tragepro.api.identity.constant.RoleType;
import com.tragepro.api.identity.model.entity.AuthenticationEntity;
import com.tragepro.api.identity.model.request.AuthenticationRequest;
import com.tragepro.api.identity.model.response.AuthenticationResponse;
import org.junit.jupiter.api.Test;

class AuthenticationMapperTest {

  @Test
  void testAuthenticationMapper() {
    AuthenticationMapper mapper = new AuthenticationMapperImpl();
    assertNull(mapper.requestToEntity(null));
    assertNull(mapper.entityToResponse(null));

    AuthenticationEntity target =
        new AuthenticationEntity(null, null, null, null, null, null, null);
    mapper.merge(null, target);

    AuthenticationRequest request =
        AuthenticationRequest.builder()
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

    AuthenticationEntity merged =
        new AuthenticationEntity(null, null, null, null, null, null, null);
    mapper.merge(request, merged);
    assertEquals("username", merged.getUserName());
  }
}
