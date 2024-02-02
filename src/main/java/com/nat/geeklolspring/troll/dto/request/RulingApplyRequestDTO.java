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
    private String applyLink;
    private String content;
    private String title;
    private String applyPosterId;

    public BoardApply toEntity(){
        return BoardApply.builder()
                .applyLink(applyLink)
                .content(content)
                .title(title)
                .applyPosterId(applyPosterId)
                .build();
    }
}
