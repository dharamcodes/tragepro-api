package com.tragepro.api.identity.account;

import static org.junit.jupiter.api.Assertions.*;

import com.tragepro.api.identity.account.model.entity.AccountDetailEntity;
import com.tragepro.api.identity.account.model.request.AccountDetailRequest;
import com.tragepro.api.identity.account.model.response.AccountDetailResponse;
import com.tragepro.api.identity.account.service.mapper.AccountDetailMapper;
import com.tragepro.api.identity.account.service.mapper.AccountDetailMapperImpl;
import org.junit.jupiter.api.Test;

class IdentityAccountTests {

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
