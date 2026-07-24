package com.swyp.voiceshield.casecatalog;

import com.swyp.voiceshield.exception.ApiException;
import com.swyp.voiceshield.exception.ErrorCode;
import java.util.Arrays;
import java.util.Comparator;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.function.Function;
import java.util.stream.Collectors;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class CaseScenarioService {

    private final CaseScenarioRepository caseScenarioRepository;

    public CaseScenarioService(CaseScenarioRepository caseScenarioRepository) {
        this.caseScenarioRepository = caseScenarioRepository;
    }

    @Transactional(readOnly = true)
    public CaseScenarioResponse getScenario(String scenarioId) {
        CaseScenario scenario = caseScenarioRepository.findWithCategoryAndVariantsById(scenarioId)
                .orElseThrow(() -> new ApiException(ErrorCode.CASE_SCENARIO_NOT_FOUND));
        return CaseScenarioResponse.from(scenario);
    }

    @Transactional(readOnly = true)
    public CaseScenarioStepResponse getScenarioStep(String scenarioId, String channel) {
        CaseScenario scenario = findScenario(scenarioId);
        CaseVariant variant = findVariant(scenario, channel);
        List<String> scriptLines = splitScriptLines(variant);

        return CaseScenarioStepResponse.from(scenario, variant, scriptLines);
    }

    @Transactional(readOnly = true)
    public CaseChoiceEvaluationResponse evaluateChoice(
            String scenarioId,
            String channel,
            CaseChoiceEvaluationRequest request
    ) {
        CaseScenario scenario = findScenario(scenarioId);
        CaseVariant variant = findVariant(scenario, channel);
        List<CaseVariantOption> selectedOptions = findSelectedOptions(variant, request.selectedChoiceOptionIds());
        CaseVariantQuiz quiz = ensureQuizExists(variant);
        List<CaseVariantOption> correctOptions = findCorrectOptions(variant);
        boolean correct = selectedOptionIds(selectedOptions).equals(selectedOptionIds(correctOptions));

        return CaseChoiceEvaluationResponse.from(quiz, selectedOptions, correctOptions, correct);
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

    private List<CaseVariantOption> findSelectedOptions(CaseVariant variant, List<String> choiceOptionIds) {
        if (choiceOptionIds.isEmpty()) {
            throw new ApiException(ErrorCode.INVALID_INPUT_VALUE);
        }

        Map<String, CaseVariantOption> optionsById = variant.getOptions().stream()
                .collect(Collectors.toMap(CaseVariantOption::getId, Function.identity()));
        return new LinkedHashSet<>(choiceOptionIds).stream()
                .map(optionId -> {
                    CaseVariantOption option = optionsById.get(optionId);
                    if (option == null) {
                        throw new ApiException(ErrorCode.CASE_CHOICE_OPTION_NOT_FOUND);
                    }
                    return option;
                })
                .toList();
    }

    private List<CaseVariantOption> findCorrectOptions(CaseVariant variant) {
        List<CaseVariantOption> correctOptions = variant.getOptions().stream()
                .filter(CaseVariantOption::isCorrect)
                .sorted(Comparator.comparingInt(CaseVariantOption::getOptionNumber))
                .toList();
        if (correctOptions.isEmpty()) {
            throw new ApiException(ErrorCode.CASE_CHOICE_OPTION_NOT_FOUND);
        }
        return correctOptions;
    }

    private Set<String> selectedOptionIds(List<CaseVariantOption> options) {
        return options.stream()
                .map(CaseVariantOption::getId)
                .collect(Collectors.toSet());
    }
}
