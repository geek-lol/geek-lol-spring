package com.nat.geeklolspring.board.bulletin.service;

import com.nat.geeklolspring.auth.TokenUserInfo;
import com.nat.geeklolspring.aws.S3Service;
import com.nat.geeklolspring.board.boardBulletinReply.repository.BoardReplyRepository;
import com.nat.geeklolspring.board.bulletin.dto.request.BoardBulletinModifyRequestDTO;
import com.nat.geeklolspring.board.bulletin.dto.request.BoardBulletinWriteRequestDTO;
import com.nat.geeklolspring.board.bulletin.dto.response.BoardBulletinDeleteResponseDTO;
import com.nat.geeklolspring.board.bulletin.dto.response.BoardBulletinDetailResponseDTO;
import com.nat.geeklolspring.board.bulletin.dto.response.BoardBulletinResponseDTO;
import com.nat.geeklolspring.board.bulletin.repository.BoardBulletinRepository;
import com.nat.geeklolspring.entity.BoardBulletin;
import com.nat.geeklolspring.entity.BoardReply;
import com.nat.geeklolspring.entity.BulletinCheck;
import com.nat.geeklolspring.entity.User;
import com.nat.geeklolspring.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

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
    private final UserRepository userRepository;
    private final BoardReplyRepository boardReplyRepository;

    private final S3Service s3Service;
    // 목록 불러오기
    public BoardBulletinResponseDTO retrieve() {

        List<BoardBulletin> boardBulletinList = boardBulletinRepository.findAll();

        List<BoardBulletinDetailResponseDTO> list = boardBulletinList.stream()
                .map(board->{
                    int replyCount = boardReplyRepository.countByBulletin(board);
                    return new BoardBulletinDetailResponseDTO(board,replyCount);
                })
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
        User user = userRepository.findById(userInfo.getUserId()).orElseThrow();
        BoardBulletin boardBulletin = boardBulletinRepository.save(dto.toEntity(fileUrl,user));

        BoardBulletin save = boardBulletinRepository.save(boardBulletin);

        log.info("{}",userInfo);


        return new BoardBulletinDetailResponseDTO(save);

    }

    // 글 삭제

    public BoardBulletinResponseDTO delete(TokenUserInfo userInfo, BoardBulletinDeleteResponseDTO dto) {
        if (!Objects.equals(dto.getPosterId(), userInfo.getUserId())) {
            log.warn("삭제할 권한이 없습니다!! - {}", dto.getPosterId());
            throw new RuntimeException("삭제 권한이 없습니다");
        }

        log.info("dto : {}",dto.getBulletinId());
        log.info("userInfo : {}",userInfo);

//        boardBulletinRepository.deleteByBoardBulletinIdWithJPQL(dto.getBulletinId());

        boardBulletinRepository.deleteById(dto.getBulletinId());

        return retrieve();
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
                boardBulletinList = boardBulletinRepository.findByUserContainingOrderByBoardDateDesc(posterKeyword,pageable);
            else if(contentKeyword != null)
                boardBulletinList = boardBulletinRepository.findByBoardContentContainingOrderByBoardDateDesc(contentKeyword,pageable);
            else
                boardBulletinList = boardBulletinRepository.findAllByOrderByBoardDateDesc(pageable);
        }else {
            if(titleKeyword != null)
                boardBulletinList = boardBulletinRepository.findByTitleContainingOrderByUpCountDesc(titleKeyword, pageable);
            else if(posterKeyword != null)
                boardBulletinList = boardBulletinRepository.findByUserContainingOrderByUpCountDesc(posterKeyword,pageable);
            else if(contentKeyword != null)
                boardBulletinList = boardBulletinRepository.findByBoardContentContainingOrderByUpCountDesc(contentKeyword,pageable);
            else
                boardBulletinList = boardBulletinRepository.findAllByOrderByUpCountDesc(pageable);
        }


        List<BoardBulletinDetailResponseDTO> list = boardBulletinList.stream()
                .map(board->{
                    int replyCount = boardReplyRepository.countByBulletin(board);
                    return new BoardBulletinDetailResponseDTO(board,replyCount);
                })
                .collect(Collectors.toList());

        return BoardBulletinResponseDTO.builder()
                .board(list)
                .totalCount(boardBulletinList.getTotalElements())
                .totalPages(boardBulletinList.getTotalPages())
                .build();

    }

    public BoardBulletinDetailResponseDTO detailRetrieve(String bulletinId) {

        BoardBulletin boardBulletin = boardBulletinRepository.findById(Long.valueOf(bulletinId)).get();
        log.info("replise :{} ", boardBulletin.getReplies());
        log.info("{}",boardBulletin);

        boardBulletin.setViewCount(boardBulletin.getViewCount()+1);

        boardBulletinRepository.save(boardBulletin);

        return BoardBulletinDetailResponseDTO.builder()
                .posterName(boardBulletin.getUser().getUserName())
                .bulletinId(boardBulletin.getBulletinId())
                .title(boardBulletin.getTitle())
                .posterId(boardBulletin.getUser().getId())
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
        dto.setPosterName(boardBulletin.get().getUser().getUserName());
        dto.setViewCount(boardBulletin.get().getViewCount());
        dto.setUpCount(boardBulletin.get().getUpCount());

        List<BoardReply> replies = boardBulletin.get().getReplies();
        List<BulletinCheck> votes = boardBulletin.get().getVotes();
        log.info("dto : {}",dto);

        BoardBulletin saveData = boardBulletinRepository.save(dto.toEntity(filePath,boardBulletin.get().getUser(),replies,votes));

        return new BoardBulletinDetailResponseDTO(saveData);

    }

    //내가 쓴 글 조회 하기
    public BoardBulletinResponseDTO findByMyBullentin(Pageable pageInfo, TokenUserInfo userInfo){
        Pageable pageable = PageRequest.of(pageInfo.getPageNumber() - 1, pageInfo.getPageSize());
        User user = userRepository.findById(userInfo.getUserId()).orElseThrow();
        Page<BoardBulletin> boardBulletinList = boardBulletinRepository.findAllByUser(user, pageable);

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
        //File rootDir = new File(rootPath);
        //if (!rootDir.exists()) rootDir.mkdirs();

        // 파일명을 유니크하게 변경
        String uniqueFileName = "Board_" + UUID.randomUUID() + "_" + originalFile.getOriginalFilename();

        // 파일을 서버에 저장
        //File uploadFile = new File(rootPath + "/" + uniqueFileName);
        //originalFile.transferTo(uploadFile);

        return s3Service.uploadUoS3Bucket(originalFile.getBytes(), uniqueFileName);
    }

    public String getImagePath(Long id){

        //DB에서 파일명 조회
        BoardBulletin boardBulletin = boardBulletinRepository.findById(id).orElseThrow();

        return boardBulletin.getBoardMedia();

    }




}

