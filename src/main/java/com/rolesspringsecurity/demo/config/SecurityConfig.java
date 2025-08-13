package com.rolesspringsecurity.demo.config;

import com.rolesspringsecurity.demo.config.filter.JwtAuthFilter;
import com.rolesspringsecurity.demo.config.filter.JwtAutorizationFilter;
import com.rolesspringsecurity.demo.config.security.JwtUtils;
import com.rolesspringsecurity.demo.service.UserDetailsServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.NoOpPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@EnableMethodSecurity(securedEnabled = true)
public class SecurityConfig {

    @Autowired
    JwtUtils jwt_utils;

    @Autowired
    UserDetailsServiceImpl user_det_impl;

    @Autowired
    JwtAutorizationFilter jwt_autoriz_flter;


    @Bean
    public SecurityFilterChain filterChainConfig(HttpSecurity http, AuthenticationManager auth_manager) throws Exception {

        JwtAuthFilter jwt_auth_filter = new JwtAuthFilter(jwt_utils);
        jwt_auth_filter.setAuthenticationManager(auth_manager);
        jwt_auth_filter.setFilterProcessesUrl("/login");

        return http
                .csrf(config -> config.disable())
                .authorizeHttpRequests(auth -> {
                            auth.requestMatchers("/sinseguro").permitAll();
//                            auth.requestMatchers(HttpMethod.POST, "/createUser").hasRole("ADMIN");
//                            auth.requestMatchers(HttpMethod.POST, "/createUser").hasRole("USER");
//                            auth.requestMatchers(HttpMethod.POST, "/createUser").hasRole("INVITED");
                            auth.anyRequest().authenticated();
                        }
                )
                .sessionManagement(session -> {
                    session.sessionCreationPolicy(SessionCreationPolicy.STATELESS);
                })
                .httpBasic(Customizer.withDefaults())
                .addFilter(jwt_auth_filter)
                .addFilterBefore(jwt_autoriz_flter, UsernamePasswordAuthenticationFilter.class)
                .build();
    }


    //    esta Bean es especificamente para encriptacion de contraseñas
    @Bean
    PasswordEncoder contraseñaEncriptacion() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    AuthenticationManager autenticacionManejador(HttpSecurity http_secure, PasswordEncoder contraseñaEncriptacion) throws Exception {
        return http_secure.getSharedObject(AuthenticationManagerBuilder.class)
                .userDetailsService(user_det_impl)
                .passwordEncoder(contraseñaEncriptacion)
                .and().build();

    }


}