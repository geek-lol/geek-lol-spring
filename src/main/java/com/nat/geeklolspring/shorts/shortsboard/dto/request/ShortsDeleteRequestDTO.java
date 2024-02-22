package com.nat.geeklolspring.shorts.shortsboard.dto.request;

import lombok.*;

import java.util.List;

@Setter
@Getter
@ToString
@EqualsAndHashCode
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ShortsDeleteRequestDTO {
    private List<Long> ids;
}

