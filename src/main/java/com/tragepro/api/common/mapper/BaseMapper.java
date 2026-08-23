package com.tragepro.api.common.mapper;

import org.mapstruct.MappingTarget;

public interface BaseMapper<E, R, O> {

    E requestToEntity(R r);

    O entityToResponse(E e);

    void merge(R source, @MappingTarget E target);

    default Class<?> getMapperClass() {
        for (Class<?> iface : getClass().getInterfaces()) {
            if (BaseMapper.class.isAssignableFrom(iface) && iface != BaseMapper.class) {
                return iface;
            }
        }
        return getClass();
    }
}
