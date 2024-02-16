package com.nat.geeklolspring.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class CorsConfig implements WebMvcConfigurer {

    @Override
    public void addCorsMappings(CorsRegistry registry) {
        registry
                .addMapping("/**")// 어떤 요청 url을 허용할 지
                .allowedOrigins("http://localhost:3000", "http://geeklol.site")// 어떤 클라이언트를 허용할 지
                .allowedMethods("*")
                .allowedHeaders("*")
                .allowCredentials(true)
                .maxAge(3600)
        ;
    }
}
