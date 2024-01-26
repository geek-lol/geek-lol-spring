package com.nat.geeklolspring.shorts.vote.dto.request;

import com.nat.geeklolspring.entity.VoteCheck;
import lombok.*;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

@Setter
@Getter
@ToString
@EqualsAndHashCode
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class VotePostRequestDTO {
    @NotNull
    private String receiver;

    @NotNull
    private Long shortsId;

    public VoteCheck toEntity(VotePostRequestDTO dto) {
        return VoteCheck.builder()
                .receiver(dto.receiver)
                .shortsId(dto.shortsId)
                .build();
    }
}
