package com.nat.geeklolspring.user.dto.response;


import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.nat.geeklolspring.entity.Role;
import com.nat.geeklolspring.entity.User;
import lombok.*;

import java.time.LocalDateTime;

import static com.nat.geeklolspring.entity.Role.COMMON;

@Setter
@Getter
@ToString
@EqualsAndHashCode
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UserSignUpResponseDTO {

    private String id;
    private String userName;
    private String profileImage;
    private int reportCount;
    private Role role = COMMON;


    @JsonProperty("join-date")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime joinDate;


    // 엔터티를 DTO로 변경하는 생성자
    public UserSignUpResponseDTO(User user) {
        this.id = user.getId();
        this.role = user.getRole();
        this.profileImage = user.getProfileImage();
        this.userName = user.getUserName();
        this.joinDate = user.getJoinMembershipDate();
    }

}
