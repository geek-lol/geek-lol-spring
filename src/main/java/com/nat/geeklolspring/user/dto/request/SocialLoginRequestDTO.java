package com.nat.geeklolspring.user.dto.request;

import lombok.*;

import javax.validation.constraints.NotNull;

@Setter
@Getter
@ToString
@EqualsAndHashCode
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class SocialLoginRequestDTO {

    @NotNull
    private String code;


}
