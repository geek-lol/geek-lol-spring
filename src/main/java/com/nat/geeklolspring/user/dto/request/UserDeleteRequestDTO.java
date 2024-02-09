package com.nat.geeklolspring.user.dto.request;

import lombok.*;

@Setter
@Getter
@ToString
@EqualsAndHashCode
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UserDeleteRequestDTO {
    private String id;
}
