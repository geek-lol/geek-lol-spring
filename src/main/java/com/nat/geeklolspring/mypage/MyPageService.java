package com.nat.geeklolspring.mypage;

import com.nat.geeklolspring.auth.TokenUserInfo;
import com.nat.geeklolspring.board.boardBulletinReply.repository.BoardReplyRepository;
import com.nat.geeklolspring.board.bulletin.repository.BoardBulletinRepository;
import com.nat.geeklolspring.entity.User;
import com.nat.geeklolspring.shorts.shortsboard.repository.ShortsRepository;
import com.nat.geeklolspring.shorts.shortsreply.repository.ShortsReplyRepository;
import com.nat.geeklolspring.troll.apply.repository.ApplyReplyRepository;
import com.nat.geeklolspring.troll.apply.repository.RulingApplyRepository;
import com.nat.geeklolspring.troll.ruling.repository.BoardRulingRepository;
import com.nat.geeklolspring.troll.ruling.repository.RulingReplyRepository;
import com.nat.geeklolspring.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;

@Service
@Slf4j
@RequiredArgsConstructor
@Transactional
public class MyPageService {
    private final BoardBulletinRepository boardBulletinRepository;
    private final BoardReplyRepository boardReplyRepository;
    private final ShortsRepository shortsRepository;
    private final ShortsReplyRepository shortsReplyRepository;
    private final RulingApplyRepository rulingApplyRepository;
    private final ApplyReplyRepository applyReplyRepository;
    private final BoardRulingRepository boardRulingRepository;
    private final RulingReplyRepository rulingReplyRepository;
    private final UserRepository userRepository;

    // 데이터 모아서 보내주기
    public MyPageResponseDTO showCount(TokenUserInfo userInfo){
        String id = userInfo.getUserId();
        User user = userRepository.findById(userInfo.getUserId()).orElseThrow();
        int b = countBoard(id,user);
        int r = countReply(id,user);

        return MyPageResponseDTO.builder()
                .boardCount(b)
                .replyCount(r)
                .build();
    }
    //내 게시글 갯수 세기
    public int countBoard(String id,User user){
        try {
            Integer board = boardBulletinRepository.countByPosterId(id);
            Integer ruling = boardRulingRepository.countByRulingPosterId(id);
            Integer apply = rulingApplyRepository.countByUserId(user);
            Integer shorts = shortsRepository.countByUploaderId(id);
            log.warn("board,ruling,apply,shorts:{},{},{},{}",board,ruling,apply,shorts);

            // Null checks
            board = (board != null) ? board : 0;
            ruling = (ruling != null) ? ruling : 0;
            apply = (apply != null) ? apply : 0;
            shorts = (shorts != null) ? shorts : 0;

            return board+ruling+apply+shorts;
        }catch (NullPointerException e){
            log.warn("뭐가 null이게~,{}",e.getMessage());
            throw new RuntimeException("조회가 안된거같아유");
        }
    }
    //내 댓글 갯수 세기
    public int countReply(String id,User user){
        Integer board = boardReplyRepository.countByReplyWriterId(id);
        Integer ruling = rulingReplyRepository.countByWriterId(id);
        Integer apply = applyReplyRepository.countByUserId(user);
        Integer shorts = shortsReplyRepository.countByWriterId(id);

        return board+ruling+apply+shorts;
    }
}
