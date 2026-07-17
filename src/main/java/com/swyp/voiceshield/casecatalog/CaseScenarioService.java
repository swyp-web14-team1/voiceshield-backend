package com.swyp.voiceshield.casecatalog;

import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.Arrays;
import java.util.List;

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

    public CaseScenarioStepResponse getScenarioStep(String scenarioId, String channel) {
        CaseScenario scenario = findScenario(scenarioId);
        CaseVariant variant = findVariant(scenario, channel);
        List<String> scriptLines = Arrays.stream(variant.getContent().split("\\R"))
                .filter(line -> !line.isBlank())
                .toList();

        return CaseScenarioStepResponse.from(scenario, variant, scriptLines);
    }

    public CaseChoiceEvaluationResponse evaluateChoice(
            String scenarioId,
            String channel,
            CaseChoiceEvaluationRequest request
    ) {
        CaseScenario scenario = findScenario(scenarioId);
        CaseVariant variant = findVariant(scenario, channel);
        CaseVariantOption selectedOption = variant.getOptions().stream()
                .filter(option -> option.getId().equals(request.choiceOptionId()))
                .findFirst()
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Choice option not found"));

        return CaseChoiceEvaluationResponse.from(selectedOption);
    }

    private CaseScenario findScenario(String scenarioId) {
        return caseScenarioRepository.findWithCategoryAndVariantsById(scenarioId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Case scenario not found"));
    }

    private CaseVariant findVariant(CaseScenario scenario, String channel) {
        CaseChannel caseChannel = parseChannel(channel);
        return scenario.getVariants().stream()
                .filter(variant -> variant.getChannel() == caseChannel)
                .findFirst()
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Case variant not found"));
    }

    private CaseChannel parseChannel(String channel) {
        try {
            return CaseChannel.valueOf(channel.toUpperCase());
        } catch (IllegalArgumentException exception) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Unsupported case channel");
        }
    }
}
