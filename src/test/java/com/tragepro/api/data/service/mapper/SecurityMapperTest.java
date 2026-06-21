package com.tragepro.api.data.service.mapper;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;

import com.tragepro.api.data.model.entity.SecurityEntity;
import com.tragepro.api.data.model.request.SecurityRequest;
import com.tragepro.api.data.model.response.SecurityResponse;
import org.junit.jupiter.api.Test;
import org.mapstruct.factory.Mappers;

class SecurityMapperTest {

  private final SecurityMapper mapper = Mappers.getMapper(SecurityMapper.class);

  @Test
  void testRequestToEntity() {
    SecurityRequest request =
        SecurityRequest.builder()
            .exchange("NSE")
            .segment("EQ")
            .securityId(12345)
            .isin("INE123")
            .instrument("EQUITY")
            .symbol("AAPL")
            .symbolName("Apple Inc")
            .name("Apple")
            .build();

    SecurityEntity entity = mapper.requestToEntity(request);

    assertNotNull(entity);
    assertEquals("NSE", entity.getExchange());
    assertEquals("EQ", entity.getSegment());
    assertEquals(12345, entity.getSecurityId());
    assertEquals("INE123", entity.getIsin());
    assertEquals("EQUITY", entity.getInstrument());
    assertEquals("AAPL", entity.getSymbol());
    assertEquals("Apple Inc", entity.getSymbolName());
    assertEquals("Apple", entity.getName());
  }

  @Test
  void testEntityToResponse() {
    SecurityEntity entity = new SecurityEntity();
    entity.setExchange("NSE");
    entity.setSegment("EQ");
    entity.setSecurityId(12345);
    entity.setIsin("INE123");
    entity.setInstrument("EQUITY");
    entity.setSymbol("AAPL");
    entity.setSymbolName("Apple Inc");
    entity.setName("Apple");

    SecurityResponse response = mapper.entityToResponse(entity);

    assertNotNull(response);
    assertEquals("NSE", response.exchange());
    assertEquals("EQ", response.segment());
    assertEquals(12345, response.securityId());
    assertEquals("INE123", response.isin());
    assertEquals("EQUITY", response.instrument());
    assertEquals("AAPL", response.symbol());
    assertEquals("Apple Inc", response.symbolName());
    assertEquals("Apple", response.name());
  }

  @Test
  void testMerge() {
    SecurityRequest request = SecurityRequest.builder().symbol("NEW_AAPL").build();

    SecurityEntity entity = new SecurityEntity();
    entity.setExchange("NSE");
    entity.setSymbol("OLD_AAPL");

    mapper.merge(request, entity);

    assertEquals("NEW_AAPL", entity.getSymbol());
    assertEquals(
        "NSE",
        entity.getExchange()); // Unchanged if null in request (depends on MapStruct config, usually
    // overwrites or ignores null)
  }

  @Test
  void testNullHandling() {
    assertNull(mapper.requestToEntity(null));
    assertNull(mapper.entityToResponse(null));

    SecurityEntity entity = new SecurityEntity();
    mapper.merge(null, entity); // test null source
  }

  @Test
  void testEmptyHandling() {
    SecurityEntity entity = mapper.requestToEntity(SecurityRequest.builder().build());
    assertNotNull(entity);
  }
}
