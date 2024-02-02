package com.nat.geeklolspring.troll.dto.request;

import com.nat.geeklolspring.entity.BoardApply;
import com.nat.geeklolspring.entity.User;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;

import javax.persistence.Column;
import java.time.LocalDateTime;

@Setter
@Getter
@ToString
@EqualsAndHashCode
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class RulingApplyRequestDTO {
    private Long applyId;
    private String applyLink;
    private String content;
    private String title;
    private User applyPosterId;

    public BoardApply toEntity(User user){
        return BoardApply.builder()
                .applyId(applyId)
                .applyLink(applyLink)
                .content(content)
                .title(title)
                .applyPosterId(user)
                .build();
    }
}
