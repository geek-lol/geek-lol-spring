package com.nat.geeklolspring.board.bulletin.service;

import com.nat.geeklolspring.auth.TokenUserInfo;
import com.nat.geeklolspring.board.bulletin.dto.request.BoardBulletinModifyRequestDTO;
import com.nat.geeklolspring.board.bulletin.dto.request.BoardBulletinWriteRequestDTO;
import com.nat.geeklolspring.board.bulletin.dto.response.BoardBulletinDeleteResponseDTO;
import com.nat.geeklolspring.board.bulletin.dto.response.BoardBulletinDetailResponseDTO;
import com.nat.geeklolspring.board.bulletin.dto.response.BoardBulletinResponseDTO;
import com.nat.geeklolspring.board.bulletin.repository.BoardBulletinRepository;
import com.nat.geeklolspring.entity.BoardBulletin;
import com.nat.geeklolspring.entity.User;
import com.nat.geeklolspring.user.repository.UserRepository;
import com.nat.geeklolspring.utils.upload.FileUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@Slf4j
@RequiredArgsConstructor
public class BoardBulletinService {

    private final BoardBulletinRepository boardBulletinRepository;

    // 목록 불러오기
    public BoardBulletinResponseDTO retrieve() {

        List<BoardBulletin> boardBulletinList = boardBulletinRepository.findAll();

        List<BoardBulletinDetailResponseDTO> list = boardBulletinList.stream()
                .map(BoardBulletinDetailResponseDTO::new)
                .collect(Collectors.toList());

        return BoardBulletinResponseDTO.builder()
                .board(list)
                .build();

    }

    public BoardBulletinDetailResponseDTO detailRetrieve(Long bulletinId) {

        BoardBulletin boardBulletin = boardBulletinRepository.findById(bulletinId).get();

        log.info("{}",boardBulletin);

        return BoardBulletinDetailResponseDTO.builder()
                .bulletinId(boardBulletin.getBulletinId())
                .title(boardBulletin.getTitle())
                .posterId(boardBulletin.getPosterId())
                .content(boardBulletin.getBoardContent())
                .boardMedia(boardBulletin.getBoardMedia())
                .build();

    }

    // 글 생성
    public BoardBulletinDetailResponseDTO create(BoardBulletinWriteRequestDTO dto,
                                                 TokenUserInfo userInfo
                                                 ,String filePath
                                                 ){

        BoardBulletin boardBulletin = boardBulletinRepository.save(dto.toEntity(filePath));



        boardBulletin.setPosterId(userInfo.getUserId());

        BoardBulletin save = boardBulletinRepository.save(boardBulletin);

        log.info("{}",userInfo);


        return new BoardBulletinDetailResponseDTO(save);

    }

    //글 수정
    public BoardBulletinDetailResponseDTO modify(BoardBulletinModifyRequestDTO dto,String filePath){

        Optional<BoardBulletin> boardBulletin = boardBulletinRepository.findById(dto.getBulletinId());

        if (dto.getTitle() == null){
            dto.setTitle(boardBulletin.get().getTitle());
        }
        if (dto.getContent() == null){
            dto.setContent(boardBulletin.get().getBoardContent());
        }


        BoardBulletin save = boardBulletinRepository.save(dto.toEntity(dto.getBulletinId(),filePath));

        return new BoardBulletinDetailResponseDTO(save);

    }

    // 글 삭제

    public void delete(TokenUserInfo userInfo, BoardBulletinDeleteResponseDTO dto) {

        if (!Objects.equals(dto.getPosterId(), userInfo.getUserId())) {
            log.warn("삭제할 권한이 없습니다!! - {}", dto.getPosterId());
            throw new RuntimeException("삭제 권한이 없습니다");
        }

        log.info("dto : {}",dto.getBulletinId());
        log.info("userInfo : {}",userInfo);

//        boardBulletinRepository.deleteByBoardBulletinIdWithJPQL(dto.getBulletinId());

        boardBulletinRepository.deleteById(dto.getBulletinId());
    }





}

