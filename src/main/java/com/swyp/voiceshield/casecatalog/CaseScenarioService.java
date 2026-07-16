package com.swyp.voiceshield.casecatalog;

import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

@Service
public class CaseScenarioService {

    private final CaseScenarioRepository caseScenarioRepository;

    public CaseScenarioService(CaseScenarioRepository caseScenarioRepository) {
        this.caseScenarioRepository = caseScenarioRepository;
    }

    public CaseScenarioResponse getScenario(String scenarioId) {
        CaseScenario scenario = caseScenarioRepository.findWithCategoryAndVariantsById(scenarioId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Case scenario not found"));
        return CaseScenarioResponse.from(scenario);
    }
}
