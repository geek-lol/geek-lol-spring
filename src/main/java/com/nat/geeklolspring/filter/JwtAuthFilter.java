package com.nat.geeklolspring.filter;


import com.nat.geeklolspring.auth.TokenProvider;
import com.nat.geeklolspring.auth.TokenUserInfo;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.AbstractAuthenticationToken;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;


@Component
@Slf4j
@RequiredArgsConstructor
// 클라이언트가 전송한 토큰을 검사하는 필터
public class JwtAuthFilter extends OncePerRequestFilter {

    private final TokenProvider tokenProvider;


    // 필터가 해야할 일을 코딩
    @Override
    protected void doFilterInternal(
            HttpServletRequest request,
            HttpServletResponse response,
            FilterChain filterChain) throws ServletException, IOException {


        try {
            // 토큰을 파싱
            // 토큰은 요청 헤더에 포함되어 전송된다.
            String token = parseBearerToken(request);


            if (token != null) {
                // 토큰 위조검사
                TokenUserInfo userInfo = tokenProvider.validateAndGetTokenUserInfo(token);

                // 인가 정보 리스트
                List<SimpleGrantedAuthority> authorityList = new ArrayList<>();
                authorityList.add(new SimpleGrantedAuthority(userInfo.getRole().toString()));

                // 인증 완료 처리
                // - 스프링 시큐리티에게 인증정보를
                //   전달해서 전역적으로 앱에서
                //   인증정보를 활용할 수 있게 설정
                AbstractAuthenticationToken auth
                        = new UsernamePasswordAuthenticationToken(
                        userInfo, // 컨트롤러에서 활용할 유저정보
                        null, // 인증된 사용자의 비밀번호 - 보통 널값
                        authorityList // 인가 정보(권한 정보)
                );
                // 인증완료 처리시 클라이언트의 요청정보 세팅
                auth.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
                // 스프링 시큐리티 컨테이너에 인증정보객체 등록
                SecurityContextHolder.getContext().setAuthentication(auth);


            }
        } catch (Exception e) {
            e.printStackTrace();
            log.warn("토큰이 위조되었습니다.");
        }

        // 필터체인에 내가 만든 필터를 실행하도록 명령
        filterChain.doFilter(request, response);

    }

    // 요청헤더에서 토큰을 파싱하는 메서드
    private String parseBearerToken(HttpServletRequest request) {
        /*
             - 요청헤더에서 토큰 가져오기

             -- request header
             --> Content-type: application/json
             --> Authorization: Bearer sdjflk32j4lhdsfidbkjsfjhfj23lkrfhdkajbfdmnsb23
         */

        String bearerToken = request.getHeader("Authorization");

        // 헤더에서 가져온 토큰값 앞에 붙어있는 Bearer라는 문자열을 제거하는 코드
        if (StringUtils.hasText(bearerToken) && bearerToken.startsWith("Bearer")) {
            return bearerToken.substring(7);
        }
        return null;
    }

}