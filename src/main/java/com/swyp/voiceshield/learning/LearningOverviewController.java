package com.swyp.voiceshield.learning;

import com.swyp.voiceshield.common.response.ApiResponse;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/learning")
public class LearningOverviewController {

    private final LearningOverviewService learningOverviewService;

    public LearningOverviewController(LearningOverviewService learningOverviewService) {
        this.learningOverviewService = learningOverviewService;
    }

    @GetMapping("/overview")
    public ApiResponse<LearningOverviewResponse> getOverview(
            @RequestHeader(value = "X-User-Id", required = false) String userId
    ) {
        return ApiResponse.success(learningOverviewService.getOverview(userId));
    }
}
