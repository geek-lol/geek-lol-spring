package com.nat.geeklolspring.utils.token;

import com.nat.geeklolspring.auth.TokenUserInfo;

import static com.nat.geeklolspring.entity.Role.ADMIN;

public class TokenUtil {
    public static boolean EqualsId(String id, TokenUserInfo userInfo) {
        if(userInfo.getRole().equals(ADMIN))
            return true;

        String userId = userInfo.getUserId();
        return id.equals(userId);
    }
}
