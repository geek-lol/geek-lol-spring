package com.nat.geeklolspring.troll.apply.service;

import com.nat.geeklolspring.auth.TokenUserInfo;
import com.nat.geeklolspring.aws.S3Service;
import com.nat.geeklolspring.entity.BoardApply;
import com.nat.geeklolspring.entity.BoardRuling;
import com.nat.geeklolspring.entity.Role;
import com.nat.geeklolspring.entity.User;
import com.nat.geeklolspring.troll.apply.dto.request.ApplyDeleteRequestDTO;
import com.nat.geeklolspring.troll.apply.dto.request.ApplySearchRequestDTO;
import com.nat.geeklolspring.troll.apply.dto.request.RulingApplyRequestDTO;
import com.nat.geeklolspring.troll.apply.dto.response.RulingApplyDetailResponseDTO;
import com.nat.geeklolspring.troll.apply.dto.response.RulingApplyResponseDTO;
import com.nat.geeklolspring.troll.apply.repository.ApplyReplyRepository;
import com.nat.geeklolspring.troll.apply.repository.RulingApplyRepository;
import com.nat.geeklolspring.troll.ruling.dto.response.RulingBoardDetailResponseDTO;
import com.nat.geeklolspring.troll.ruling.repository.BoardRulingRepository;
import com.nat.geeklolspring.user.repository.UserRepository;
import com.nat.geeklolspring.utils.files.Videos;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.MediaType;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.PostConstruct;
import javax.transaction.Transactional;
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
    private final ApplyReplyRepository applyReplyRepository;
    private final UserRepository userRepository;

    @Value("${upload.path}")
    private String rootPath;
    //투표 끝나는 시간
    LocalDateTime threeDayafter;
    //서버 실행 시간
    LocalDateTime startServerTime;
    // 목록 전체 조회
    public RulingApplyResponseDTO findAllBoard(Pageable pageInfo, String orderType) {
        Pageable pageable = PageRequest.of(pageInfo.getPageNumber() - 1, pageInfo.getPageSize());
        Page<BoardApply> boardApplyList = rulingApplyRepository.findAllByOrderByApplyDateDesc(pageable);
        if(orderType == null)
            orderType = "";
        log.warn("orderType:{}",orderType);
        if (orderType.equals("like")) {
            boardApplyList = rulingApplyRepository.findAllByOrderByUpCountDesc(pageable);
        }
        else {
            boardApplyList = rulingApplyRepository.findAllByOrderByApplyDateDesc(pageable);
        }
        log.warn("정렬된 boardList : {} ",boardApplyList);
        List<RulingApplyDetailResponseDTO> list = boardApplyList.stream()
                .map(boardApply -> {
                    int i = applyReplyRepository.countByApplyId(boardApply);
                    return new RulingApplyDetailResponseDTO(boardApply,i);
                })
                .collect(Collectors.toList());

        return RulingApplyResponseDTO.builder()
                .boardApply(list)
                .totalPages(boardApplyList.getTotalPages())
                .totalCount(boardApplyList.getTotalElements())
                .build();

    }

    private final S3Service s3Service;
    //게시물 사진, 동영상 파일을 서버에 저장
    public String uploadBoardImage(MultipartFile originalFile) throws IOException {

        // 루트 디렉토리가 존재하는지 확인 후 존재하지 않으면 생성한다.
        //File rootDir = new File(rootPath);
        //if (!rootDir.exists()) rootDir.mkdirs();

        //파일명을 유니크하게 변경
        String uniqueFileName = UUID.randomUUID() + "_" + originalFile.getOriginalFilename();

        //파일을 서버에 저장
        //File uploadFile = new File(rootPath + "/" + uniqueFileName);
        //originalFile.transferTo(uploadFile);


        return s3Service.uploadUoS3Bucket(originalFile.getBytes(), uniqueFileName);
    }


    // 게시물 저장
    public RulingApplyDetailResponseDTO createBoard(
            RulingApplyRequestDTO dto,
            TokenUserInfo userInfo,
            MultipartFile boardFile) throws IOException {
        User user = userRepository.findById(userInfo.getUserId()).orElseThrow();
        String boardImg = null;
        if (boardFile !=null){
            log.info(" file-name:{}",boardFile.getOriginalFilename());
            boardImg = uploadBoardImage(boardFile);
        }
        BoardApply boardApply = rulingApplyRepository.save(dto.toEntity(boardImg, user));
        return new RulingApplyDetailResponseDTO(boardApply);
    }

    // 글 삭제
    public void deleteBoard(TokenUserInfo userInfo, ApplyDeleteRequestDTO dto) {
        User user = userRepository.findById(userInfo.getUserId()).orElseThrow();
        if (dto.getIds() != null) {
            dto.getIds().stream()
                    .filter(boardId -> {
                        BoardApply boardApply = rulingApplyRepository.findById(boardId).orElseThrow();
                        return boardApply.getUserId().equals(user) || userInfo.getRole().equals(Role.ADMIN);
                    })
                    .forEach(rulingApplyRepository::deleteById);

        } else {
            BoardApply targetBoard = rulingApplyRepository.findById(dto.getId()).orElseThrow();
            if (userInfo.getRole().equals(Role.ADMIN) || targetBoard.getUserId().equals(user)) {
                rulingApplyRepository.delete(targetBoard);
            } else {
                throw new IllegalStateException("삭제 권한이 없습니다.");
            }
        }
    }


    // 게시물 개별조회
    public RulingApplyDetailResponseDTO detailBoard(Long applyId){
        BoardApply boardApply = rulingApplyRepository.findById(applyId).orElseThrow();
        int replyCount = applyReplyRepository.countByApplyId(boardApply);
        return new RulingApplyDetailResponseDTO(viewCountUp(boardApply),replyCount);
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

    //게시물 추천수 증가
    public void agrees(Long applyId){
        BoardApply targetBoard = rulingApplyRepository.findById(applyId).orElseThrow();
        targetBoard.setUpCount(targetBoard.getUpCount()+1);
        rulingApplyRepository.save(targetBoard);
    }
    @PostConstruct
    public void init() {
        startServerTime = LocalDateTime.now().withSecond(0).withNano(0);

    }
    // 기준일로 부터 3일 뒤 추천수 많은거 골라내서 board_ruling에 저장
    @Scheduled(initialDelay = 0, fixedDelay = 60 * 1000) // 3일(밀리초 단위)3 * 24 * 60 * 60 * 1000
    @Transactional
    public void selectionOfTopic() {
        log.info("스케줄링 실행중!!");
        // 현재 시간
        LocalDateTime now = LocalDateTime.now().withSecond(0).withNano(0);

        // 현재 시간으로부터 3일전
        LocalDateTime threeDaysAgo = now.minusDays(3);

        // 현재 시간으로부터 3일후
        threeDayafter = now.plusDays(3);
        log.info("now Time :{} ",now);
        if (now.isEqual(startServerTime))
            return;
        // 3일 동안 추천수가 가장 많은 게시물
        BoardApply BestBoard = rulingApplyRepository.findFirstByApplyDateBetweenOrderByUpCountDescViewCountDesc(threeDaysAgo, now);
        if (BestBoard == null)
            return;
        RulingBoardDetailResponseDTO rulingDto = new RulingBoardDetailResponseDTO(BestBoard);
        User user = userRepository.findById(rulingDto.getApplyPosterId()).orElseThrow();
        BoardRuling save = boardRulingRepository.save(rulingDto.toEntity(user));

//        //지원 게시글의 좋아요 정보들 다 삭제
//        rulingApplyRepository.findAll()
//                .forEach(board->{
//                    board.getVotes().clear();
//                });
        //지원 게시글은 다 지우기
        rulingApplyRepository.deleteAll();


    }
    public LocalDateTime threeDayAfter (){
        return threeDayafter;
    }

    // 게시물 검색
    public RulingApplyResponseDTO serchToBoard(ApplySearchRequestDTO dto, Pageable pageInfo,String order){
        Pageable pageable = PageRequest.of(pageInfo.getPageNumber() - 1, pageInfo.getPageSize());
        Page<BoardApply> boardApplyList;
        if (order == null)
            order = "";

        if (order.equals("like")){
            switch (dto.getType()){
                case "title":
                    boardApplyList = rulingApplyRepository.findByTitleContainingOrderByUpCountDesc(dto.getKeyword(),pageable);
                    break;
                case "writer":
                    boardApplyList = rulingApplyRepository.findBoardsByUserNameOrderByUpCountDesc(dto.getKeyword(), pageable);
                    break;
                case "mix":
                    boardApplyList = rulingApplyRepository.findByTitleContainingAndContentContainingOrderByUpCountDesc(dto.getKeyword(),dto.getKeyword(),pageable);
                    break;
                default:
                    boardApplyList = rulingApplyRepository.findAllByOrderByUpCountDesc(pageable);
            }
        }else{
            switch (dto.getType()){
                case "title":
                    boardApplyList = rulingApplyRepository.findByTitleContainingOrderByApplyDateDesc(dto.getKeyword(),pageable);
                    break;
                case "writer":
                    boardApplyList = rulingApplyRepository.findBoardsByUserNameOrderByApplyDateDesc(dto.getKeyword(), pageable);
                    break;
                case "mix":
                    boardApplyList = rulingApplyRepository.findByTitleContainingAndContentContainingOrderByApplyDateDesc(dto.getKeyword(),dto.getKeyword(),pageable);
                    break;
                default:
                    boardApplyList = rulingApplyRepository.findAllByOrderByApplyDateDesc(pageable);
            }
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
        User user = userRepository.findById(userInfo.getUserId()).orElseThrow();
        Page<BoardApply> boardApplyList = rulingApplyRepository.findByUserId(user, pageable);

        List<RulingApplyDetailResponseDTO> list = boardApplyList.stream()
                .map(RulingApplyDetailResponseDTO::new)
                .collect(Collectors.toList());

        return RulingApplyResponseDTO.builder()
                .boardApply(list)
                .totalPages(boardApplyList.getTotalPages())
                .totalCount(boardApplyList.getTotalElements())
                .build();
    }

    // 비디오 파일인지 체크
    public int CheckFile(MultipartFile boardFile) {
        MediaType mediaType = Videos.extractFileExtension(boardFile.getOriginalFilename());
        if (mediaType != null)
            return 1; //true - 비디오 파일임
        else
            return 0; //false -비디오 파일 아님
    }
}
