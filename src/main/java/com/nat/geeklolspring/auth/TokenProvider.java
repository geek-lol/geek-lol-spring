package com.nat.geeklolspring.auth;


import com.nat.geeklolspring.entity.Role;
import com.nat.geeklolspring.entity.User;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.*;

@Service
@Slf4j
public class TokenProvider {

    @Value("${jwt.secret}")
    private String SECRET_KEY;

    public String createToken(User userEntity) {

        // 토큰 만료시간 생성
        Date expiry = Date.from(
                Instant.now().plus(1, ChronoUnit.DAYS)
        );

        // 추가 클레임 정의
        Map<String, Object> claims = new HashMap<>();
        claims.put("userId", userEntity.getId());
        claims.put("password", userEntity.getPassword());
        claims.put("userName",userEntity.getUserName());
        claims.put("profileImage",userEntity.getProfileImage());
        claims.put("role",userEntity.getRole());


        return Jwts.builder()
                .signWith(
                        Keys.hmacShaKeyFor(SECRET_KEY.getBytes())
                        , SignatureAlgorithm.HS512
                )

                .setClaims(claims)
                .setIssuer("geet-lol")
                .setIssuedAt(new Date())
                .setExpiration(expiry)
                .setSubject(userEntity.getId())
                .compact();
    }


    public TokenUserInfo validateAndGetTokenUserInfo(String token) {

        Claims claims = Jwts.parserBuilder()
                .setSigningKey(Keys.hmacShaKeyFor(SECRET_KEY.getBytes()))
                .build()
                .parseClaimsJws(token)
                .getBody();

        log.info("claims: {}", claims);

        return TokenUserInfo.builder()
//                .userId(claims.getSubject())
                .userId(claims.get("userId",String.class))
                .password(claims.get("password", String.class))
                .userName(claims.get("userName", String.class))
                .profileImage(claims.get("profileImage", String.class))
                .role(Role.valueOf(claims.get("role", String.class)))
                .build();
    }


}
