package com.swyp.voiceshield.casecatalog;

import com.swyp.voiceshield.common.response.ApiResponse;
import com.swyp.voiceshield.vulnerability.ChoiceLogService;
import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/cases")
public class CaseScenarioController {

    private final CaseScenarioService caseScenarioService;
    private final ChoiceLogService choiceLogService;

    public CaseScenarioController(CaseScenarioService caseScenarioService, ChoiceLogService choiceLogService) {
        this.caseScenarioService = caseScenarioService;
        this.choiceLogService = choiceLogService;
    }

    @GetMapping("/{scenarioId}")
    public ApiResponse<CaseScenarioResponse> getScenario(@PathVariable String scenarioId) {
        return ApiResponse.success(caseScenarioService.getScenario(scenarioId));
    }

    @GetMapping("/{scenarioId}/variants/{channel}/scenario-step")
    public ApiResponse<CaseScenarioStepResponse> getScenarioStep(
            @PathVariable String scenarioId,
            @PathVariable String channel
    ) {
        return ApiResponse.success(caseScenarioService.getScenarioStep(scenarioId, channel));
    }

    @GetMapping("/{scenarioId}/variants/{channel}/simulation-step")
    public ApiResponse<CaseSimulationStepResponse> getSimulationStep(
            @PathVariable String scenarioId,
            @PathVariable String channel
    ) {
        return ApiResponse.success(caseScenarioService.getSimulationStep(scenarioId, channel));
    }

    @GetMapping("/{scenarioId}/variants/{channel}/quiz-step")
    public ApiResponse<CaseQuizStepResponse> getQuizStep(
            @PathVariable String scenarioId,
            @PathVariable String channel
    ) {
        return ApiResponse.success(caseScenarioService.getQuizStep(scenarioId, channel));
    }

    /**
     * 선택지 채점.
     *
     * <p>채점 결과를 취약 유형 진단의 원자료로 남긴다. 적재는 채점과 별도 트랜잭션이며 실패해도
     * 응답에 영향을 주지 않는다. {@code X-User-Id} 는 프론트의 {@code apiFetch} 가 모든 요청에
     * 이미 붙이고 있어 이 엔드포인트만 안 받고 있었다.
     */
    @PostMapping("/{scenarioId}/variants/{channel}/choices")
    public ApiResponse<CaseChoiceEvaluationResponse> evaluateChoice(
            @RequestHeader(value = "X-User-Id", required = false) String userId,
            @PathVariable String scenarioId,
            @PathVariable String channel,
            @Valid @RequestBody CaseChoiceEvaluationRequest request
    ) {
        CaseChoiceEvaluationResponse response = caseScenarioService.evaluateChoice(scenarioId, channel, request);
        choiceLogService.record(userId, scenarioId, request.selectedChoiceOptionIds(), response.isCorrect());
        return ApiResponse.success(response);
    }
}
