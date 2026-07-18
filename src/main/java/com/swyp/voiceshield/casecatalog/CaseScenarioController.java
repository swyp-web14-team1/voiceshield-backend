package com.swyp.voiceshield.casecatalog;

import com.swyp.voiceshield.common.response.ApiResponse;
import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/cases")
public class CaseScenarioController {

    private final CaseScenarioService caseScenarioService;

    public CaseScenarioController(CaseScenarioService caseScenarioService) {
        this.caseScenarioService = caseScenarioService;
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

    @PostMapping("/{scenarioId}/variants/{channel}/choices")
    public ApiResponse<CaseChoiceEvaluationResponse> evaluateChoice(
            @PathVariable String scenarioId,
            @PathVariable String channel,
            @Valid @RequestBody CaseChoiceEvaluationRequest request
    ) {
        return ApiResponse.success(caseScenarioService.evaluateChoice(scenarioId, channel, request));
    }
}
