package com.webapp.madrasati.core.model;

import org.mapstruct.BeanMapping;
import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;
import org.mapstruct.NullValuePropertyMappingStrategy;

import java.util.List;
import java.util.stream.Collectors;

@Mapper(componentModel = "spring")
public interface BaseMapper <E extends BaseEntity , D>{
    D toDto(E entity);

    E toEntity(D dto);

    List<D> toDtoList(List<E> entityList);

    List<E> toEntityList(List<D> dtoList);

    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    void updateEntityFromDto(D dto, @MappingTarget E entity);

    // Helper method to handle null lists
    default <T> List<T> mapList(List<?> source, java.util.function.Function<Object, T> mapper) {
        if (source == null) {
            return null;
        }
        return source.stream()
                .map(mapper)
                .collect(Collectors.toList());
    }
}
