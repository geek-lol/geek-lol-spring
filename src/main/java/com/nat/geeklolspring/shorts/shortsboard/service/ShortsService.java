package com.nat.geeklolspring.shorts.shortsboard.service;

import com.nat.geeklolspring.entity.BoardShorts;
import com.nat.geeklolspring.exception.IdNotFoundException;
import com.nat.geeklolspring.shorts.shortsboard.dto.request.ShortsPostRequestDTO;
import com.nat.geeklolspring.shorts.shortsboard.dto.response.ShortsDetailResponseDTO;
import com.nat.geeklolspring.shorts.shortsboard.dto.response.ShortsListResponseDTO;
import com.nat.geeklolspring.shorts.shortsboard.repository.ShortsRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityNotFoundException;
import javax.swing.text.html.parser.Entity;
import java.util.List;
import java.util.stream.Collectors;

@Service
@Slf4j
@RequiredArgsConstructor
@Transactional  // JPA 사용시 필수
public class ShortsService {
    private final ShortsRepository shortsRepository;

    public ShortsListResponseDTO insertVideo(ShortsPostRequestDTO dto, String videoPath, String thumbnailPath) {
        log.debug("쇼츠 등록 서비스 실행!");

        BoardShorts shorts = dto.toEntity(videoPath, thumbnailPath);
        shortsRepository.save(shorts);

        return retrieve();
    }

    public ShortsListResponseDTO deleteShorts(Long id) {
        try {
            shortsRepository.deleteById(id);
            return retrieve();
        } catch (Exception e) {
            log.error("삭제에 실패했습니다. - ID: {}, error: {}",
                    id, e.getMessage());
            throw new RuntimeException("해당 아이디 값을 가진 쇼츠가 없습니다!");
        }
    }

    public ShortsListResponseDTO retrieve() {
        List<BoardShorts> shortsList = shortsRepository.findAll();

        List<ShortsDetailResponseDTO> allShorts = shortsList.stream()
                .map(ShortsDetailResponseDTO::new)
                .collect(Collectors.toList());

        return ShortsListResponseDTO.builder()
                .shorts(allShorts)
                .build();
    }
}
