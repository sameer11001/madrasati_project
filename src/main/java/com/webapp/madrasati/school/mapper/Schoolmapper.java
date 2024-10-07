package com.webapp.madrasati.school.mapper;

import org.mapstruct.BeanMapping;
import org.mapstruct.InheritConfiguration;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.mapstruct.NullValuePropertyMappingStrategy;

import com.webapp.madrasati.school.model.School;
import com.webapp.madrasati.school.model.dto.SchoolDto;

@Mapper(componentModel = "spring")
public interface Schoolmapper {

    @Mapping(target = "schoolRatings", ignore = true)
    @Mapping(target = "schoolImages", source = "schoolImages")
    @Mapping(target = "schoolFeedBacks", source = "schoolFeedBacks")
    @Mapping(target = "teachers", ignore = true)
    School toSchoolEntity(SchoolDto school);

    @Mapping(target = "createdDate", source = "createdDate")
    @Mapping(target = "updatedDate", source = "updatedDate")
    @Mapping(target = "schoolFound", source = "schoolFound")
    SchoolDto toSchoolDto(School school);

    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "createdDate", ignore = true)
    @Mapping(target = "schoolRatings", ignore = true)
    @Mapping(target = "schoolImages", source = "schoolImages")
    @Mapping(target = "schoolFeedBacks", source = "schoolFeedBacks")
    @InheritConfiguration(name = "toSchoolEntity")
    School updateUserEntity(SchoolDto schoolDto, @MappingTarget School schoolEntity);
}
