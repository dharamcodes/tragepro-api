package com.tragepro.api.common.mapper;

import org.mapstruct.MappingTarget;

public interface BaseMapper<E, R, O> {

    E requestToEntity(R r);

    O entityToResponse(E e);

    void merge(R source, @MappingTarget E target);

    MapperType getType();
}
