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
import com.webapp.madrasati.core.config.LoggerApp;
import com.webapp.madrasati.core.error.BadRequestException;
import com.webapp.madrasati.core.error.InternalServerErrorException;

@Service
@Transactional
public class AuthenticateServiceImp implements AuthenticateService {

    private UserServices userService;

    private AuthenticationManager authenticationManager;

    private RefresherTokenService refresherTokenService;

    private JwtTokenUtils jwtTokenUtils;

    AuthenticateServiceImp(RefresherTokenService refresherToken, UserServices userService,
            AuthenticationManager authenticationManager, JwtTokenUtils jwtTokenUtils) {
        this.refresherTokenService = refresherToken;
        this.userService = userService;
        this.authenticationManager = authenticationManager;
        this.jwtTokenUtils = jwtTokenUtils;
    }

    @Transactional
    public JwtResponseDto login(LoginRequestDto requestBody) throws AuthenticationException {

        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(requestBody.getUserEmail(), requestBody.getPassword()));
        Optional<UserEntity> user = userService.findByUserEmail(requestBody.getUserEmail());
        if (user.isPresent() && authentication.isAuthenticated()) {
            LoggerApp.info("doAuthenticate complete with " + requestBody.getUserEmail());

            // Security Best Practices
            SecurityContextHolder.getContext().setAuthentication(authentication); // Optional based on framework
            LoggerApp.info("setAuthentication complete with " + requestBody.getUserEmail());

            if (refresherTokenService.existsByDeviceId(requestBody.getDeviceId())) {
                throw new BadRequestException("Already Login!");
            }

            // refresher token generation
            RefresherToken refresherToken = refresherTokenService.createRefreshToken(user.get(),
                    requestBody.getDeviceId());

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
            LoggerApp.info("Logout complete ");
        } catch (Exception e) {
            throw new InternalServerErrorException("Something went wrong: " + e.getMessage());
        }
    }

}
