package com.nat.geeklolspring.config;

import org.springframework.context.annotation.Bean;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

@EnableWebSecurity
public class SecurityConfig {


    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http
                .cors()
                .and()
                .csrf().disable()
                .httpBasic().disable()
                .sessionManagement()
                .sessionCreationPolicy(SessionCreationPolicy.STATELESS)
                .and()
                // 토큰 페이지 설정
                .authorizeRequests()
                .antMatchers("/","/user/**").permitAll()
                .antMatchers("/", "/find/**").permitAll()
                .antMatchers("/", "/recentGames/**").permitAll()
                .antMatchers("/", "/championMastery/**").permitAll()
                .antMatchers("/", "/realtimeGame/**").permitAll()
                .antMatchers("/", "/all-champion-mastery").permitAll()
                .antMatchers("/","/board/**").permitAll()
                .antMatchers("/","/detail/**").permitAll()
                .antMatchers("/", "/api/**").permitAll()
                .antMatchers("/", "/ranking/**").permitAll()
                .antMatchers("/", "/troll/**").permitAll()
                .antMatchers("/", "/game/**").permitAll()
                .antMatchers("/", "/admin/**").permitAll()
//                    .antMatchers("/**").hasRole("ADMIN")
                    //.antMatchers(HttpMethod.GET,"/").permitAll()
                .anyRequest().authenticated()
        ;



        return http.build();
    }

    @Bean
    public PasswordEncoder passwordEncoder(){
        return new BCryptPasswordEncoder();
    }
}
