package com.tragepro.api.identity.service.mapper;

import static org.junit.jupiter.api.Assertions.*;

import com.tragepro.api.domain.identity.entity.AccountDetailEntity;
import com.tragepro.api.domain.identity.request.AccountDetailRequest;
import com.tragepro.api.domain.identity.response.AccountDetailResponse;
import org.junit.jupiter.api.Test;

class AccountDetailMapperTest {

  @Test
  void testAccountDetailMapper() {
    AccountDetailMapper mapper = org.mapstruct.factory.Mappers.getMapper(AccountDetailMapper.class);
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
