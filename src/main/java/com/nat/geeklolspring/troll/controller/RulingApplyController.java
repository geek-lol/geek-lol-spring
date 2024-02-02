package com.nat.geeklolspring.troll.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@Slf4j
@RequestMapping("/troll/support")
public class RulingApplyController {

    //게시물 목록 조회
    @GetMapping
    public ResponseEntity<?> findAllBoard(){

        return null;
    }
}
