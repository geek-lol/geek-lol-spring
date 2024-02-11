package com.nat.geeklolspring.troll.apply.dto.request;

import com.nat.geeklolspring.auth.TokenUserInfo;
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

    public BoardApply toEntity(String applyLink, TokenUserInfo userInfo){
        return BoardApply.builder()
                .applyLink(applyLink)
                .content(content)
                .title(title)
                .applyPosterId(userInfo.getUserId())
                .applyPosterName(userInfo.getUserName())
                .build();
    }
}
