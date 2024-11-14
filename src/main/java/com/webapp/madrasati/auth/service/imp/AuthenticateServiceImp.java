package com.webapp.madrasati.auth.service.imp;

import java.time.LocalDate;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;

import com.webapp.madrasati.core.error.AlreadyExistException;

import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.webapp.madrasati.auth.mapper.UserMapper;
import com.webapp.madrasati.auth.model.RefresherToken;
import com.webapp.madrasati.auth.model.Role;
import com.webapp.madrasati.auth.model.UserEntity;
import com.webapp.madrasati.auth.model.dto.req.LoginRequestDto;
import com.webapp.madrasati.auth.model.dto.res.LoginResponseDto;
import com.webapp.madrasati.auth.model.dto.res.LoginGuestResponseDto;
import com.webapp.madrasati.auth.model.dto.res.UserResDto;
import com.webapp.madrasati.auth.security.JwtTokenUtils;
import com.webapp.madrasati.auth.service.AuthenticateService;
import com.webapp.madrasati.auth.service.RefresherTokenService;
import com.webapp.madrasati.auth.service.RoleServices;
import com.webapp.madrasati.auth.service.UserServices;
import com.webapp.madrasati.auth.util.GenderConstant;
import com.webapp.madrasati.auth.util.RoleAppConstant;
import com.webapp.madrasati.core.error.InternalServerErrorException;
import com.webapp.madrasati.core.error.ResourceNotFoundException;
import com.webapp.madrasati.school.model.dto.res.SchoolProfilePageDto;
import com.webapp.madrasati.school.service.SchoolService;
import com.webapp.madrasati.school_group.model.Group;
import com.webapp.madrasati.school_group.service.GroupService;
import com.webapp.madrasati.util.AppUtilConverter;

import lombok.AllArgsConstructor;

@Service
@AllArgsConstructor
public class AuthenticateServiceImp implements AuthenticateService {

    private final UserServices userService;
    private final AuthenticationManager authenticationManager;
    private final RefresherTokenService refresherTokenService;
    private final JwtTokenUtils jwtTokenUtils;
    private final RoleServices roleServices;
    private final SchoolService schoolService;
    private final GroupService groupService;
    private final UserMapper mapper;

    private static final AppUtilConverter dataConverter = AppUtilConverter.Instance;

    @Transactional
    public LoginResponseDto login(LoginRequestDto requestBody, String deviceId) throws AuthenticationException {
        Authentication authentication = authenticateUser(requestBody);
        Optional<UserEntity> user = userService.findByUserEmail(requestBody.getUserEmail());

        if (user.isEmpty() || !authentication.isAuthenticated()) {
            throw new BadCredentialsException("Invalid username or password");
        }

        UserEntity userEntity = user.get();
        validateDeviceLogin(deviceId);
        RefresherToken refresherToken = createRefreshToken(userEntity, deviceId);
        String accessToken = jwtTokenUtils.generateToken(userEntity.getUserEmail(), userEntity.getId());

        return buildLoginResponse(userEntity, accessToken, refresherToken);
    }

    private Authentication authenticateUser(LoginRequestDto requestBody) {
        return authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(requestBody.getUserEmail(), requestBody.getPassword()));
    }

    private void validateDeviceLogin(String deviceId) {
        if (refresherTokenService.existsByDeviceId(deviceId)) {
            throw new AlreadyExistException("Already Login!");
        }
    }

    private RefresherToken createRefreshToken(UserEntity user, String deviceId) {
        return refresherTokenService.createRefreshToken(user, deviceId);
    }

    private LoginResponseDto buildLoginResponse(UserEntity userEntity, String accessToken, RefresherToken refresherToken) {
        UserResDto loginUserDto = mapper.fromEntityToUserResDto(userEntity);

        LoginResponseDto.LoginResponseDtoBuilder responseBuilder = LoginResponseDto.builder()
                .userId(dataConverter.uuidToString(userEntity.getId()))
                .accessToken(accessToken)
                .token(refresherToken.getToken())
                .user(loginUserDto)
                .expiryDate(refresherToken.getExpiryDate());

        Map<String, Object> data = getDataBasedOnRole(userEntity);

        responseBuilder.data(data);

        return responseBuilder.build();
    }

    private Map<String, Object> getDataBasedOnRole(UserEntity userEntity) {
        Map<String, Object> data = new HashMap<>();
        String roleName = userEntity.getUserRole().getRoleName();

        if (RoleAppConstant.ADMIN.getString().equals(roleName)) {
            // Soon will be data
        } else if (RoleAppConstant.SMANAGER.getString().equals(roleName)) {
            Group group = groupService.findBySchoolId(userEntity.getUserSchool().getId())
                    .orElseThrow(() -> new ResourceNotFoundException("Group not found"));

            SchoolProfilePageDto school = schoolService.fetchSchoolById(dataConverter.uuidToString(userEntity.getUserSchool().getId()));
            data.put("school", school);
            data.put("groupId", dataConverter.objectIdToString(group.getId()));
            data.put("isManger", true);
        } else if (RoleAppConstant.STUDENT.getString().equals(roleName)) {
            Group group = groupService.findBySchoolId(userEntity.getUserSchool().getId())
                    .orElseThrow(() -> new ResourceNotFoundException("Group not found"));

            data.put("groupId", dataConverter.objectIdToString(group.getId()));
            data.put("schoolId", dataConverter.uuidToString(userEntity.getUserSchool().getId()));
            data.put("schoolName", userEntity.getUserSchool().getSchoolName());
            data.put("isManager", false);
        }

        return data;
    }

    public void logout(String token) {
        try {
            refresherTokenService.deleteByToken(token);
        } catch (InternalServerErrorException e) {
            throw new InternalServerErrorException("Something went wrong: " + e.getMessage());
        } finally {
            SecurityContextHolder.clearContext();
        }
    }

    @Override
    @Transactional
    public LoginGuestResponseDto guestLogin(String deviceId) {
        validateDeviceLogin(deviceId);
        String userEmail = "guest" + dataConverter.randomUUIDToString();
        UserEntity guestUser = createGuestUser(userEmail);
        RefresherToken refresherToken = refresherTokenService.createRefreshToken(userService.saveUserEntity(guestUser), deviceId);
        String accessToken = jwtTokenUtils.generateToken(userEmail, dataConverter.randomUUID());

        return LoginGuestResponseDto.builder()
                .Gid(dataConverter.uuidToString(guestUser.getId()))
                .username(guestUser.getUserEmail())
                .accessToken(accessToken)
                .token(refresherToken.getToken())
                .expiryDate(refresherToken.getExpiryDate())
                .data(schoolService.getSchoolHomePage(0, 10))
                .build();
    }

    private UserEntity createGuestUser(String userEmail) {
        Role role = roleServices.findByRoleName(RoleAppConstant.GUEST.getString())
                .orElseThrow(() -> new ResourceNotFoundException("Role not found"));

        return UserEntity.builder()
                .userEmail(userEmail)
                .userPassword("guest")
                .userFirstName("guest")
                .userBirthDate(LocalDate.now())
                .userRole(role)
                .userGender(GenderConstant.MALE)
                .build();
    }

    @Override
    @Transactional
    public void guestLogout(String token, String userIdString) {
        UUID userId = dataConverter.stringToUUID(userIdString);
        try {
            refresherTokenService.deleteByToken(token);
            userService.deleteUser(userId);
        } catch (ResourceNotFoundException e) {
            throw e;
        } catch (InternalServerErrorException e) {
            throw new InternalServerErrorException("Something went wrong: " + e.getMessage());
        } finally {
            SecurityContextHolder.clearContext();
        }
    }
}
