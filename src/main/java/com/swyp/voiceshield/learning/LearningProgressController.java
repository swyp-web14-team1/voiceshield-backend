package com.swyp.voiceshield.learning;

import com.swyp.voiceshield.common.response.ApiResponse;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/learning")
public class LearningProgressController {

    private final LearningProgressService learningProgressService;

    public LearningProgressController(LearningProgressService learningProgressService) {
        this.learningProgressService = learningProgressService;
    }

    @GetMapping("/progress")
    public ApiResponse<LearningProgressResponse> getProgress(
            @RequestHeader(value = "X-User-Id", required = false) String userId
    ) {
        return ApiResponse.success(learningProgressService.getProgress(userId));
    }
}
