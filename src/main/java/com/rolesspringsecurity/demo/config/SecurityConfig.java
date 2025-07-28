package com.rolesspringsecurity.demo.config;

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

    @Bean
    public SecurityFilterChain filterChainConfig(HttpSecurity http) throws Exception {
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
                .build();
    }




    @Bean
    UserDetailsService detallesUsuario() {
        InMemoryUserDetailsManager manager = new InMemoryUserDetailsManager();
        manager.createUser(User.withUsername("fernando")
                .password("321")
                .roles()
                .build());
        return manager;
    }

//    esta Bean es especificamente para encriptacion de contraseñas
    @Bean
    PasswordEncoder contraseñaEncriptacion(){
        return new BCryptPasswordEncoder();
    }

    @Bean
    AuthenticationManager autenticacionManejador(HttpSecurity http_secure, PasswordEncoder contraseñaEncriptacion) throws Exception{
        return http_secure.getSharedObject(AuthenticationManagerBuilder.class)
                .userDetailsService(detallesUsuario())
                .passwordEncoder(contraseñaEncriptacion)
                .and().build();

    }
}