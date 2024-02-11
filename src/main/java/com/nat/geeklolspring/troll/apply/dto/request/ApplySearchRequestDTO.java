package com.nat.geeklolspring.troll.apply.dto.request;

import lombok.*;

@Setter
@Getter
@ToString
@EqualsAndHashCode
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ApplySearchRequestDTO {
    // 검색 종류 
    private String type;
    // 사용자가 입력한 검색내용
    private String keyword;
    
}
