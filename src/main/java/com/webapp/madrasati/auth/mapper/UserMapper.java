package com.webapp.madrasati.auth.mapper;

import org.mapstruct.BeanMapping;
import org.mapstruct.InheritConfiguration;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.mapstruct.NullValuePropertyMappingStrategy;

import com.webapp.madrasati.auth.model.UserEntity;
import com.webapp.madrasati.auth.model.dto.UserEntityDto;

@Mapper(componentModel = "spring")
public interface UserMapper {

    @Mapping(target = "userRole", ignore = true)
    UserEntity toUserEntity(UserEntityDto userEntityDto);

    @Mapping(target = "userBirthDate", source = "userBirthDate", dateFormat = "yyyy-MM-dd")
    @Mapping(target = "createdDate", source = "createdDate", dateFormat = "yyyy-MM-dd")
    @Mapping(target = "updatedDate", source = "updatedDate", dateFormat = "yyyy-MM-dd")
    UserEntityDto toUserEntityDto(UserEntity userEntity);

    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "createdDate", ignore = true)
    @Mapping(target = "userRole", ignore = true)
    @InheritConfiguration(name = "toUserEntity")
    UserEntity updateUserEntity(UserEntityDto userEntityDto, @MappingTarget UserEntity userEntity);
}
