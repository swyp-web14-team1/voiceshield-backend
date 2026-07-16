package com.swyp.voiceshield.casecatalog;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
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
}
