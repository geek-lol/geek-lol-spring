package com.nat.geeklolspring.user.dto.response;

import com.nat.geeklolspring.entity.User;
import lombok.*;

import java.time.LocalDateTime;

@Setter
@Getter
@ToString
@EqualsAndHashCode
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UserResponseDTO {

    private String userId;
    private String userName;
    private String profileImage;
    private LocalDateTime joinMembershipDate;

    public UserResponseDTO(User user){
        this.userId = user.getId();
        this.userName = user.getUserName();
        this.profileImage = user.getProfileImage();
        this.joinMembershipDate = user.getJoinMembershipDate();
    }
}
