package com.nat.geeklolspring.auth;

import com.nat.geeklolspring.entity.Role;
import lombok.*;

@Setter
@Getter
@ToString
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode
@Builder
public class TokenUserInfo {

    private String userId;
    private String userName;
    private String password;
    private String profileImage;
    private Role role;
    private boolean autoLogin;

}