package com.swyp.voiceshield.reportguide;

import com.swyp.voiceshield.common.response.ApiResponse;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/report-guide")
public class ReportGuideController {

    private final ReportGuideService reportGuideService;

    public ReportGuideController(ReportGuideService reportGuideService) {
        this.reportGuideService = reportGuideService;
    }

    @GetMapping
    public ApiResponse<ReportGuideResponse> getReportGuide() {
        return ApiResponse.success(reportGuideService.getReportGuide());
    }
}
