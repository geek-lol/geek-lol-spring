package com.nat.geeklolspring.user.service;

import com.nat.geeklolspring.user.dto.response.SocialAutoResponseDTO;
import com.nat.geeklolspring.user.dto.response.SocialUserResponseDTO;
import org.hibernate.usertype.UserType;
import org.springframework.stereotype.Service;

@Service
public abstract class SocialLoginService {
    abstract SocialAutoResponseDTO getAccessToken(String authorizationCode);
    abstract SocialUserResponseDTO getUserInfo(String accessToken);
}
