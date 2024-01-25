package com.nat.geeklolspring.shorts.service;

import com.nat.geeklolspring.entity.BoardShorts;
import com.nat.geeklolspring.repository.UserRepository;
import com.nat.geeklolspring.shorts.dto.request.ShortsPostRequestDTO;
import com.nat.geeklolspring.shorts.dto.response.ShortsDetailResponseDTO;
import com.nat.geeklolspring.shorts.dto.response.ShortsListResponseDTO;
import com.nat.geeklolspring.shorts.repository.ShortsRepository;
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

    public ShortsListResponseDTO insertVideo(ShortsPostRequestDTO dto, String videoPath) {
        log.debug("쇼츠 등록 서비스 실행!");

        BoardShorts shorts = dto.toEntity(videoPath);
        shortsRepository.save(shorts);

        return retrieve();
    }

    public ShortsListResponseDTO deleteShorts(Long id) {
        try {
            shortsRepository.deleteById(id);
        } catch (Exception e) {
            log.error("id가 존재하지 않아 삭제에 실패했습니다. - ID: {}, error: {}",
                    id, e.getMessage());
            throw new RuntimeException("삭제에 실패했습니다!!");
        }

        return retrieve();
    }

    public ShortsListResponseDTO retrieve() {
        List<BoardShorts> allShorts = shortsRepository.findAll();

        List<ShortsDetailResponseDTO> shortsList = allShorts.stream()
                .map(ShortsDetailResponseDTO::new)
                .collect(Collectors.toList());

        return ShortsListResponseDTO.builder()
                .shorts(shortsList)
                .build();
    }
}
