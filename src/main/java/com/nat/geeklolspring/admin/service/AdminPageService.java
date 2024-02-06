package com.nat.geeklolspring.admin.service;

import com.nat.geeklolspring.admin.dto.Response.AdminPageBoardBulletinResponseDTO;
import com.nat.geeklolspring.admin.dto.Response.AdminPageShortsResponseDTO;
import com.nat.geeklolspring.admin.dto.Response.List.AdminPageBoardBulletinListResponseDTO;
import com.nat.geeklolspring.admin.dto.Response.List.AdminPageShortsListResponseDTO;
import com.nat.geeklolspring.admin.dto.Response.List.AdminPageUserListResponseDTO;
import com.nat.geeklolspring.admin.dto.Response.AdminPageUserResponseDTO;
import com.nat.geeklolspring.board.bulletin.repository.BoardBulletinRepository;
import com.nat.geeklolspring.entity.BoardBulletin;
import com.nat.geeklolspring.entity.BoardShorts;
import com.nat.geeklolspring.entity.User;
import com.nat.geeklolspring.exception.BadRequestException;
import com.nat.geeklolspring.shorts.shortsboard.repository.ShortsRepository;
import com.nat.geeklolspring.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@Slf4j
@RequiredArgsConstructor
public class AdminPageService {

    private final UserRepository userRepository;
    private final BoardBulletinRepository boardBulletinRepository;
    private final ShortsRepository shortsRepository;

    //문철 + 문철 지원 머지 안해서 연동 못함



    public AdminPageUserListResponseDTO userView(Pageable pageInfo){

        Pageable pageable = PageRequest.of(pageInfo.getPageNumber() - 1, pageInfo.getPageSize());
        Page<User> userListAll = userRepository.findAll(pageable);

        List<AdminPageUserResponseDTO> userList = userListAll.stream()
                .map(AdminPageUserResponseDTO::new)
                .collect(Collectors.toList());

        if(pageInfo.getPageNumber() > 1 && userList.isEmpty()) {
            // 페이징 처리된 페이지의 최대값보다 높게 요청시 에러 발생시키기
            throw new BadRequestException("비정상적인 접근입니다!");
        } else {
            return AdminPageUserListResponseDTO
                    .builder()
                    .user(userList)
                    .totalPages(userListAll.getTotalPages())
                    .totalCount(userListAll.getTotalElements())
                    .build();
        }
    }

    public AdminPageShortsListResponseDTO shortsView(Pageable pageInfo){

        Pageable pageable = PageRequest.of(pageInfo.getPageNumber() - 1, pageInfo.getPageSize());
        Page<BoardShorts> shortsListAll = shortsRepository.findAll(pageable);

        List<AdminPageShortsResponseDTO> shortsList = shortsListAll.stream()
                .map(AdminPageShortsResponseDTO::new)
                .collect(Collectors.toList());

        if(pageInfo.getPageNumber() > 1 && shortsList.isEmpty()) {
            throw new BadRequestException("비정상적인 접근입니다!");
        } else {
            return AdminPageShortsListResponseDTO
                    .builder()
                    .shorts(shortsList)
                    .totalPages(shortsListAll.getTotalPages())
                    .totalCount(shortsListAll.getTotalElements())
                    .build();
        }
    }

    public AdminPageBoardBulletinListResponseDTO boardView(Pageable pageInfo){

        Pageable pageable = PageRequest.of(pageInfo.getPageNumber() - 1, pageInfo.getPageSize());
        Page<BoardBulletin> boardListAll = boardBulletinRepository.findAll(pageable);

        List<AdminPageBoardBulletinResponseDTO> boardList = boardListAll.stream()
                .map(AdminPageBoardBulletinResponseDTO::new)
                .collect(Collectors.toList());

        if(pageInfo.getPageNumber() > 1 && boardList.isEmpty()) {
            throw new BadRequestException("비정상적인 접근입니다!");
        } else {
            return AdminPageBoardBulletinListResponseDTO
                    .builder()
                    .board(boardList)
                    .totalPages(boardListAll.getTotalPages())
                    .totalCount(boardListAll.getTotalElements())
                    .build();
        }
    }







}
