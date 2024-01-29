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
    private String email;
    private Role role;

}