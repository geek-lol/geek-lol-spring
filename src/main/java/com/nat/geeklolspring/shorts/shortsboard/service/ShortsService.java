package com.nat.geeklolspring.shorts.shortsboard.service;

import com.nat.geeklolspring.auth.TokenUserInfo;
import com.nat.geeklolspring.entity.BoardShorts;
import com.nat.geeklolspring.exception.BadRequestException;
import com.nat.geeklolspring.exception.NotEqualTokenException;
import com.nat.geeklolspring.shorts.shortsboard.dto.request.ShortsPostRequestDTO;
import com.nat.geeklolspring.shorts.shortsboard.dto.response.ShortsDetailResponseDTO;
import com.nat.geeklolspring.shorts.shortsboard.dto.response.ShortsListResponseDTO;
import com.nat.geeklolspring.shorts.shortsboard.repository.ShortsRepository;
import com.nat.geeklolspring.shorts.shortsreply.dto.response.ShortsReplyListResponseDTO;
import com.nat.geeklolspring.utils.token.TokenUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
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

    public void insertVideo(ShortsPostRequestDTO dto, String videoPath, TokenUserInfo userInfo) {
        log.debug("쇼츠 등록 서비스 실행!");

        // DB에 저장될 형식에 맞게 엔티티화
        BoardShorts shorts = dto.toEntity(videoPath, userInfo);
        // DB에 저장
        shortsRepository.save(shorts);
    }

    public void deleteShorts(Long id, TokenUserInfo userInfo) {
        BoardShorts shorts = shortsRepository.findById(id).orElseThrow();

        boolean flag = TokenUtil.EqualsId(shorts.getUploaderId(), userInfo);
        try {
            if(flag) {
                // id값에 해당하는 동영상 삭제
                shortsRepository.deleteById(id);
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

    public ShortsListResponseDTO retrievePaging(Pageable pageInfo) {

        Pageable pageable = PageRequest.of(pageInfo.getPageNumber() - 1, pageInfo.getPageSize());
        // DB에서 모든 쇼츠 영상을 찾아 shortsList에 저장
        Page<BoardShorts> shortsList = shortsRepository.findAll(pageable);

        List<ShortsDetailResponseDTO> allShorts = shortsList.stream()
                .map(ShortsDetailResponseDTO::new)
                .collect(Collectors.toList());

        // shortsList를 정제해서 allShorts에 저장
        if (pageInfo.getPageNumber() > 1 && shortsList.isEmpty()) {
            // 페이징 처리된 페이지의 최대값보다 높게 요청시 에러 발생시키기
            throw new BadRequestException("비정상적인 접근입니다!");
        } else {
            return ShortsListResponseDTO
                    .builder()
                    .shorts(allShorts)
                    .totalPages(shortsList.getTotalPages())
                    .totalCount(shortsList.getTotalElements())
                    .build();
        }
    }
}
