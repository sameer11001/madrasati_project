package com.webapp.madrasati.auth.mapper;

import org.mapstruct.BeanMapping;
import org.mapstruct.InheritConfiguration;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.mapstruct.NullValuePropertyMappingStrategy;

import com.webapp.madrasati.auth.model.UserEntity;
import com.webapp.madrasati.auth.model.dto.UserEntityDto;
import com.webapp.madrasati.auth.util.GenderConstant;

@Mapper(componentModel = "spring")
public interface UserMapper {

    @Mapping(target = "userRole", ignore = true)
    @Mapping(target = "userSchool", source = "userSchool")
    @Mapping(target = "userGender", expression = "java(getGenderFromChar(userEntityDto.getUserGender()))")
    UserEntity toUserEntity(UserEntityDto userEntityDto);

    @Mapping(target = "userBirthDate", source = "userBirthDate")
    @Mapping(target = "createdDate", source = "createdDate")
    @Mapping(target = "updatedDate", source = "updatedDate")
    @Mapping(target = "userGender", expression = "java(userEntity.getUserGender().getCode())")
    UserEntityDto toUserEntityDto(UserEntity userEntity);

    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "createdDate", ignore = true)
    @Mapping(target = "userRole", ignore = true)
    @Mapping(target = "userSchool", source = "userSchool")
    @InheritConfiguration(name = "toUserEntity")
    UserEntity updateUserEntity(UserEntityDto userEntityDto, @MappingTarget UserEntity userEntity);

    default GenderConstant getGenderFromChar(char genderChar) {
        switch (genderChar) {
            case 'M':
                return GenderConstant.MALE;
            case 'F':
                return GenderConstant.FEMALE;
            default:
                throw new IllegalArgumentException("Invalid gender character: " + genderChar);
        }
    }
}
