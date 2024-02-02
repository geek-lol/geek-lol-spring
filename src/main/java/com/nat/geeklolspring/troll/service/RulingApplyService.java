package com.nat.geeklolspring.troll.service;

import com.nat.geeklolspring.auth.TokenUserInfo;
import com.nat.geeklolspring.entity.BoardApply;
import com.nat.geeklolspring.entity.User;
import com.nat.geeklolspring.troll.dto.request.RulingApplyRequestDTO;
import com.nat.geeklolspring.troll.dto.response.RulingApplyDetailResponseDTO;
import com.nat.geeklolspring.troll.dto.response.RulingApplyResponseDTO;
import com.nat.geeklolspring.troll.repository.RulingApplyRepository;
import com.nat.geeklolspring.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@Slf4j
@RequiredArgsConstructor
public class RulingApplyService {
    private final RulingApplyRepository rar;
    private final UserRepository userRepository;

    // 목록 불러오기
    public RulingApplyResponseDTO findAllBoard() {
        List<BoardApply> boardApplyList = rar.findAll();

        List<RulingApplyDetailResponseDTO> list = boardApplyList.stream()
                .map(RulingApplyDetailResponseDTO::new)
                .collect(Collectors.toList());

        return RulingApplyResponseDTO.builder()
                .boardApply(list)
                .build();

    }
    // 게시물 저장
    public RulingApplyResponseDTO createBoard(RulingApplyRequestDTO dto,
                                              TokenUserInfo userInfo){
        User user = userRepository.findById(userInfo.getUserId()).orElseThrow();
        BoardApply boardApply = rar.save(dto.toEntity(user));
        return findAllBoard();
    }

    // 글 삭제
//
//    public void delete(TokenUserInfo userInfo, BoardBulletinDeleteResponseDTO dto) {
//
//        if (!Objects.equals(dto.getPosterId(), userInfo.getUserId())) {
//            log.warn("삭제할 권한이 없습니다!! - {}", dto.getPosterId());
//            throw new RuntimeException("삭제 권한이 없습니다");
//        }
//
//        log.info("dto : {}",dto.getBulletinId());
//        log.info("userInfo : {}",userInfo);
//
////        boardBulletinRepository.deleteByBoardBulletinIdWithJPQL(dto.getBulletinId());
//
//        boardBulletinRepository.deleteById(dto.getBulletinId());
//    }


    // 게시물 개별조회
    public BoardApply findOneBoard(Long applyId){
        return rar.findById(applyId).orElseThrow();

    }

    // 게시물 수정
    public void modityBoard(Long applyId){
        BoardApply targetBoard = findOneBoard(applyId);

    }

    // 게시물 삭제
    public void deleteBoard(){}

    //게시물 추천수 증감
    public void agrees(){}

    // 기준일로 부터 3일 뒤 추천수 많은거 골라내기
    public BoardApply selectionOfTopic() {
        return null;
    }

}
