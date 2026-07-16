package com.swyp.voiceshield.casecatalog;

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
    public CaseScenarioResponse getScenario(@PathVariable String scenarioId) {
        return caseScenarioService.getScenario(scenarioId);
    }

    @GetMapping("/{scenarioId}/variants/{channel}/scenario-step")
    public CaseScenarioStepResponse getScenarioStep(
            @PathVariable String scenarioId,
            @PathVariable String channel
    ) {
        return caseScenarioService.getScenarioStep(scenarioId, channel);
    }

    @PostMapping("/{scenarioId}/variants/{channel}/choices")
    public CaseChoiceEvaluationResponse evaluateChoice(
            @PathVariable String scenarioId,
            @PathVariable String channel,
            @Valid @RequestBody CaseChoiceEvaluationRequest request
    ) {
        return caseScenarioService.evaluateChoice(scenarioId, channel, request);
    }
}
