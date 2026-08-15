package com.tragepro.api.common.mapper;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.Mockito.when;

import java.util.List;
import java.util.Map;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class MapperFactoryTest {

  @Mock private BaseMapper<?, ?, ?> mockMapper;

  @Test
  void testListConstructor() {
    when(mockMapper.getMapperClass()).thenAnswer(inv -> DummyMapper.class);
    MapperFactory factory = new MapperFactory(List.of(mockMapper));
    assertNotNull(factory.getMapper(DummyMapper.class));
  }

  @Test
  void testMapConstructor() {
    MapperFactory factory = new MapperFactory(Map.of(DummyMapper.class, mockMapper));
    assertEquals(mockMapper, factory.getMapper(DummyMapper.class));
  }

  private interface DummyMapper extends BaseMapper<Object, Object, Object> {}
}
