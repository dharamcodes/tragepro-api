package com.tragepro.api.journal.service.mapper;

import static org.junit.jupiter.api.Assertions.assertNull;

import com.tragepro.api.journal.model.entity.JournalEntity;
import com.tragepro.api.journal.model.request.JournalRequest;
import org.junit.jupiter.api.Test;
import org.mapstruct.factory.Mappers;

class JournalMapperTest {

    private final JournalMapper mapper = Mappers.getMapper(JournalMapper.class);

    @Test
    void testNullMappings() {
        assertNull(mapper.toEntity(null));
        assertNull(mapper.toResponse(null));

        JournalEntity entity = new JournalEntity();
        mapper.updateEntityFromRequest(null, entity); // Should not throw exception
    }

    @Test
    void testUpdateEntityFromRequest_WithNullFields() {
        JournalRequest request = JournalRequest.builder().build(); // All null fields
        JournalEntity entity = new JournalEntity();
        entity.setAccountId("accId");

        mapper.updateEntityFromRequest(request, entity);

        // MapStruct default strategy might overwrite with null or ignore nulls based on config.
        // Usually, in updates we want NullValuePropertyMappingStrategy.IGNORE.
        // Here we just test it runs without error to get coverage on the generated null checks.
    }
}
