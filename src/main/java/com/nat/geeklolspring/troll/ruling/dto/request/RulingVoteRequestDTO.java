package com.nat.geeklolspring.troll.ruling.dto.request;


import lombok.*;
import org.springframework.web.bind.annotation.RequestParam;

@Setter
@Getter
@ToString
@EqualsAndHashCode
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class RulingVoteRequestDTO {

    private String vote;
    private Long rulingId;
}
