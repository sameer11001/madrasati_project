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
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;
import org.springframework.security.web.util.matcher.RequestMatcher;
import org.springframework.util.AntPathMatcher;
import org.springframework.util.PathMatcher;

import com.webapp.madrasati.auth.service.UserDetailsServiceImp;

/**
 * for enable @RolesAllowed and @Secured
 * beside @PreAuthorize and @PostAuthorize and @PostFilter and @PreFilter
 * 
 * @PreAuthorize("hasRole('ADMIN')")
 * @PostAuthorize("hasRole('ADMIN')") without 'ROLE_' prefix
 * spring security understand ROLE_
 * 
 * @PreAuthorize("hasAnyRole('ADMIN','USER')")
 * @PostAuthorize("hasAnyRole('ADMIN','USER')")
 * 
 * @PreAuthorize("hasAuthority('READ_DATA')")
 * @PostAuthorize("hasAuthority('CREATE_POST')")
 * 
 * @PreAuthorize("hasRole('ADMIN') and hasAuthority('DELETE_USER')")
 * 
 * @EnableMethodSecurity(prePostEnabled = true, securedEnabled = true,
 *                                      jsr250Enabled = true)
 **/

@Configuration
@EnableWebSecurity
@EnableMethodSecurity(prePostEnabled = true)
public class WebSecurityConfig {

        @Autowired
        JwtAuthenticationEntryPoint authEntryPoint;

        @Autowired
        UserDetailsServiceImp userDetailsServiceImp;

        @Autowired
        JwtAuthFilter jwtAuthFilter;

        // this is the public req can any one access
        // put it into jwt Auth filter
        Set<String> publicRequest = new HashSet<>(
                        Arrays.asList("/v3/api-docs/**", "/swagger-resources/**", "/swagger-resources",
                                        "/swagger-ui/**", "/swagger-ui.html", "/", "/api/v1/auth/login")); // we can add

        @Bean
        public PathMatcher pathMatcher() {
                return new AntPathMatcher();
        }

        @Bean
        public SecurityFilterChain securityFilterChain(HttpSecurity http, JwtAuthFilter jwtAuthFilter)
                        throws Exception {
                return http.csrf(AbstractHttpConfigurer::disable)
                                .exceptionHandling(ex -> ex.authenticationEntryPoint(authEntryPoint)).sessionManagement(
                                                session -> session
                                                                .sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                                .authorizeHttpRequests(request -> request
                                                .requestMatchers(publicRequest.stream().map(AntPathRequestMatcher::new)
                                                                .toArray(RequestMatcher[]::new))
                                                .permitAll()
                                                .requestMatchers("/api/v1/auth/token").permitAll()
                                                .anyRequest().authenticated())
                                .cors(Customizer.withDefaults())
                                .authenticationProvider(
                                                authenticationProvider())
                                .addFilterBefore(jwtAuthFilter, UsernamePasswordAuthenticationFilter.class)
                                .build();

        }

        // this is encode method for all password
        @Bean
        public PasswordEncoder passwordEncoder() {
                return new BCryptPasswordEncoder();
        }

        // put this to auth manger to use
        @Bean
        public AuthenticationManager authenticationManager(AuthenticationConfiguration config) throws Exception {
                return config.getAuthenticationManager();
        }

        @Bean
        AuthenticationProvider authenticationProvider() {
                DaoAuthenticationProvider authProvider = new DaoAuthenticationProvider();

                authProvider.setUserDetailsService(userDetailsServiceImp);
                authProvider.setPasswordEncoder(passwordEncoder());

                return authProvider;
        }
}
