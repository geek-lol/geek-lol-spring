package com.nat.geeklolspring.troll.apply.service;

import com.nat.geeklolspring.auth.TokenUserInfo;
import com.nat.geeklolspring.entity.BoardApply;
import com.nat.geeklolspring.troll.apply.dto.request.RulingApplyRequestDTO;
import com.nat.geeklolspring.troll.apply.dto.response.RulingApplyDetailResponseDTO;
import com.nat.geeklolspring.troll.apply.dto.response.RulingApplyResponseDTO;
import com.nat.geeklolspring.troll.apply.repository.RulingApplyRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@Slf4j
@RequiredArgsConstructor
public class RulingApplyService {
    private final RulingApplyRepository rar;

    @Value("${upload.path}")
    private String rootPath;

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

    //게시물 사진, 동영상 파일을 서버에 저장
    public String uploadBoardImage(MultipartFile originalFile) throws IOException {

        // 루트 디렉토리가 존재하는지 확인 후 존재하지 않으면 생성한다.
        File rootDir = new File(rootPath);
        if (!rootDir.exists()) rootDir.mkdirs();

        //파일명을 유니크하게 변경
        String uniqueFileName = UUID.randomUUID() + "_" + originalFile.getOriginalFilename();

        //파일을 서버에 저장
        File uploadFile = new File(rootPath + "/" + uniqueFileName);
        originalFile.transferTo(uploadFile);


        return uniqueFileName;
    }


    // 게시물 저장
    public RulingApplyResponseDTO createBoard(RulingApplyRequestDTO dto,
                                              TokenUserInfo userInfo,
                                              MultipartFile boardFile) throws IOException {
        String boardImg = null;
        if (boardFile !=null){
            log.info(" file-name:{}",boardFile.getOriginalFilename());
            boardImg = uploadBoardImage(boardFile);
        }
        rar.save(dto.toEntity(boardImg,userInfo.getUserId()));
        return findAllBoard();
    }

    // 글 삭제
    public RulingApplyResponseDTO deleteBoard(TokenUserInfo userInfo, Long bno){
        BoardApply targetBoard = rar.findById(bno).orElseThrow();
        if (targetBoard == null) {
            throw new IllegalStateException("존재하지 않은 게시판입니다.");
        }
        if (targetBoard.getApplyPosterId().equals(userInfo.getUserId())){
            rar.delete(targetBoard);
            return findAllBoard();
        }else{
            throw new IllegalStateException("삭제 권한이 없습니다.");
        }
    }


    // 게시물 개별조회
    public RulingApplyDetailResponseDTO detailBoard(Long applyId){
        BoardApply boardApply = rar.findById(applyId).orElseThrow();
        RulingApplyDetailResponseDTO dto = new RulingApplyDetailResponseDTO(boardApply);
        return dto;
    }

    // 게시물 수정
    public void modityBoard(Long applyId){}

    //게시물 추천수 증가
    public RulingApplyDetailResponseDTO agrees(Long applyId){
        BoardApply targetBoard = rar.findById(applyId).orElseThrow();
        targetBoard.setUpCount(targetBoard.getUpCount()+1);
        return new RulingApplyDetailResponseDTO(rar.save(targetBoard));
    }


    // 기준일로 부터 3일 뒤 추천수 많은거 골라내기
    public BoardApply selectionOfTopic() {
        return null;
    }

}
