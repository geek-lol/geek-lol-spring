package com.nat.geeklolspring.shorts.shortsboard.service;

import com.nat.geeklolspring.auth.TokenUserInfo;
import com.nat.geeklolspring.entity.BoardShorts;
import com.nat.geeklolspring.exception.NotEqualTokenException;
import com.nat.geeklolspring.shorts.shortsboard.dto.request.ShortsPostRequestDTO;
import com.nat.geeklolspring.shorts.shortsboard.dto.response.ShortsDetailResponseDTO;
import com.nat.geeklolspring.shorts.shortsboard.dto.response.ShortsListResponseDTO;
import com.nat.geeklolspring.shorts.shortsboard.repository.ShortsRepository;
import com.nat.geeklolspring.shorts.shortsreply.repository.ShortsReplyRepository;
import com.nat.geeklolspring.utils.token.TokenUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;
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

    public void deleteShorts(Long id) {
        BoardShorts shorts = shortsRepository.findById(id).orElseThrow();
        try {
            shortsRepository.deleteById(id);
        } catch (Exception e) {
            // 보통 해당 아이디 값이 없을 때 발생
            // 다만 다른 예외적인 오류가 있을 수 있으므로 취급주의
            log.error("삭제에 실패했습니다. - ID: {}, error: {}",
                    id, e.getMessage());
            throw new RuntimeException("해당 아이디 값을 가진 쇼츠가 없습니다!");
        }
    }

    public ShortsListResponseDTO retrieve() {

        // 페이징 처리 시 사용했던 것들
        // Pageable pageable = PageRequest.of(pageInfo.getPageNumber() - 1, pageInfo.getPageSize());
        // Page<BoardShorts> shortsList = shortsRepository.findAll(pageable);

        // shorts 전체 가져오기
        List<BoardShorts> shortsList = shortsRepository.findAll();

        // 만약 쇼츠가 5개가 넘지 않는다면 바로 리턴하기
        if(shortsList.size() < 5) {
            shortsList.forEach(list -> shortsRepository.upViewCount(list.getShortsId()));
            List<ShortsDetailResponseDTO> allShorts = shortsList.stream()
                    .map(ShortsDetailResponseDTO::new)
                    .collect(Collectors.toList());

            // shortsList를 정제해서 allShorts에 저장
            return ShortsListResponseDTO
                    .builder()
                    .shorts(allShorts)
                    .build();
        }

        // 랜덤으로 쇼츠를 가져오기 위한 함수들 선언
        Random rand = new Random();
        int count = 0;
        BoardShorts shorts;
        ArrayList<BoardShorts> randShortsList = new ArrayList<>();
        boolean flag;


        // 랜덤으로 중복되지 않는 쇼츠 5개 가져오기
        while (count < 5) {
            // flag 초기화
            flag = false;
            // 리스트 중에서 랜덤으로 숫자 하나를 뽑아서 shorts로 등록
            int randNum = rand.nextInt(shortsList.size());
            shorts = shortsList.get(randNum);
            for (BoardShorts boardShorts : randShortsList) {
                if (boardShorts.getShortsId().equals(shorts.getShortsId()))
                {
                    // 랜덤으로 가져온 값이 기존에 뽑은 값과 일치하므로 flag 값 변경
                    flag = true;
                    // 이미 값이 겹쳤기 때문에 더 볼 필요가 없으니 break로 빠져나오기
                    break;
                }
            }

            // flag가 true면 기존에 있던 값이므로 while문 재실행
            // false면 기존에 없던 값이므로 추가하고 count값 증가
            if(flag)
                continue;
            else {
                // 쇼츠 조회수 처리는 잘 모르겠으니까 그냥 불러오게 되면 전부 +1되도록 코드 작성
                shortsRepository.plusUpCount(shorts.getShortsId());
                randShortsList.add(shorts);
                count++;
            }
        }

        List<ShortsDetailResponseDTO> allShorts = randShortsList.stream()
                .map(ShortsDetailResponseDTO::new)
                .collect(Collectors.toList());

        // shortsList를 정제해서 allShorts에 저장
        return ShortsListResponseDTO
                .builder()
                .shorts(allShorts)
                .build();
    }
}
