package com.webapp.madrasati.school.service.imp;

import com.webapp.madrasati.auth.mapper.UserMapper;
import com.webapp.madrasati.auth.model.UserEntity;
import com.webapp.madrasati.school.model.dto.res.CreateNewSchoolDto;
import com.webapp.madrasati.school_group.model.Group;
import com.webapp.madrasati.school_group.service.GroupService;
import com.webapp.madrasati.util.AppUtilConverter;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.webapp.madrasati.auth.model.dto.req.CreateUserBodyDto;
import com.webapp.madrasati.auth.service.UserServices;
import com.webapp.madrasati.auth.util.RoleAppConstant;
import com.webapp.madrasati.core.error.AlreadyExistException;
import com.webapp.madrasati.core.error.InternalServerErrorException;
import com.webapp.madrasati.school.mapper.Schoolmapper;
import com.webapp.madrasati.school.model.School;
import com.webapp.madrasati.school.model.dto.req.SchoolCreateBody;
import com.webapp.madrasati.school.repository.SchoolRepository;

import lombok.AllArgsConstructor;

@Service
@AllArgsConstructor
public class SchoolCreateService {

    private final SchoolRepository schoolRepository;
    private final Schoolmapper schoolmapper;
    private final UserServices userServices;
    private final GroupService groupService;
    private final UserMapper userMapper;

    @Transactional
    public CreateNewSchoolDto createSchool(SchoolCreateBody schoolCreateBody) {
        try {
            if (Boolean.TRUE.equals(schoolRepository.existsBySchoolName(schoolCreateBody.getSchoolName()))) {
                throw new AlreadyExistException(
                        "School with name " + schoolCreateBody.getSchoolName() + " already exists.");
            }
            AppUtilConverter dataConvert = AppUtilConverter.Instance;

            School school = schoolmapper.fromCreateSchoolBodyDto(schoolCreateBody);
            schoolRepository.save(school);
            Group group = groupService.createGroup(dataConvert.uuidToString(school.getId()),school.getSchoolCoverImage());
            CreateUserBodyDto bodyDto = CreateUserBodyDto.builder()
                    .userEmail(schoolCreateBody.getSchoolEmail())
                    .userPassword(schoolCreateBody.getSchoolMangerPassword())
                    .userFirstName(schoolCreateBody.getSchoolName())
                    .userLastName("Manager")
                    .userSchool(school)
                    .userGender('M')
                    .userBirthDate(schoolCreateBody.getSchoolFound())
                    .build();
            UserEntity user = userServices.createNewUser(bodyDto, RoleAppConstant.SMANAGER);

            return CreateNewSchoolDto.builder()
                    .school(schoolmapper.fromSchoolEntity(school))
                    .user(userMapper.fromUserEntity(user))
                    .groupId(dataConvert.objectIdToString(group.getId())).build();
        } catch (Exception e) {
            throw new InternalServerErrorException(e.getMessage());
        }
    }
}
