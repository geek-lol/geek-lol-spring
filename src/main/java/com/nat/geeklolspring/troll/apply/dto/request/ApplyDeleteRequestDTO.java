package com.nat.geeklolspring.troll.apply.dto.request;

import lombok.*;

import java.util.List;

@Setter
@Getter
@ToString
@EqualsAndHashCode
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ApplyDeleteRequestDTO {
    Long id;
    List<Long> idList;
}
