package com.nat.geeklolspring.board.bulletin.service;

import com.nat.geeklolspring.auth.TokenUserInfo;
import com.nat.geeklolspring.board.bulletin.dto.request.BoardBulletinModifyRequestDTO;
import com.nat.geeklolspring.board.bulletin.dto.request.BoardBulletinWriteRequestDTO;
import com.nat.geeklolspring.board.bulletin.dto.response.BoardBulletinDeleteResponseDTO;
import com.nat.geeklolspring.board.bulletin.dto.response.BoardBulletinDetailResponseDTO;
import com.nat.geeklolspring.board.bulletin.dto.response.BoardBulletinResponseDTO;
import com.nat.geeklolspring.board.bulletin.repository.BoardBulletinRepository;
import com.nat.geeklolspring.entity.BoardBulletin;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@Slf4j
@RequiredArgsConstructor
public class BoardBulletinService {

    @Value("${upload.path}")
    private String rootPath;

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

    // 글 생성
    public BoardBulletinDetailResponseDTO create(BoardBulletinWriteRequestDTO dto,
                                                 TokenUserInfo userInfo
                                                 , String fileUrl
                                                 ){

        BoardBulletin boardBulletin = boardBulletinRepository.save(dto.toEntity(fileUrl));

        boardBulletin.setPosterId(userInfo.getUserId());

        boardBulletin.setPosterName(userInfo.getUserName());

        BoardBulletin save = boardBulletinRepository.save(boardBulletin);

        log.info("{}",userInfo);


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

    public void delete(BoardBulletinDeleteResponseDTO dto) {

        log.info("dto : {}",dto.getBulletinId());

        if (dto.getIds() != null){
            dto.getIds().forEach(boardBulletinRepository::deleteById);
        }else{
            boardBulletinRepository.deleteById(dto.getBulletinId());
        }

    }


    // 목록 불러오기
    public BoardBulletinResponseDTO retrieve(String titleKeyword,String posterKeyword,String contentKeyword, Pageable pageInfo,int upCountView) {

        Pageable pageable = PageRequest.of(pageInfo.getPageNumber() - 1, pageInfo.getPageSize());
        Page<BoardBulletin> boardBulletinList;
        log.info("upCount : {}",upCountView);

        if (upCountView == 0){
            if(titleKeyword != null)
                boardBulletinList = boardBulletinRepository.findByTitleContainingOrderByBoardDateDesc(titleKeyword, pageable);
            else if(posterKeyword != null)
                boardBulletinList = boardBulletinRepository.findByPosterIdContainingOrderByBoardDateDesc(posterKeyword,pageable);
            else if(contentKeyword != null)
                boardBulletinList = boardBulletinRepository.findByBoardContentContainingOrderByBoardDateDesc(contentKeyword,pageable);
            else
                boardBulletinList = boardBulletinRepository.findAllByOrderByBoardDateDesc(pageable);
        }else {
            if(titleKeyword != null)
                boardBulletinList = boardBulletinRepository.findByTitleContainingOrderByUpCountDesc(titleKeyword, pageable);
            else if(posterKeyword != null)
                boardBulletinList = boardBulletinRepository.findByPosterIdContainingOrderByUpCountDesc(posterKeyword,pageable);
            else if(contentKeyword != null)
                boardBulletinList = boardBulletinRepository.findByBoardContentContainingOrderByUpCountDesc(contentKeyword,pageable);
            else
                boardBulletinList = boardBulletinRepository.findAllByOrderByUpCountDesc(pageable);
        }


        List<BoardBulletinDetailResponseDTO> list = boardBulletinList.stream()
                .map(BoardBulletinDetailResponseDTO::new)
                .collect(Collectors.toList());

        return BoardBulletinResponseDTO.builder()
                .board(list)
                .totalCount(boardBulletinList.getTotalElements())
                .totalPages(boardBulletinList.getTotalPages())
                .build();

    }

    public BoardBulletinDetailResponseDTO detailRetrieve(String bulletinId) {

        BoardBulletin boardBulletin = boardBulletinRepository.findById(Long.valueOf(bulletinId)).get();

        log.info("{}",boardBulletin);

        boardBulletin.setViewCount(boardBulletin.getViewCount()+1);

        boardBulletinRepository.save(boardBulletin);

        return BoardBulletinDetailResponseDTO.builder()
                .posterName(boardBulletin.getPosterName())
                .bulletinId(boardBulletin.getBulletinId())
                .title(boardBulletin.getTitle())
                .posterId(boardBulletin.getPosterId())
                .content(boardBulletin.getBoardContent())
                .boardMedia(boardBulletin.getBoardMedia())
                .localDateTime(boardBulletin.getBoardDate())
                .viewCount(boardBulletin.getViewCount())
                .build();

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

        dto.setBoardDate(boardBulletin.get().getBoardDate());
        dto.setPosterName(boardBulletin.get().getPosterName());
        dto.setViewCount(boardBulletin.get().getViewCount());
        dto.setBoardReportCount(boardBulletin.get().getBoardReportCount());
        dto.setUpCount(boardBulletin.get().getUpCount());

        log.info("dto : {}",dto);

        BoardBulletin saveData = boardBulletinRepository.save(dto.toEntity(dto.getBulletinId(),filePath,dto.getTitle(),dto.getContent()));

        return new BoardBulletinDetailResponseDTO(saveData);

    }

    //내가 쓴 글 조회 하기
    public BoardBulletinResponseDTO findByMyBullentin(Pageable pageInfo, TokenUserInfo userInfo){
        Pageable pageable = PageRequest.of(pageInfo.getPageNumber() - 1, pageInfo.getPageSize());

        Page<BoardBulletin> boardBulletinList = boardBulletinRepository.findAllByPosterId(userInfo.getUserId(), pageable);

        List<BoardBulletinDetailResponseDTO> list = boardBulletinList.stream()
                .map(BoardBulletinDetailResponseDTO::new)
                .collect(Collectors.toList());

        return BoardBulletinResponseDTO.builder()
                .board(list)
                .totalPages(boardBulletinList.getTotalPages())
                .build();

    }
    public String uploadImage(MultipartFile originalFile) throws IOException {

        // 루트 디렉토리가 존재하는지 확인 후 존재하지 않으면 생성한다
        File rootDir = new File(rootPath);
        if (!rootDir.exists()) rootDir.mkdirs();

        // 파일명을 유니크하게 변경
        String uniqueFileName = UUID.randomUUID() + "_" + originalFile.getOriginalFilename();

        // 파일을 서버에 저장
        File uploadFile = new File(rootPath + "/" + uniqueFileName);
        originalFile.transferTo(uploadFile);

        return uniqueFileName;
    }

    public String getImagePath(Long id){

        //DB에서 파일명 조회
        BoardBulletin boardBulletin = boardBulletinRepository.findById(id).orElseThrow();
        String fileName = boardBulletin.getBoardMedia();

        return rootPath+"/"+fileName;

    }




}

