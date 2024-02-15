package com.nat.geeklolspring.user.dto.response;

import lombok.*;

@Setter
@Getter
@ToString
@Data
@EqualsAndHashCode
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class GoogleLoginResponseDTO {
    private String id;
    private String email;
    private String picture;
}
