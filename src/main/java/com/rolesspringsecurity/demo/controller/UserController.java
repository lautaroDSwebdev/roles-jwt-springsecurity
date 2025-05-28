package com.rolesspringsecurity.demo.controller;


import com.rolesspringsecurity.demo.DTO.CreateUserDTO;
import com.rolesspringsecurity.demo.entities.Erole;
import com.rolesspringsecurity.demo.entities.RolesEntity;
import com.rolesspringsecurity.demo.entities.UserEntity;
import com.rolesspringsecurity.demo.repositories.UserRepo;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Set;
import java.util.stream.Collectors;

@RestController
public class UserController {

    @Autowired
    private UserRepo user_repo;

    @GetMapping("/seguro")
    public String Hola() {
        return "Hola con seguridad";
    }

    @GetMapping("/sin-seguro")
    public String Hola2() {
        return "Hola sin seguridad";
    }

    @PostMapping("/crearUsuario")
    public ResponseEntity<?> crearUsuario(@Valid @RequestBody CreateUserDTO createUserDto){

        Set<RolesEntity> roles = createUserDto.getRoles().stream()
                .map(role -> RolesEntity.builder().name(Erole.valueOf(role))

                        .build()).collect(Collectors.toSet());

        UserEntity entity_user = UserEntity.builder()
                .username(createUserDto.getUsername())
                .password(createUserDto.getPassword())
                .email(createUserDto.getEmail())
                .roles(roles)
                .build();
        user_repo.save(entity_user);
        return ResponseEntity.ok(entity_user);
    }



    @DeleteMapping
    public String deleteUser(@RequestParam String id){
        user_repo.deleteById(Long.parseLong(id));
        return "Usuario borrado" .concat(id);
    }
}
