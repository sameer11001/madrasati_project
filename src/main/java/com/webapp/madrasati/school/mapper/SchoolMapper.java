package com.webapp.madrasati.school.mapper;

import com.webapp.madrasati.school.model.dto.req.SchoolEditBodyDto;
import com.webapp.madrasati.school.model.dto.res.SchoolEditResponseDto;
import com.webapp.madrasati.school.model.dto.res.SchoolProfilePageDto;
import org.mapstruct.BeanMapping;
import org.mapstruct.InheritConfiguration;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.mapstruct.NullValuePropertyMappingStrategy;

import com.webapp.madrasati.school.model.School;
import com.webapp.madrasati.school.model.dto.SchoolDto;
import com.webapp.madrasati.school.model.dto.req.SchoolCreateBody;

@Mapper(componentModel = "spring")
public interface SchoolMapper {

    @Mapping(target = "schoolRatings", ignore = true)
    @Mapping(target = "schoolImages", source = "schoolImages")
    @Mapping(target = "schoolFeedBacks", source = "schoolFeedBacks")
    @Mapping(target = "teachers", ignore = true)
    School fromSchoolDto(SchoolDto school);

    @Mapping(target = "createdAt", source = "createdAt")
    @Mapping(target = "updatedAt", source = "updatedAt")
    @Mapping(target = "schoolFound", source = "schoolFound")
    SchoolDto fromSchoolEntity(School school);

    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "schoolRatings", ignore = true)
    @Mapping(target = "schoolImages", source = "schoolImages")
    @Mapping(target = "schoolFeedBacks", source = "schoolFeedBacks")
    @InheritConfiguration(name = "fromSchoolDto")
    School updateSchoolEntity(SchoolDto schoolDto, @MappingTarget School schoolEntity);

    @Mapping(target = "schoolImages", ignore = true)  
    @Mapping(target = "schoolCoverImage", ignore = true)
    @Mapping(target = "schoolRatings", ignore = true)  
    @Mapping(target = "schoolFeedBacks", ignore = true)  
    @Mapping(target = "teachers", ignore = true)  
    @Mapping(target = "averageRating", ignore = true)  
    School fromCreateSchoolBodyDto(SchoolCreateBody schoolDto);


    @Mapping(target = "schoolImagesPath", ignore = true)
    @Mapping(target = "schoolFeedBacks", ignore = true)
    @Mapping(target = "schoolId", source = "id")
    SchoolProfilePageDto fromSchoolEntityToSchoolProfilePageDto(School school);



    SchoolEditResponseDto fromSchoolEntityToSchoolEditResponseDto(School school);

    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "schoolRatings", ignore = true)
    @Mapping(target = "schoolImages", ignore = true)
    @Mapping(target = "schoolFeedBacks", ignore = true)
    @Mapping(target = "teachers", ignore = true)
    @Mapping(target = "schoolCoverImage", ignore = true)
    @InheritConfiguration(name = "fromSchoolDto")
    School updateSchoolEntity(SchoolEditBodyDto schoolDto, @MappingTarget School schoolEntity);


}
