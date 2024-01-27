package com.nat.geeklolspring.troll.service;

import com.nat.geeklolspring.entity.RulingReply;
import com.nat.geeklolspring.troll.repository.RulingReplyRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@Slf4j
@RequiredArgsConstructor
public class RulingReplyService {
    private final RulingReplyRepository rrr;
    //댓글 저장
    public boolean rulingReplySave(RulingReply rulingReply){
        if (rulingReply == null){
            throw new RuntimeException("댓글이 없습니다.");
        }
        try {
            rrr.save(rulingReply);
            return true;
        }catch (Exception e){
            log.warn(e.getMessage());
            return false;
        }
    }

    //댓글 전체 조회
    public List<RulingReply> rulingReplyAll(){
         return rrr.findAll();
    }

    //댓글 삭제
    public List<RulingReply> rulingReplyDelete(Long id){
        rrr.deleteById(id);
        return rulingReplyAll();
    }
}
