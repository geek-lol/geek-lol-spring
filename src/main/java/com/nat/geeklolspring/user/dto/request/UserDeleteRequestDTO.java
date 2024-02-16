package com.nat.geeklolspring.user.dto.request;

import lombok.*;

import java.util.List;

@Setter
@Getter
@ToString
@EqualsAndHashCode
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UserDeleteRequestDTO {
    private String id;
    private List<String> ids;
}
