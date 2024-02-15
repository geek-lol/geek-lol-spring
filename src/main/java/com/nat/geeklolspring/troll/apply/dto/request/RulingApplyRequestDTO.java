package com.nat.geeklolspring.troll.apply.dto.request;

import com.nat.geeklolspring.auth.TokenUserInfo;
import com.nat.geeklolspring.entity.BoardApply;
import com.nat.geeklolspring.entity.User;
import lombok.*;

import java.util.ArrayList;

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

    public BoardApply toEntity(String applyLink, User user){
        return BoardApply.builder()
                .applyLink(applyLink)
                .content(content)
                .title(title)
                .userId(user)
                .applyReplies(new ArrayList<>())
                .build();
    }
}
