package com.nat.geeklolspring.user.feign.google;


import com.nat.geeklolspring.config.FeignConfiguration;
import com.nat.geeklolspring.user.dto.request.GoogleRequestAccessTokenDTO;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

@FeignClient(value = "googleAuth", url="https://oauth2.googleapis.com", configuration = {FeignConfiguration.class})
public interface GoogleAuthApi {
    @PostMapping("/token")
    ResponseEntity<String> getAccessToken(@RequestBody GoogleRequestAccessTokenDTO requestDto);
}