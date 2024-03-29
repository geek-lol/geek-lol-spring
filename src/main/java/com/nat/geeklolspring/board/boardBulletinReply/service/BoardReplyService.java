package com.nat.geeklolspring.board.boardBulletinReply.service;

import com.nat.geeklolspring.auth.TokenUserInfo;
import com.nat.geeklolspring.board.boardBulletinReply.dto.request.BoardPostRequestDTO;
import com.nat.geeklolspring.board.boardBulletinReply.dto.request.BoardUpdateRequestDTO;
import com.nat.geeklolspring.board.boardBulletinReply.dto.response.BoardMyReplyResponseDTO;
import com.nat.geeklolspring.board.boardBulletinReply.dto.response.BoardReplyListResponseDTO;
import com.nat.geeklolspring.board.boardBulletinReply.dto.response.BoardReplyResponseDTO;
import com.nat.geeklolspring.board.boardBulletinReply.repository.BoardReplyRepository;
import com.nat.geeklolspring.board.bulletin.repository.BoardBulletinRepository;
import com.nat.geeklolspring.entity.BoardBulletin;
import com.nat.geeklolspring.entity.BoardReply;
import com.nat.geeklolspring.entity.BoardRuling;
import com.nat.geeklolspring.entity.User;
import com.nat.geeklolspring.exception.BadRequestException;
import com.nat.geeklolspring.exception.NotEqualTokenException;
import com.nat.geeklolspring.troll.ruling.dto.response.RulingReplyResponseDTO;
import com.nat.geeklolspring.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import static com.nat.geeklolspring.utils.token.TokenUtil.EqualsId;

@Service
@Slf4j
@RequiredArgsConstructor
public class BoardReplyService {
    private final BoardReplyRepository boardReplyRepository;
    private final BoardBulletinRepository boardBulletinRepository;
    private final UserRepository userRepository;
    // 댓글 정보들을 돌려주는 서비스

    public BoardReplyListResponseDTO retrieve(Long bulletinId, Pageable pageInfo) {
        log.warn("retreieve 페이징처리 실행! Id: {}, PageInfo: {}", bulletinId, pageInfo);
        
        // 페이징 처리 시 첫번째 페이지는 0으로 시작하니 전달받은 페이지번호 - 1을 페이징 정보로 저장
        Pageable pageable = PageRequest.of(pageInfo.getPageNumber() - 1, pageInfo.getPageSize());
        BoardBulletin bulletin = boardBulletinRepository.findById(bulletinId).orElseThrow();
        // shortsId로 가져온 해당 쇼츠의 댓글 페이징 처리 정보를 저장
        Page<BoardReply> replyList = boardReplyRepository.findAllByBulletinOrderByBoardReplyDate(bulletin, pageable);

        // 정보를 가공하여 List<DTO>형태로 저장
        List<BoardReplyResponseDTO> allReply = replyList.stream()
                .map(BoardReplyResponseDTO::new)
                .collect(Collectors.toList());

        if(pageInfo.getPageNumber() > 1 && allReply.isEmpty()) {
            // 페이징 처리된 페이지의 최대값보다 높게 요청시 에러 발생시키기
            throw new BadRequestException("비정상적인 접근입니다!");
        } else {
            return BoardReplyListResponseDTO
                    .builder()
                    .reply(allReply)
                    .totalPages(replyList.getTotalPages())
                    .totalCount(replyList.getTotalElements())
                    .build();
        }
    }

    @Transactional
    // 쇼츠 댓글 저장 서비스
    public BoardReplyListResponseDTO insertBoardReply(
            Long id,
            BoardPostRequestDTO dto,
            TokenUserInfo userInfo,
            Pageable pageInfo) {
        log.debug("쇼츠 댓글 저장 서비스 실행!");
        User user = userRepository.findById(userInfo.getUserId()).orElseThrow();
        BoardBulletin boardBulletin = boardBulletinRepository.findById(id).orElseThrow();
        // dto에 담겨 있던 내용을 Reply 형식으로 변환해 reply에 저장
        BoardReply reply = dto.toEntity(boardBulletin, user);

        // DB에 저장
        boardReplyRepository.save(reply);

        // 새 댓글이 DB에 저장된 댓글 리스트를 리턴
        return retrieve(id, pageInfo);
    }

    @Transactional
    // 쇼츠 댓글 삭제 서비스
    public void deleteBoardReply(Long replyId, TokenUserInfo userInfo) {
        // 전달받은 댓글Id의 모든 정보를 가져오기
        BoardReply reply = boardReplyRepository.findById(replyId).orElseThrow();
        User user = userRepository.findById(userInfo.getUserId()).orElseThrow();
        try {
            boolean flag = reply.getWriterUser().equals(user);
            // 토큰의 id와 댓글의 작성자 id가 같으면 실행
            if(flag)
                // 삭제하지 못하면 Exception 발생
                 boardReplyRepository.deleteById(replyId);
            else
                throw new NotEqualTokenException("댓글 작성자만 삭제할 수 있습니다!");

        } catch (Exception e) {
            log.error("삭제에 실패했습니다. - ID: {}, Error: {}", replyId, e.getMessage());
            throw new RuntimeException("해당 아이디를 가진 댓글이 없습니다!");
        }
    }
    // 내 댓글 조회
    public BoardReplyListResponseDTO findMyReply(Pageable pageInfo, TokenUserInfo userInfo){
        Pageable pageable = PageRequest.of(pageInfo.getPageNumber() - 1, pageInfo.getPageSize());
        User user = userRepository.findById(userInfo.getUserId()).orElseThrow();
        Page<BoardReply> replyList = boardReplyRepository.findAllByWriterUser(user, pageable);
        // 정보를 가공하여 List<DTO>형태로 저장
        List<BoardMyReplyResponseDTO> allReply = replyList.stream()
                .map(reply->{
                    BoardMyReplyResponseDTO dto = new BoardMyReplyResponseDTO(reply);
                    BoardBulletin boardBulletin = boardBulletinRepository.findById(dto.getBoardId()).orElseThrow();
                    dto.setTitle(boardBulletin.getTitle());
                    return dto;
                })
                .collect(Collectors.toList());

        if(pageInfo.getPageNumber() > 1 && allReply.isEmpty()) {
            // 페이징 처리된 페이지의 최대값보다 높게 요청시 에러 발생시키기
            throw new BadRequestException("비정상적인 접근입니다!");
        } else {
            return BoardReplyListResponseDTO
                    .builder()
                    .myReply(allReply)
                    .totalPages(replyList.getTotalPages())
                    .build();
        }
    }
}
