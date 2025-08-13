package com.rolesspringsecurity.demo.controller;

import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class TestRoleController {

    @GetMapping("/accesAdmin")
    @PreAuthorize("hasRole('ADMIN')")
    public String AccesAdmin() {
        return "Accediste como admin";
    }

    @GetMapping("/accesUser")
    @PreAuthorize("hasRole('USER')")
//    así elegimos para una sola ruta, varios permisos a la vez
//    @PreAuthorize("hasAnyRole('USER', 'ADMIN')")
    public String AccesUser() {
        return "Accediste como usuario";
    }

    @GetMapping("/accesInvited")
    @PreAuthorize("hasRole('INVITED')")
    public String AccesInvited() {
        return "Accediste como Invited";
    }

}
