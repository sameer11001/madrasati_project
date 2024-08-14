package com.webapp.madrasati.auth.security;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;
import org.springframework.security.web.util.matcher.RequestMatcher;
import org.springframework.util.AntPathMatcher;
import org.springframework.util.PathMatcher;

import com.webapp.madrasati.auth.service.LoginService;

/**
 * for enable @RolesAllowed and @Secured
 * beside @PreAuthorize and @PostAuthorize and @PostFilter and @PreFilter
 * 
 **/
// @EnableMethodSecurity(prePostEnabled = true, securedEnabled = true,
// jsr250Enabled = true)

@Configuration
@EnableWebSecurity
@EnableMethodSecurity(prePostEnabled = true)
public class WebSecurityConfig {
        @Autowired
        JwtAuthenticationEntryPoint authEntryPoint;

        @Autowired
        LoginService loginService;

        // this is the public req can any one access
        // put it into jwt Auth filter
        Set<String> publicRequest = new HashSet<>(
                        Arrays.asList("/swagger-ui/**", "/")); // we can add

        public SecurityFilterChain securityFilterChain(HttpSecurity http)
                        throws Exception {
                return http.csrf(AbstractHttpConfigurer::disable)
                                .exceptionHandling(ex -> ex.authenticationEntryPoint(authEntryPoint)).sessionManagement(
                                                session -> session
                                                                .sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                                .authorizeHttpRequests(request -> request
                                                .requestMatchers(publicRequest.stream().map(AntPathRequestMatcher::new)
                                                                .toArray(RequestMatcher[]::new))
                                                .permitAll()
                                                .anyRequest().authenticated())
                                .cors(Customizer.withDefaults())
                                .authenticationProvider(
                                                authenticationProvider())

                                // TODO add before filter jwt filter
                                .build();

        }

        @Bean
        public PasswordEncoder passwordEncoder() {
                return new BCryptPasswordEncoder(); // Or use a stronger encoder
        }

        @Bean
        public PathMatcher pathMatcher() {
                return new AntPathMatcher();
        }

        // put this to auth manger to use
        @Bean
        public AuthenticationManager authenticationManager(AuthenticationConfiguration config) throws Exception {
                return config.getAuthenticationManager();
        }

        @Bean
        AuthenticationProvider authenticationProvider() {
                DaoAuthenticationProvider authProvider = new DaoAuthenticationProvider();

                authProvider.setUserDetailsService(loginService);
                authProvider.setPasswordEncoder(passwordEncoder());

                return authProvider;
        }
}
