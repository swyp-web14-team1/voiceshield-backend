package com.swyp.voiceshield.casecatalog;

import com.swyp.voiceshield.exception.ApiException;
import com.swyp.voiceshield.exception.ErrorCode;
import java.util.Arrays;
import java.util.Comparator;
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
        List<String> scriptLines = splitScriptLines(variant);

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
        CaseVariantQuiz quiz = ensureQuizExists(variant);
        CaseVariantOption correctOption = findResultCorrectOption(variant, selectedOption);

        return CaseChoiceEvaluationResponse.from(quiz, selectedOption, correctOption);
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

    private CaseVariantQuiz ensureQuizExists(CaseVariant variant) {
        CaseVariantQuiz quiz = variant.getQuiz();
        if (quiz == null) {
            throw new ApiException(ErrorCode.CASE_VARIANT_NOT_FOUND);
        }
        return quiz;
    }

    private List<String> splitScriptLines(CaseVariant variant) {
        String content = variant.getContent();
        if (content == null || content.isBlank()) {
            return List.of();
        }

        return Arrays.stream(content.split("\\R"))
                .filter(line -> !line.isBlank())
                .toList();
    }

    private CaseVariantOption findResultCorrectOption(CaseVariant variant, CaseVariantOption selectedOption) {
        if (selectedOption.isCorrect()) {
            return selectedOption;
        }

        return variant.getOptions().stream()
                .filter(CaseVariantOption::isCorrect)
                .min(Comparator.comparingInt(CaseVariantOption::getOptionNumber))
                .orElseThrow(() -> new ApiException(ErrorCode.CASE_CHOICE_OPTION_NOT_FOUND));
    }
}
