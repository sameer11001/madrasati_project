package com.webapp.madrasati.auth.mapper;

import org.mapstruct.BeanMapping;
import org.mapstruct.InheritConfiguration;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.mapstruct.NullValuePropertyMappingStrategy;

import com.webapp.madrasati.auth.model.UserEntity;
import com.webapp.madrasati.auth.model.dto.UserEntityDto;
import com.webapp.madrasati.auth.model.dto.res.UserResDto;
import com.webapp.madrasati.auth.util.GenderConstant;

@Mapper(componentModel = "spring")
public interface UserMapper {

    @Mapping(target = "userRole", ignore = true)
    @Mapping(target = "userSchool", source = "userSchool")
    @Mapping(target = "userGender", expression = "java(getGenderFromChar(userEntityDto.getUserGender()))")
    UserEntity fromUserEntityDto(UserEntityDto userEntityDto);

    @Mapping(target = "userBirthDate", source = "userBirthDate")
    @Mapping(target = "createdAt", source = "createdAt")
    @Mapping(target = "updatedAt", source = "updatedAt")
    @Mapping(target = "userGender", expression = "java(userEntity.getUserGender().getCode())")
    UserEntityDto fromUserEntity(UserEntity userEntity);

    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "userRole", ignore = true)
    @Mapping(target = "userSchool", source = "userSchool")
    @InheritConfiguration(name = "fromUserEntityDto")
    UserEntity updateUserEntity(UserEntityDto userEntityDto, @MappingTarget UserEntity userEntity);

    @Mapping(target = "userEmail", source = "userEmail")
    @Mapping(target = "userFirstName", source = "userFirstName")
    @Mapping(target = "userLastName", source = "userLastName")
    @Mapping(target = "userImage", source = "userImage")
    @Mapping(target = "userBirthDate", source = "userBirthDate")
    @Mapping(target = "userGender", expression = "java(userEntity.getUserGender() != null ? userEntity.getUserGender().getCode() : null)")
    UserResDto fromEntityToUserResDto(UserEntity userEntity);

    default GenderConstant getGenderFromChar(char genderChar) {
        return switch (genderChar) {
            case 'M' -> GenderConstant.MALE;
            case 'F' -> GenderConstant.FEMALE;
            default -> throw new IllegalArgumentException("Invalid gender character: " + genderChar);
        };
    }
}
