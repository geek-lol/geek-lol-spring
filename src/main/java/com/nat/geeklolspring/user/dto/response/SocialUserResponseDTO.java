package com.nat.geeklolspring.user.dto.response;

import lombok.*;

@Setter
@Getter
@ToString
@EqualsAndHashCode
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class SocialUserResponseDTO {

    private String id;
    private String email;
    private String name;
    private String gender;
    private String birthday;
}
