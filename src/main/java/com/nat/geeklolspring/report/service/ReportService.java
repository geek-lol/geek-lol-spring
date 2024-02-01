package com.nat.geeklolspring.report.service;

import com.nat.geeklolspring.auth.TokenUserInfo;
import com.nat.geeklolspring.entity.Report;
import com.nat.geeklolspring.report.dto.request.ReportPostRequestDTO;
import com.nat.geeklolspring.report.dto.response.ReportListResponseDTO;
import com.nat.geeklolspring.report.repository.ReportRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import static com.nat.geeklolspring.entity.Role.ADMIN;

@Service
@Slf4j
@RequiredArgsConstructor
@Transactional  // JPA 사용시 필수
public class ReportService {
    private final ReportRepository reportRepository;

    // Admin이면 실행
    public ReportListResponseDTO retrieveAdmin() {
        List<Report> reportsList = reportRepository.findAll();

        return ReportListResponseDTO
                .builder()
                .reportList(reportsList)
                .build();
    }

    public boolean checkPermission(TokenUserInfo userInfo) {
        return userInfo.getRole().equals(ADMIN);
    }

    // 일반유저면 실행
    public ReportListResponseDTO retrieve(TokenUserInfo userInfo) {
        List<Report> reportsList = reportRepository.findAllByUserId(userInfo.getUserId());

        return ReportListResponseDTO
                .builder()
                .reportList(reportsList)
                .build();
    }

    public ReportListResponseDTO insertReport(ReportPostRequestDTO dto, TokenUserInfo userInfo) {
        log.debug("리포트 등록 서비스 실행!");

        Report entity = dto.toEntity(userInfo);

        reportRepository.save(entity);

        return retrieve(userInfo);


    }
}
