package com.nat.geeklolspring.troll.ruling.dto.request;

import lombok.*;

import java.util.List;

@Setter
@Getter
@ToString
@EqualsAndHashCode
@NoArgsConstructor
@AllArgsConstructor

@Builder

public class RulingDeleteRequestDTO {
    private Long id;
    private List<Long> ids;
}
