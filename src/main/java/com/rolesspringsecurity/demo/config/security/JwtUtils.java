package com.rolesspringsecurity.demo.config.security;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
public class JwtUtils {

    @Value("${jwt.secret.key}")
    private String SecretKey;
    @Value("${jwt.time.exp}")
    private String  timeExpiration;
}
