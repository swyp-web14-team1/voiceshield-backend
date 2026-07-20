package com.swyp.voiceshield.learning;

import com.swyp.voiceshield.common.response.ApiResponse;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/learning/scenarios")
public class LearningCompletionController {

    private final LearningCompletionService learningCompletionService;

    public LearningCompletionController(LearningCompletionService learningCompletionService) {
        this.learningCompletionService = learningCompletionService;
    }

    @PostMapping("/{scenarioId}/simulation-complete")
    public ApiResponse<LearningCompletionResponse> markSimulationComplete(
            @RequestHeader(value = "X-User-Id", required = false) String userId,
            @PathVariable String scenarioId
    ) {
        return ApiResponse.success(learningCompletionService.markSimulationComplete(userId, scenarioId));
    }

    @PostMapping("/{scenarioId}/quiz-complete")
    public ApiResponse<LearningCompletionResponse> markQuizComplete(
            @RequestHeader(value = "X-User-Id", required = false) String userId,
            @PathVariable String scenarioId
    ) {
        return ApiResponse.success(learningCompletionService.markQuizComplete(userId, scenarioId));
    }
}
