package com.nat.geeklolspring.troll.apply.service;

import com.nat.geeklolspring.auth.TokenUserInfo;
import com.nat.geeklolspring.entity.BoardApply;
import com.nat.geeklolspring.troll.apply.dto.request.ApplyDeleteRequestDTO;
import com.nat.geeklolspring.troll.apply.dto.request.ApplySearchRequestDTO;
import com.nat.geeklolspring.troll.apply.dto.request.RulingApplyRequestDTO;
import com.nat.geeklolspring.troll.apply.dto.response.RulingApplyDetailResponseDTO;
import com.nat.geeklolspring.troll.apply.dto.response.RulingApplyResponseDTO;
import com.nat.geeklolspring.troll.apply.repository.RulingApplyRepository;
import com.nat.geeklolspring.troll.ruling.dto.response.RulingBoardDetailResponseDTO;
import com.nat.geeklolspring.troll.ruling.repository.BoardRulingRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import javax.transaction.Transactional;
import java.io.File;
import java.io.IOException;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@Slf4j
@Transactional
@RequiredArgsConstructor
public class RulingApplyService {

    private final RulingApplyRepository rulingApplyRepository;
    private final BoardRulingRepository boardRulingRepository;

    @Value("${upload.path}")
    private String rootPath;

    // 목록 전체 조회
    public RulingApplyResponseDTO findAllBoard(Pageable pageInfo, String orderType) {
        Pageable pageable = PageRequest.of(pageInfo.getPageNumber() - 1, pageInfo.getPageSize());
        Page<BoardApply> boardApplyList = rulingApplyRepository.findAllByOrderByApplyDateDesc(pageable);;
        if(orderType == null)
            orderType = "";

        if (orderType.equals("like")) {
            boardApplyList = rulingApplyRepository.findAllByOrderByUpCountDesc(pageable);
        }
        else {
            boardApplyList = rulingApplyRepository.findAllByOrderByApplyDateDesc(pageable);
        }

        List<RulingApplyDetailResponseDTO> list = boardApplyList.stream()
                .map(RulingApplyDetailResponseDTO::new)
                .collect(Collectors.toList());

        return RulingApplyResponseDTO.builder()
                .boardApply(list)
                .totalPages(boardApplyList.getTotalPages())
                .totalCount(boardApplyList.getTotalElements())
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
    public RulingApplyDetailResponseDTO createBoard(
            RulingApplyRequestDTO dto,
            TokenUserInfo userInfo,
            MultipartFile boardFile) throws IOException {
        String boardImg = null;
        if (boardFile !=null){
            log.info(" file-name:{}",boardFile.getOriginalFilename());
            boardImg = uploadBoardImage(boardFile);
        }
        BoardApply boardApply = rulingApplyRepository.save(dto.toEntity(boardImg, userInfo));
        return new RulingApplyDetailResponseDTO(boardApply);
    }

    // 글 삭제
    public void deleteBoard(TokenUserInfo userInfo, ApplyDeleteRequestDTO dto) {
        if (dto.getIdList() != null) {
            dto.getIdList()
                    .forEach(rulingApplyRepository::deleteById);

        } else {
            BoardApply targetBoard = rulingApplyRepository.findById(dto.getId()).orElseThrow();
            if (userInfo.getRole().toString().equals("ADMIN") || targetBoard.getApplyPosterId().equals(userInfo.getUserId())) {
                rulingApplyRepository.delete(targetBoard);
            } else {
                throw new IllegalStateException("삭제 권한이 없습니다.");
            }
        }
    }


    // 게시물 개별조회
    public RulingApplyDetailResponseDTO detailBoard(Long applyId){
        BoardApply boardApply = rulingApplyRepository.findById(applyId).orElseThrow();
        return new RulingApplyDetailResponseDTO(viewCountUp(boardApply));
    }

    // 게시물 영상 주소
    public String getVideoPath(Long applyId){
        BoardApply boardApply = rulingApplyRepository.findById(applyId).orElseThrow();
        String applyLink = boardApply.getApplyLink();
        return rootPath+"/"+applyLink;
    }

    //조회수 증가
    public BoardApply viewCountUp(BoardApply boardApply){
        boardApply.setViewCount(boardApply.getViewCount()+1);
        return rulingApplyRepository.save(boardApply);
    }

    // 게시물 수정
    public void modityBoard(Long applyId){}

    //게시물 추천수 증가
    public void agrees(Long applyId){
        BoardApply targetBoard = rulingApplyRepository.findById(applyId).orElseThrow();
        targetBoard.setUpCount(targetBoard.getUpCount()+1);
        rulingApplyRepository.save(targetBoard);
    }


    // 기준일로 부터 3일 뒤 추천수 많은거 골라내서 board_ruling에 저장
    @Scheduled(cron = "0 0 0 */3 * *")
    public void selectionOfTopic() {
        log.info("스케줄링 실행중!!");
        // 현재 시간
        LocalDateTime now = LocalDateTime.now().withHour(0).withMinute(0).withSecond(0).withNano(0);
        // 현재 시간으로부터 3일전
        LocalDateTime threeDaysAgo = now.minusDays(3);

        // 3일 동안 추천수가 가장 많은 게시물
        BoardApply BestBoard = rulingApplyRepository.findFirstByApplyDateBetweenOrderByUpCountDescReportCountDesc(threeDaysAgo, now);
        RulingBoardDetailResponseDTO rulingDto = new RulingBoardDetailResponseDTO(BestBoard);
        rulingDto.setApplyPosterName(BestBoard.getApplyPosterName());
        boardRulingRepository.save(rulingDto.toEntity());

    }

    // 게시물 검색
    public RulingApplyResponseDTO serchToBoard(ApplySearchRequestDTO dto, Pageable pageInfo){
        Pageable pageable = PageRequest.of(pageInfo.getPageNumber() - 1, pageInfo.getPageSize());
        Page<BoardApply> boardApplyList;

        switch (dto.getType()){
            case "title":
                boardApplyList = rulingApplyRepository.findByTitleContaining(dto.getKeyword(),pageable);
                break;
            case "writer":
                boardApplyList = rulingApplyRepository.findByApplyPosterNameContaining(dto.getKeyword(), pageable);
                break;
            case "mix":
                boardApplyList = rulingApplyRepository.findByTitleContainingAndContentContaining(dto.getKeyword(),dto.getKeyword(),pageable);
                break;
            default:
                boardApplyList = rulingApplyRepository.findAllByOrderByApplyDateDesc(pageable);
        }

        List<RulingApplyDetailResponseDTO> list = boardApplyList.stream()
                .map(RulingApplyDetailResponseDTO::new)
                .collect(Collectors.toList());

        return RulingApplyResponseDTO.builder()
                .boardApply(list)
                .totalPages(boardApplyList.getTotalPages())
                .totalCount(boardApplyList.getTotalElements())
                .build();
    }

    //로그인 한 사람의 게시물 조회
    public RulingApplyResponseDTO findByUserId(TokenUserInfo userInfo, Pageable pageInfo){
        Pageable pageable = PageRequest.of(pageInfo.getPageNumber() - 1, pageInfo.getPageSize());
        Page<BoardApply> boardApplyList = rulingApplyRepository.findByApplyPosterId(userInfo.getUserId(), pageable);

        List<RulingApplyDetailResponseDTO> list = boardApplyList.stream()
                .map(RulingApplyDetailResponseDTO::new)
                .collect(Collectors.toList());

        return RulingApplyResponseDTO.builder()
                .boardApply(list)
                .totalPages(boardApplyList.getTotalPages())
                .totalCount(boardApplyList.getTotalElements())
                .build();
    }

}
