package com.nat.geeklolspring.board.bulletin.service;

import com.nat.geeklolspring.board.bulletin.repository.BoardBulletinRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Service
@Slf4j
@RequiredArgsConstructor
public class BoardBulletinService {

    private final BoardBulletinRepository boardBulletinRepository;



}

