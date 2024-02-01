package com.nat.geeklolspring.user.dto.request;


import com.nat.geeklolspring.entity.Role;
import com.nat.geeklolspring.entity.User;
import lombok.*;
import org.springframework.security.crypto.password.PasswordEncoder;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

@Setter
@Getter
@ToString
@EqualsAndHashCode
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UserModifyRequestDTO {


    @NotBlank
    private String id;

    @Size(min = 8,max = 20)
    private String password;

    private String userName;

    private String profileIamge;

    private Role role;


    public User toEntity(String id,PasswordEncoder encoder,String profileIamge,Role role){
        return User.builder()
                .id(id)
                .profileImage(profileIamge)
                .password(encoder.encode(this.password))
                .userName(this.userName)
                .role(role)
                .build();
    }


}
