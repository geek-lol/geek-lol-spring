package com.nat.geeklolspring.troll.apply.dto.request;

import com.nat.geeklolspring.entity.BoardApply;
import lombok.*;

@Setter
@Getter
@ToString
@EqualsAndHashCode
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class RulingApplyRequestDTO {
    private String content;
    private String title;

    public BoardApply toEntity(String applyLink, String userId){
        return BoardApply.builder()
                .applyLink(applyLink)
                .content(content)
                .title(title)
                .applyPosterId(userId)
                .build();
    }
}
