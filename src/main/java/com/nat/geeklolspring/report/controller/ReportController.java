package com.nat.geeklolspring.report.controller;

import com.nat.geeklolspring.auth.TokenUserInfo;
import com.nat.geeklolspring.report.dto.request.ReportPostRequestDTO;
import com.nat.geeklolspring.report.dto.response.ReportListResponseDTO;
import com.nat.geeklolspring.report.service.ReportService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

@RestController
@Slf4j
@RequiredArgsConstructor
//@CrossOrigin(origins = {"http://localhost:3000","",""})
@RequestMapping("/api/report")
public class ReportController {
    private final ReportService reportService;

    // 신고목록 보여주기
    @GetMapping()
    public ResponseEntity<?> reportList(@AuthenticationPrincipal TokenUserInfo userInfo) {
        log.info("/api/report : Get!");

        // 권한 확인 : 권한이 있으면 True, 없으면 False
        boolean flag = reportService.checkPermission(userInfo);

        try {
            if(flag) {
                // 권한이 있음(Admin)
                // 모든 신고 정보를 가져옴
                ReportListResponseDTO reportList = reportService.retrieveAdmin();

                if (reportList.getReportList().isEmpty()) {
                    return ResponseEntity
                            .badRequest()
                            .body(ReportListResponseDTO
                                    .builder()
                                    .error("아직 신고가 없습니다.")
                                    .build());
                }

                return ResponseEntity.ok().body(reportList);
            } else {
                // 권한이 없음 (Common)
                // 본인의 신고 정보만 가져옴
                ReportListResponseDTO reportList = reportService.retriever(userInfo);

                if (reportList.getReportList().isEmpty()) {
                    return ResponseEntity
                            .badRequest()
                            .body(ReportListResponseDTO
                                    .builder()
                                    .error("아직 신고하지 않으셨습니다.")
                                    .build());
                }

                return ResponseEntity.ok().body(reportList);
            }
        } catch (Exception e) {
            log.warn(e.getMessage());
            return ResponseEntity.internalServerError().body(e.getMessage());
        }
    }

    @PostMapping()
    public ResponseEntity<?> addReport(
            @Validated @RequestBody ReportPostRequestDTO dto,
            @AuthenticationPrincipal TokenUserInfo userInfo,
            BindingResult result
    ) {

        // 입력값 검증에 걸리면 400번 코드와 함께 메시지를 클라이언트에 전송
        if(result.hasErrors()) {
            return ResponseEntity
                    .badRequest()
                    .body(result.toString())
                    ;
        }

        log.info("/api/report : Post!");
        log.warn("request parameter : {}", dto);

        try {
            ReportListResponseDTO reportList = reportService.insertReport(dto, userInfo);

            return ResponseEntity.ok().body(reportList);
        } catch (Exception e) {
            log.warn(e.getMessage());
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

}
