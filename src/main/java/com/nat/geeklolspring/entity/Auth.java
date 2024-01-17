package com.nat.geeklolspring.entity;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.ToString;

@Getter @ToString
@AllArgsConstructor
public enum Auth {
    ADMIN("관리자 회원", 1),
    COMMON("일반 회원", 2)
    ;
    
    private String description; // 권한 설명
    private int authNumber; // 권한 번호
}
