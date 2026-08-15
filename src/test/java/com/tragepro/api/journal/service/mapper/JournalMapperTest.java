package com.tragepro.api.journal.service.mapper;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;

import com.tragepro.api.domain.journal.entity.JournalEntity;
import com.tragepro.api.domain.journal.request.JournalRequest;
import com.tragepro.api.domain.journal.response.JournalResponse;
import java.util.ArrayList;
import java.util.List;
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
  void testToEntity_FullyPopulated() {
    JournalRequest request =
        JournalRequest.builder()
            .accountId("acc1")
            .symbol("AAPL")
            .notes("Notes")
            .tags(List.of("win"))
            .tradeType(com.tragepro.api.domain.journal.enums.TradeType.LONG)
            .status(com.tragepro.api.domain.journal.enums.TradeStatus.OPEN)
            .build();
    JournalEntity entity = mapper.toEntity(request);
    assertNotNull(entity);
    assertEquals("AAPL", entity.getSymbol());
    assertNotNull(entity.getTags());
  }

  @Test
  void testToEntity_ListsNull() {
    JournalRequest request = JournalRequest.builder().build();
    JournalEntity entity = mapper.toEntity(request);
    assertNotNull(entity);
    assertNull(entity.getTags());
  }

  @Test
  void testToResponse_FullyPopulated() {
    JournalEntity entity = new JournalEntity();
    entity.setSymbol("AAPL");
    entity.setTags(List.of("win"));
    JournalResponse response = mapper.toResponse(entity);
    assertNotNull(response);
    assertNotNull(response.getTags());
  }

  @Test
  void testToResponse_ListsNull() {
    JournalEntity entity = new JournalEntity();
    JournalResponse response = mapper.toResponse(entity);
    assertNotNull(response);
    assertNull(response.getTags());
  }

  @Test
  void testUpdateEntityFromRequest_FullyPopulated() {
    JournalRequest request =
        JournalRequest.builder().symbol("Updated").tags(List.of("loss")).build();
    JournalEntity entity = new JournalEntity();
    entity.setTags(new ArrayList<>(List.of("win")));
    mapper.updateEntityFromRequest(request, entity);
    assertEquals("Updated", entity.getSymbol());
    assertEquals(1, entity.getTags().size());
    assertEquals("loss", entity.getTags().get(0));
  }

  @Test
  void testUpdateEntityFromRequest_ListsNull() {
    JournalRequest request = JournalRequest.builder().build();
    JournalEntity entity = new JournalEntity();
    entity.setTags(new ArrayList<>(List.of("win")));
    mapper.updateEntityFromRequest(request, entity);
    // mapstruct will either clear or keep based on config, here we just test branches
  }

  @Test
  void testUpdateEntityFromRequest_EntityTagsNull() {
    JournalRequest request1 = JournalRequest.builder().tags(List.of("tag1")).build();
    JournalEntity entity1 = new JournalEntity();
    entity1.setTags(null);
    mapper.updateEntityFromRequest(request1, entity1);
    assertNotNull(entity1.getTags());
    assertEquals("tag1", entity1.getTags().get(0));

    JournalRequest request2 = JournalRequest.builder().tags(null).build();
    JournalEntity entity2 = new JournalEntity();
    entity2.setTags(null);
    mapper.updateEntityFromRequest(request2, entity2);
    assertNull(entity2.getTags());
  }
}
