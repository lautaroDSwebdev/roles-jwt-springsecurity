package com.rolesspringsecurity.demo.config.security;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.security.Key;
import java.util.Date;
import java.util.function.Function;

@Component
public class JwtUtils {

    @Value("${jwt.secret.key}")
    private String SecretKey;

    @Value("${jwt.time.exp}")
    private String timeExpiration;

    //    esto va a servir para darle una clave de acceso unico al usuario que expira por seguridad, siempre se tiene que renovar
//        El bouilder es para construir la clave token
    public String generateAccesToken(String username) {
        return Jwts.builder()
//                le enviamos el sujeto o persona que envia el token
                .setSubject(username)
//                fecha de creacion del token
                .setIssuedAt(new Date(System.currentTimeMillis()))
//                fecha de expiracion del token
                .setExpiration(new Date(System.currentTimeMillis() + Long.parseLong(timeExpiration)))
//                para iniciar secion le ponemos doble factor de encriptacion, la firma que hicimos mas un algoritmo de enscriptacion
                .signWith(getSignatureKey(), SignatureAlgorithm.HS256)
                .compact();
    }

    //    Obtener firma del metodo para el inicio de sesion
    public Key getSignatureKey() {
//        Esto decodifica la clave token de acceso
        byte[] keyBytes = Decoders.BASE64.decode(SecretKey);
        return Keys.hmacShaKeyFor(keyBytes);
    }

    //    Validar el token de acceso
    public boolean isTokenValid(String token) {
        try {
//        El parseBouilder es para leer la clave token generada
            Jwts.parserBuilder()
                    .setSigningKey(getSignatureKey())
                    .build()
                    .parseClaimsJws(token)
                    .getBody();
            return true;
        } catch (Exception e) {
            System.out.println("Token invalido: " + e.getMessage());
            return false;
        }
    }

    //    Extraer todos los claims, osea los parametros que nos trae el body de json web token
    public Claims extractEntireClaims(String token) {
        return Jwts.parserBuilder()
                .setSigningKey(getSignatureKey())
                .build()
                .parseClaimsJws(token)
                .getBody();
    }

    //    Cuando queremos obtener un solo claim del body de json web token
    public <T> T getOneClaim(String token, Function<Claims, T> claimsTFunction) {
        Claims extractClaims = extractEntireClaims(token);
        return claimsTFunction.apply(extractClaims);
    }

    //    Aqui elegimos que claim queremos obtener en especifico
    public String getUsernameFromtToken(String token) {
        return getOneClaim(token, Claims::getSubject);
    }
}
