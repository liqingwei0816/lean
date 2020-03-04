package com.github.config.security.jwt;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Date;

@Component
public class JWTUtil {

    public static String secretKey = "qwerty123456";

    public  String getToken(String subject, String authorities) {
        Date from = Date.from(LocalDateTime.now().plusDays(30).atZone(ZoneId.systemDefault()).toInstant());
        return subject+"-"+Jwts.builder()
                .claim("authorities", authorities)//配置用户角色
                .setSubject(subject)
                .setExpiration(from)
                .signWith(SignatureAlgorithm.HS512, secretKey)
                .compact();
    }

    public Claims getClaims(String token){
        return Jwts.parser().setSigningKey(secretKey).parseClaimsJws(token)
                .getBody();
    }




}
