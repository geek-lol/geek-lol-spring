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

    private String password;

    private String userName;

    private String profileIamge;

    private Role role;

    public UserModifyRequestDTO(User user) {
        this.id = user.getId();
        this.password = user.getPassword();
        this.userName = user.getUserName();
        this.profileIamge = user.getProfileImage();
        this.role = user.getRole();
    }

    public User toEntity(PasswordEncoder encoder){
        return User.builder()
                .id(this.id)
                .profileImage(this.profileIamge)
                .password(encoder.encode(this.password))
                .userName(this.userName)
                .role(this.role)
                .build();
    }
    public User toEntity(){
        return User.builder()
                .id(this.id)
                .profileImage(this.profileIamge)
                .password(this.password)
                .userName(this.userName)
                .role(this.role)
                .build();
    }

}
