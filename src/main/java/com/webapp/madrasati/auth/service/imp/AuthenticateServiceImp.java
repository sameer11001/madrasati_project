package com.webapp.madrasati.auth.service.imp;

import java.time.LocalDate;
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

import com.webapp.madrasati.auth.model.RefresherToken;
import com.webapp.madrasati.auth.model.Role;
import com.webapp.madrasati.auth.model.UserEntity;
import com.webapp.madrasati.auth.model.dto.req.LoginRequestDto;
import com.webapp.madrasati.auth.model.dto.res.JwtResponseDto;
import com.webapp.madrasati.auth.model.dto.res.LoginGuestResponseDto;
import com.webapp.madrasati.auth.model.dto.res.LoginUserDto;
import com.webapp.madrasati.auth.security.JwtTokenUtils;
import com.webapp.madrasati.auth.security.UserIdSecurity;
import com.webapp.madrasati.auth.service.AuthenticateService;
import com.webapp.madrasati.auth.service.RefresherTokenService;
import com.webapp.madrasati.auth.service.RoleServices;
import com.webapp.madrasati.auth.service.UserServices;
import com.webapp.madrasati.auth.util.GenderConstant;
import com.webapp.madrasati.auth.util.RoleAppConstant;
import com.webapp.madrasati.core.error.InternalServerErrorException;
import com.webapp.madrasati.core.error.ResourceNotFoundException;

import lombok.AllArgsConstructor;

@Service
@AllArgsConstructor
public class AuthenticateServiceImp implements AuthenticateService {

    private final UserServices userService;

    private final AuthenticationManager authenticationManager;

    private final RefresherTokenService refresherTokenService;

    private final JwtTokenUtils jwtTokenUtils;

    private final RoleServices roleServices;

    private final UserIdSecurity userId;

    @Transactional
    public JwtResponseDto login(LoginRequestDto requestBody, String deviceId) throws AuthenticationException {

        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(requestBody.getUserEmail(), requestBody.getPassword()));
        Optional<UserEntity> user = userService.findByUserEmail(requestBody.getUserEmail());
        if (user.isPresent() && authentication.isAuthenticated()) {

            SecurityContextHolder.getContext().setAuthentication(authentication);

            if (refresherTokenService.existsByDeviceId(deviceId)) {
                throw new AlreadyExistException("Already Login!");
            }

            RefresherToken refresherToken = refresherTokenService.createRefreshToken(user.get(),
                    deviceId);

            // jwt access token generation
            String accessToken = jwtTokenUtils.generateToken(user.get().getUserEmail(), user.get().getId());
            UserEntity userEntity = user.get();
            LoginUserDto loginUserDto = LoginUserDto.builder()
                    .userEmail(userEntity.getUserEmail())
                    .firstName(userEntity.getUserFirstName())
                    .lastName(userEntity.getUserLastName())
                    .birthDate(LocalDate.from(userEntity.getUserBirthDate()))
                    .gender(userEntity.getUserGender().getCode()).imagePath(userEntity.getUserImage()).build();
            return JwtResponseDto.builder().accessToken(accessToken)
                    .token(refresherToken.getToken()).user(loginUserDto).expiryDate(refresherToken.getExpiryDate())
                    .build();
        }
        throw new BadCredentialsException("Invalid username or password");
    }

    public void logout(String token) {
        try {
            refresherTokenService.deleteByToken(token);
        } catch (Exception e) {
            throw new InternalServerErrorException("Something went wrong: " + e.getMessage());
        }
    }

    @Override
    public LoginGuestResponseDto guestLogin(String deviceId) {
        if (refresherTokenService.existsByDeviceId(deviceId)) {
            throw new AlreadyExistException("Already Login!");
        }
        Role role = roleServices.findByRoleName(RoleAppConstant.SMANAGER.getString()).orElseThrow(() -> new ResourceNotFoundException("Role not found"));
        UserEntity user = UserEntity.builder().userEmail("guest").userPassword("guest").userFirstName("guest").userBirthDate(LocalDate.now()).userRole(role).userGender(GenderConstant.MALE).build();
        RefresherToken refresherToken = refresherTokenService.createRefreshToken(userService.saveGuest(user),
                    deviceId);
                    String accessToken = jwtTokenUtils.generateToken("guest", UUID.randomUUID());
        return LoginGuestResponseDto.builder().Gid(user.getId()).username(user.getUserEmail()).accessToken(accessToken).token(refresherToken.getToken()).expiryDate(refresherToken.getExpiryDate()).build();
        
    }

    @Override
    @Transactional
    public void guestLogout(String token) {
        try {
            refresherTokenService.deleteByToken(token);
            
            if (userId.getUId() != null) {
                userService.deleteUser(userId.getUId());
            } else {
                throw new ResourceNotFoundException("User ID not found for logout.");
            }
        } catch (ResourceNotFoundException e) {
            throw e; 
        } catch (Exception e) {
            throw new InternalServerErrorException("Something went wrong: " + e.getMessage());
        }
    }
}   
