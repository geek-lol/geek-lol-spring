package com.nat.geeklolspring.shorts.shortsboard.service;

import com.nat.geeklolspring.auth.TokenUserInfo;
import com.nat.geeklolspring.entity.BoardShorts;
import com.nat.geeklolspring.exception.NotEqualTokenException;
import com.nat.geeklolspring.shorts.shortsboard.dto.request.ShortsPostRequestDTO;
import com.nat.geeklolspring.shorts.shortsboard.dto.response.ShortsDetailResponseDTO;
import com.nat.geeklolspring.shorts.shortsboard.dto.response.ShortsListResponseDTO;
import com.nat.geeklolspring.shorts.shortsboard.repository.ShortsRepository;
import com.nat.geeklolspring.utils.token.TokenUtil;
import jdk.jshell.execution.Util;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@Slf4j
@RequiredArgsConstructor
@Transactional  // JPA 사용시 필수
public class ShortsService {
    private final ShortsRepository shortsRepository;

    public ShortsListResponseDTO insertVideo(ShortsPostRequestDTO dto, String videoPath, String thumbnailPath, TokenUserInfo userInfo) {
        log.debug("쇼츠 등록 서비스 실행!");

        // DB에 저장될 형식에 맞게 엔티티화
        BoardShorts shorts = dto.toEntity(videoPath, thumbnailPath, userInfo);
        // DB에 저장
        shortsRepository.save(shorts);

        // 갱신된 DB의 동영상 리스트를 리턴
        return retrieve();
    }

    public ShortsListResponseDTO deleteShorts(Long id, TokenUserInfo userInfo) {
        BoardShorts shorts = shortsRepository.findById(id).orElseThrow();

        boolean flag = TokenUtil.EqualsId(shorts.getUploaderId(), userInfo);
        try {
            if(flag) {
                // id값에 해당하는 동영상 삭제
                shortsRepository.deleteById(id);
                return retrieve();
            } else throw new NotEqualTokenException("업로드 한 유저만 삭제할 수 있습니다!");
        } catch (Exception e) {
            // 보통 해당 아이디 값이 없을 때 발생
            // 다만 다른 예외적인 오류가 있을 수 있으므로 취급주의
            log.error("삭제에 실패했습니다. - ID: {}, error: {}",
                    id, e.getMessage());
            throw new RuntimeException("해당 아이디 값을 가진 쇼츠가 없습니다!");
        }
    }

    public ShortsListResponseDTO retrieve() {
        // DB에서 모든 쇼츠 영상을 찾아 shortsList에 저장
        List<BoardShorts> shortsList = shortsRepository.findAll();

        // shortsList를 정제해서 allShorts에 저장
        List<ShortsDetailResponseDTO> allShorts = shortsList.stream()
                .map(ShortsDetailResponseDTO::new)
                .collect(Collectors.toList());

        return ShortsListResponseDTO.builder()
                .shorts(allShorts)
                .build();
    }
}
