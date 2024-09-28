package com.webapp.madrasati.auth.service.imp;

import java.util.Optional;

import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.webapp.madrasati.auth.model.RefresherToken;
import com.webapp.madrasati.auth.model.UserEntity;
import com.webapp.madrasati.auth.model.dto.req.LoginRequestDto;
import com.webapp.madrasati.auth.model.dto.res.JwtResponseDto;
import com.webapp.madrasati.auth.model.dto.res.LoginUserDto;
import com.webapp.madrasati.auth.security.JwtTokenUtils;
import com.webapp.madrasati.auth.service.AuthenticateService;
import com.webapp.madrasati.auth.service.RefresherTokenService;
import com.webapp.madrasati.auth.service.UserServices;
import com.webapp.madrasati.core.error.BadRequestException;
import com.webapp.madrasati.core.error.InternalServerErrorException;

@Service
public class AuthenticateServiceImp implements AuthenticateService {

    private final UserServices userService;

    private final AuthenticationManager authenticationManager;

    private final RefresherTokenService refresherTokenService;

    private final JwtTokenUtils jwtTokenUtils;

    AuthenticateServiceImp(RefresherTokenService refresherToken, UserServices userService,
            AuthenticationManager authenticationManager, JwtTokenUtils jwtTokenUtils) {
        this.refresherTokenService = refresherToken;
        this.userService = userService;
        this.authenticationManager = authenticationManager;
        this.jwtTokenUtils = jwtTokenUtils;
    }

    @Transactional
    public JwtResponseDto login(LoginRequestDto requestBody, String deviceId) throws AuthenticationException {

        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(requestBody.getUserEmail(), requestBody.getPassword()));
        Optional<UserEntity> user = userService.findByUserEmail(requestBody.getUserEmail());
        if (user.isPresent() && authentication.isAuthenticated()) {

            SecurityContextHolder.getContext().setAuthentication(authentication);

            if (refresherTokenService.existsByDeviceId(deviceId)) {
                throw new BadRequestException("Already Login!");
            }

            // refresher token generation
            RefresherToken refresherToken = refresherTokenService.createRefreshToken(user.get(),
                    deviceId);

            // jwt access token generation
            String accessToken = jwtTokenUtils.generateToken(user.get().getUserEmail(), user.get().getId());
            UserEntity userEntity = user.get();
            LoginUserDto loginUserDto = LoginUserDto.builder()
                    .userEmail(userEntity.getUserEmail())
                    .firstName(userEntity.getUserFirstName())
                    .lastName(userEntity.getUserLastName())
                    .birthDate(userEntity.getUserBirthDate())
                    .gender(userEntity.getUserGender()).imagePath(userEntity.getUserImage()).build();
            new JwtResponseDto();
            return JwtResponseDto.builder().accessToken(accessToken)
                    .token(refresherToken.getToken()).user(loginUserDto).build();
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

}
