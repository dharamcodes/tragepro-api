package com.tragepro.api.identity.service.mapper;

import static org.junit.jupiter.api.Assertions.*;

import com.tragepro.api.identity.model.entity.AccountDetailEntity;
import com.tragepro.api.identity.model.request.AccountDetailRequest;
import com.tragepro.api.identity.model.response.AccountDetailResponse;
import org.junit.jupiter.api.Test;

class AccountDetailMapperTest {

  @Test
  void testAccountDetailMapper() {
    AccountDetailMapper mapper = new AccountDetailMapperImpl();
    assertNull(mapper.requestToEntity(null));
    assertNull(mapper.entityToResponse(null));

    AccountDetailEntity target = AccountDetailEntity.builder().build();
    mapper.merge(null, target);

    AccountDetailRequest request =
        new AccountDetailRequest("name", "email@example.com", "identifier", 9876543210L, true);

    AccountDetailEntity entity = mapper.requestToEntity(request);
    assertNotNull(entity);
    assertEquals("name", entity.getName());

    AccountDetailResponse response = mapper.entityToResponse(entity);
    assertNotNull(response);
    assertEquals("name", response.name());

    AccountDetailEntity merged = AccountDetailEntity.builder().build();
    mapper.merge(request, merged);
    assertEquals("name", merged.getName());
  }
}
