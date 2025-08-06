package com.rolesspringsecurity.demo.config;

import com.rolesspringsecurity.demo.config.filter.JwtAuthFilter;
import com.rolesspringsecurity.demo.config.security.JwtUtils;
import com.rolesspringsecurity.demo.service.UserDetailsServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
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

@Configuration
public class SecurityConfig {

    @Autowired
    JwtUtils jwt_utils;

    @Autowired
    UserDetailsServiceImpl user_det_impl;

    @Bean
    public SecurityFilterChain filterChainConfig(HttpSecurity http, AuthenticationManager auth_manager) throws Exception {

        JwtAuthFilter jwt_auth_filter = new JwtAuthFilter(jwt_utils);
        jwt_auth_filter.setAuthenticationManager(auth_manager);

        return http
                .csrf(config -> config.disable())
                .authorizeHttpRequests(auth -> {
                            auth.requestMatchers("/sinseguro").permitAll();
                            auth.anyRequest().authenticated();
                        }
                )
                .sessionManagement(session -> {
                    session.sessionCreationPolicy(SessionCreationPolicy.STATELESS);
                })
                .httpBasic(Customizer.withDefaults())
                .addFilter(jwt_auth_filter)
                .build();
    }




//    @Bean
//    UserDetailsService detallesUsuario() {
//        InMemoryUserDetailsManager manager = new InMemoryUserDetailsManager();
//        manager.createUser(User.withUsername("fernando")
//                .password("321")
//                .roles()
//                .build());
//        return manager;
//    }

//    esta Bean es especificamente para encriptacion de contraseñas
    @Bean
    PasswordEncoder contraseñaEncriptacion(){
        return new BCryptPasswordEncoder();
    }

    @Bean
    AuthenticationManager autenticacionManejador(HttpSecurity http_secure, PasswordEncoder contraseñaEncriptacion) throws Exception{
        return http_secure.getSharedObject(AuthenticationManagerBuilder.class)
                .userDetailsService(user_det_impl)
                .passwordEncoder(contraseñaEncriptacion)
                .and().build();

    }


}