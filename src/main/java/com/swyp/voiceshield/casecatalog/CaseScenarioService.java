package com.swyp.voiceshield.casecatalog;

import com.swyp.voiceshield.exception.ApiException;
import com.swyp.voiceshield.exception.ErrorCode;
import java.util.Arrays;
import java.util.List;
import org.springframework.stereotype.Service;

@Service
public class CaseScenarioService {

    private final CaseScenarioRepository caseScenarioRepository;

    public CaseScenarioService(CaseScenarioRepository caseScenarioRepository) {
        this.caseScenarioRepository = caseScenarioRepository;
    }

    public CaseScenarioResponse getScenario(String scenarioId) {
        CaseScenario scenario = caseScenarioRepository.findWithCategoryAndVariantsById(scenarioId)
                .orElseThrow(() -> new ApiException(ErrorCode.CASE_SCENARIO_NOT_FOUND));
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
                .orElseThrow(() -> new ApiException(ErrorCode.CASE_CHOICE_OPTION_NOT_FOUND));

        return CaseChoiceEvaluationResponse.from(selectedOption);
    }

    private CaseScenario findScenario(String scenarioId) {
        return caseScenarioRepository.findWithCategoryAndVariantsById(scenarioId)
                .orElseThrow(() -> new ApiException(ErrorCode.CASE_SCENARIO_NOT_FOUND));
    }

    private CaseVariant findVariant(CaseScenario scenario, String channel) {
        CaseChannel caseChannel = parseChannel(channel);
        return scenario.getVariants().stream()
                .filter(variant -> variant.getChannel() == caseChannel)
                .findFirst()
                .orElseThrow(() -> new ApiException(ErrorCode.CASE_VARIANT_NOT_FOUND));
    }

    private CaseChannel parseChannel(String channel) {
        try {
            return CaseChannel.valueOf(channel.toUpperCase());
        } catch (IllegalArgumentException exception) {
            throw new ApiException(ErrorCode.CASE_CHANNEL_NOT_SUPPORTED);
        }
    }
}
