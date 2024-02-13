package com.nat.geeklolspring.troll.ruling.service;

import com.nat.geeklolspring.auth.TokenUserInfo;
import com.nat.geeklolspring.entity.BoardApply;
import com.nat.geeklolspring.entity.BoardRuling;
import com.nat.geeklolspring.entity.Role;
import com.nat.geeklolspring.troll.apply.dto.response.RulingApplyDetailResponseDTO;
import com.nat.geeklolspring.troll.ruling.dto.request.RulingDeleteRequestDTO;
import com.nat.geeklolspring.troll.ruling.dto.response.CurrentBoardListResponseDTO;
import com.nat.geeklolspring.troll.ruling.dto.response.RulingBoardDetailResponseDTO;
import com.nat.geeklolspring.troll.ruling.dto.response.RulingBoardListResponseDTO;
import com.nat.geeklolspring.troll.ruling.dto.response.RulingBoardResponseDTO;
import com.nat.geeklolspring.troll.ruling.repository.BoardRulingRepository;
import com.nat.geeklolspring.troll.ruling.repository.RulingReplyRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@Slf4j
@RequiredArgsConstructor
@Transactional
public class RulingBoardService {
    private final BoardRulingRepository boardRulingRepository;
    private final RulingReplyRepository rulingReplyRepository;


    @Value("${upload.path}")
    private String rootPath;
    // 최근 게시물 2건 조회
    public CurrentBoardListResponseDTO descRulingBoard(){
        //투표 게시판의 목록을 불러옴
        List<BoardRuling> topTwo = boardRulingRepository.findAllByOrderByRulingDateDesc();

        //목록이 비어있거나 1개인 경우
        if (topTwo.isEmpty()){
            return CurrentBoardListResponseDTO.builder()
                    .currentBoard(null)
                    .previousBoard(null)
                    .build();
        }else if (topTwo.size() == 1){
            return CurrentBoardListResponseDTO.builder()
                    .currentBoard(new RulingBoardResponseDTO(topTwo.get(0)))
                    .previousBoard(null)
                    .build();
        }

        //최근 2개의 게시판만 저장해서 리턴
        return new CurrentBoardListResponseDTO(topTwo);
    }
    //게시물 상세조회
    public RulingBoardDetailResponseDTO findDetailBoard(Long rulingId){
        BoardRuling boardRuling = boardRulingRepository.findById(rulingId).orElseThrow();
        boardRuling.setViewCount(boardRuling.getViewCount()+1);
        boardRulingRepository.save(boardRuling);
        return new RulingBoardDetailResponseDTO(boardRuling);
    }
    // 게시물 영상 주소
    public String getVideoPath(Long rulingId){
        BoardRuling boardRuling = boardRulingRepository.findById(rulingId).orElseThrow();
        String applyLink = boardRuling.getRulingLink();
        return rootPath+"/"+applyLink;
    }


    // 게시물 전체조회
    public RulingBoardListResponseDTO findAllRulingBoard(Pageable pageInfo){
        Pageable pageable = PageRequest.of(pageInfo.getPageNumber() - 1, pageInfo.getPageSize());
        //투표 게시판의 목록을 불러옴
        Page<BoardRuling> rulings = boardRulingRepository.findAllByOrderByRulingDateDesc(pageable);
        List<RulingBoardDetailResponseDTO> rulingList = rulings.stream()
                .map(board->{
                    RulingBoardDetailResponseDTO dto = new RulingBoardDetailResponseDTO(board);
                    dto.setReplyCount(rulingReplyRepository.countByRulingId(dto.getRulingId()));
                    return dto;
                })
                .collect(Collectors.toList());
        return RulingBoardListResponseDTO.builder()
                .rulingList(rulingList)
                .totalPages(rulings.getTotalPages())
                .build();
    }
    // 게시물 삭제
    public void deleteBoard(TokenUserInfo userInfo, RulingDeleteRequestDTO dto){
        BoardRuling targetBoard = boardRulingRepository.findById(dto.getId()).orElseThrow();
        if (userInfo.getRole().equals(Role.ADMIN)){
            if (dto.getIds() != null){
                dto.getIds()
                        .forEach(boardRulingRepository::deleteById);
            }else{
                boardRulingRepository.deleteById(dto.getId());
            }
        }else{
            throw new IllegalStateException("삭제 권한이 없습니다.");
        }
    }

    //게시물 내껏만 조회
    public RulingBoardListResponseDTO findMyBoard(Pageable pageInfo, TokenUserInfo userInfo){
        Pageable pageable = PageRequest.of(pageInfo.getPageNumber() - 1, pageInfo.getPageSize());
        Page<BoardRuling> rulings = boardRulingRepository.findAllByRulingPosterId(userInfo.getUserId(), pageable);
        List<RulingBoardDetailResponseDTO> rulingList = rulings.stream()
                .map(RulingBoardDetailResponseDTO::new)
                .collect(Collectors.toList());

        return RulingBoardListResponseDTO.builder()
                .rulingList(rulingList)
                .totalPages(rulings.getTotalPages())
                .build();

    }
}
