package com.nat.geeklolspring.user.dto.request;

import lombok.*;

@Setter
@Getter
@ToString
@EqualsAndHashCode
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class GoogleRequestAccessTokenDTO {

    private String code;
    private String client_id;
    private String clientSecret;
    private String redirect_uri;
    private String grant_type;

}
