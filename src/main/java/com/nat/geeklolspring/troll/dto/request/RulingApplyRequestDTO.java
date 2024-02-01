package com.nat.geeklolspring.troll.dto.request;

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
}
