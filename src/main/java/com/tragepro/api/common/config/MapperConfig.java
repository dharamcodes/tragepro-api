package io.tragepro.api.common.config;

import io.tragepro.api.common.mapper.BaseMapper;
import io.tragepro.api.common.mapper.MapperType;
import java.util.EnumMap;
import java.util.Map;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class MapperConfig<E, R, O> {

    @Bean
    public Map<MapperType, BaseMapper<E, R, O>> mappers() {
        return new EnumMap<>(MapperType.class);
    }
}
