package com.webapp.madrasati.core.model;

import org.mapstruct.BeanMapping;
import org.mapstruct.MappingTarget;
import org.mapstruct.NullValuePropertyMappingStrategy;

import java.util.Collections;
import java.util.List;

public interface BaseMapper <E extends BaseEntity, D extends BaseDto>{
    D toDto(E entity);

    E toEntity(D dto);

    List<D> toDtoList(List<E> entityList);

    List<E> toEntityList(List<D> dtoList);

    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    void updateEntityFromDto(D dto, @MappingTarget E entity);

    default <T> List<T> mapList(List<?> source, java.util.function.Function<Object, T> mapper) {
        if (source == null) {
            return Collections.emptyList();
        }
        return source.stream()
                .map(mapper)
                .toList();
    }
}
