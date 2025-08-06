package com.rolesspringsecurity.demo;

import com.rolesspringsecurity.demo.entities.Erole;
import com.rolesspringsecurity.demo.entities.RolesEntity;
import com.rolesspringsecurity.demo.entities.UserEntity;
import com.rolesspringsecurity.demo.repositories.UserRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Set;

@SpringBootApplication
public class DemoApplication {

    public static void main(String[] args) {
        SpringApplication.run(DemoApplication.class, args);
    }

    @Autowired
    PasswordEncoder passw_encoder;

    @Autowired
    UserRepo user_repo;

    @Bean
    CommandLineRunner init() {
        return args -> {

            UserEntity user_ent = UserEntity.builder()
                    .email("maildeejemplo@gmail.com")
                    .username("lautaro")
                    .password(passw_encoder.encode("654345"))
                    .roles(Set.of(RolesEntity.builder()
                            .name(Erole.valueOf(Erole.ADMIN.name())).build()))
                    .build();
            UserEntity user_ent2 = UserEntity.builder()
                    .email("guillermo2@gmail.com")
                    .username("guillermo2")
                    .password(passw_encoder.encode("7654747"))
                    .roles(Set.of(RolesEntity.builder()
                            .name(Erole.valueOf(Erole.USER.name())).build()))
                    .build();


            user_repo.save(user_ent);
            user_repo.save(user_ent2);
        };
    }
}
