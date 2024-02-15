package com.nat.geeklolspring.user.service;


import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.nat.geeklolspring.user.dto.request.GoogleRequestAccessTokenDTO;
import com.nat.geeklolspring.user.dto.response.GoogleLoginResponseDTO;
import com.nat.geeklolspring.user.dto.response.SocialAutoResponseDTO;
import com.nat.geeklolspring.user.dto.response.SocialUserResponseDTO;
import com.nat.geeklolspring.user.feign.google.GoogleAuthApi;
import com.nat.geeklolspring.user.feign.google.GoogleUserApi;
import com.nat.geeklolspring.utils.localDate.GsonLocalDateTimeAdapter;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.hibernate.usertype.UserType;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Slf4j
@Service
@Qualifier("googleLogin")
public class GoogleLoginServiceImpl extends SocialLoginService {

    private GoogleAuthApi googleAuthApi;
    private GoogleUserApi googleUserApi;

    @Value("${social.client.google.rest-api-key}")
    private String googleAppKey;
    @Value("${social.client.google.secret-key}")
    private String googleAppSecret;
    @Value("${social.client.google.redirect-uri}")
    private String googleRedirectUri;
    @Value("${social.client.google.grant_type}")
    private String googleGrantType;

    public GoogleLoginServiceImpl() {

    }


    @Override
    public SocialAutoResponseDTO getAccessToken(String authorizationCode) {
        ResponseEntity<?> response = googleAuthApi.getAccessToken(
                GoogleRequestAccessTokenDTO.builder()
                        .code(authorizationCode)
                        .client_id(googleAppKey)
                        .clientSecret(googleAppSecret)
                        .redirect_uri(googleRedirectUri)
                        .grant_type(googleGrantType)
                        .build()
        );

        log.info("google auth info");
        log.info(response.toString());

        return new Gson()
                .fromJson(
                        response.getBody().toString(),
                        SocialAutoResponseDTO.class
                );
    }

    @Override
    public SocialUserResponseDTO getUserInfo(String accessToken) {
        ResponseEntity<?> response = googleUserApi.getUserInfo(accessToken);

        log.info("google user response");
        log.info(response.toString());

        String jsonString = response.getBody().toString();

        Gson gson = new GsonBuilder()
                .setPrettyPrinting()
                .registerTypeAdapter(LocalDateTime.class, new GsonLocalDateTimeAdapter())
                .create();

        GoogleLoginResponseDTO googleLoginResponse = gson.fromJson(jsonString, GoogleLoginResponseDTO.class);

        return SocialUserResponseDTO.builder()
                .id(googleLoginResponse.getId())
                .email(googleLoginResponse.getEmail())
                .build();
    }
}