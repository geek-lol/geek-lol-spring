package com.nat.geeklolspring.user.dto.response;

import com.nat.geeklolspring.entity.*;
import lombok.*;

import java.util.List;

@Setter
@Getter
@ToString
@EqualsAndHashCode
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class LoginResponseDTO {

    private String id;
    private String userName;
    private String profileImage;
    private String role;
    /*
    private List<BoardBulletion> poster; // 자갤
    private List<BoardShorts> uploaderId; // 쇼츠
    private List<BoardRuling> rulingPoster; // 문철
     */
    private String token;

    public LoginResponseDTO(User user,String token){
        this.id = user.getId();
        this.userName = user.getUserName();
        this.profileImage = user.getProfileImage();
        /*
        this.poster = user.getPoster();
        this.uploaderId = user.getUploaderId();
        this.rulingPoster = user.getRulingPoster();
         */
        this.role = user.getRole().toString();
        this.token = token;
    }

}
