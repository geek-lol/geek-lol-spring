package com.nat.geeklolspring.user.dto.request;


import com.nat.geeklolspring.entity.Auth;
import com.nat.geeklolspring.entity.User;
import lombok.*;
import org.springframework.security.crypto.password.PasswordEncoder;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

import static com.nat.geeklolspring.entity.Auth.COMMON;

@Setter
@Getter
@ToString
@EqualsAndHashCode
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UserSignUpRequestDTO {


    @NotBlank
    private String id;

    @NotBlank
    @Size(min = 8,max = 20)
    private String password;

    @NotBlank
    private String userName;

    private String profileIamge;

    public User toEntity(PasswordEncoder encoder){
        return User.builder()
                .id(this.id)
                .profileImage(this.profileIamge)
                .password(encoder.encode(this.password))
                .userName(this.userName)
                .build();
    }




}
