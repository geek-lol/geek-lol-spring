package com.nat.geeklolspring.mypage;

import lombok.*;

@Setter
@Getter
@ToString
@EqualsAndHashCode
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class MyPageResponseDTO {
    private int boardCount;
    private int replyCount;

}
