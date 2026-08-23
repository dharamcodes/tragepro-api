package com.tragepro.api.common.mapper;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class MapperFactory {

    private final Map<Class<?>, BaseMapper<?, ?, ?>> mappers;

    @Autowired
    public MapperFactory(List<BaseMapper<?, ?, ?>> mapperList) {
        this.mappers = new HashMap<>();
        for (BaseMapper<?, ?, ?> mapper : mapperList) {
            mappers.put(mapper.getMapperClass(), mapper);
        }
    }

    public MapperFactory(Map<Class<?>, BaseMapper<?, ?, ?>> mappers) {
        this.mappers = mappers;
    }

    @SuppressWarnings("unchecked")
    public <T extends BaseMapper<?, ?, ?>> T getMapper(Class<T> mapperClass) {
        return (T) mappers.get(mapperClass);
    }
}
