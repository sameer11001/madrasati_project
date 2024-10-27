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
import com.webapp.madrasati.school.model.dto.res.SchoolPageDto;
import com.webapp.madrasati.school.service.SchoolService;
import com.webapp.madrasati.school_group.model.Group;
import com.webapp.madrasati.school_group.service.GroupService;
import com.webapp.madrasati.util.DataTypeConverter;

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
        UserResDto loginUserDto = mapper.fromEntityToUserResDto(userEntity);

        return buildLoginResponse(userEntity, accessToken, refresherToken, loginUserDto);
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

    private LoginResponseDto buildLoginResponse(UserEntity userEntity, String accessToken, RefresherToken refresherToken, UserResDto loginUserDto) {
        LoginResponseDto.LoginResponseDtoBuilder responseBuilder = LoginResponseDto.builder()
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
            SchoolPageDto school = schoolService.fetchSchoolById(userEntity.getUserSchool().getId().toString());
            data.put("school", school);
        } else if (RoleAppConstant.STUDENT.getString().equals(roleName)) {
            Group group = groupService.findBySchoolId(userEntity.getUserSchool().getId())
                    .orElseThrow(() -> new ResourceNotFoundException("Group not found"));
            data.put("group", DataTypeConverter.Instance.objectIdToString(group.getId()));
            data.put("school", DataTypeConverter.Instance.uuidToString(userEntity.getUserSchool().getId()));
        }

        return data;
    }

    public void logout(String token) {
        try {
            refresherTokenService.deleteByToken(token);
        } catch (Exception e) {
            throw new InternalServerErrorException("Something went wrong: " + e.getMessage());
        } finally {
            SecurityContextHolder.clearContext();
        }
    }

    @Override
    public LoginGuestResponseDto guestLogin(String deviceId) {
        validateDeviceLogin(deviceId);
        UserEntity guestUser = createGuestUser();
        RefresherToken refresherToken = refresherTokenService.createRefreshToken(userService.saveGuest(guestUser), deviceId);
        String accessToken = jwtTokenUtils.generateToken("guest", UUID.randomUUID());

        return LoginGuestResponseDto.builder()
                .Gid(guestUser.getId())
                .username(guestUser.getUserEmail())
                .accessToken(accessToken)
                .token(refresherToken.getToken())
                .expiryDate(refresherToken.getExpiryDate())
                .build();
    }

    private UserEntity createGuestUser() {
        Role role = roleServices.findByRoleName(RoleAppConstant.GUEST.getString())
                .orElseThrow(() -> new ResourceNotFoundException("Role not found"));

        return UserEntity.builder()
                .userEmail("guest")
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
        UUID userId = UUID.fromString(userIdString);
        try {
            refresherTokenService.deleteByToken(token);
            userService.deleteUser(userId);
        } catch (ResourceNotFoundException e) {
            throw e;
        } catch (Exception e) {
            throw new InternalServerErrorException("Something went wrong: " + e.getMessage());
        } finally {
            SecurityContextHolder.clearContext();
        }
    }
}
