package com.swyp.voiceshield.casecatalog;

import com.swyp.voiceshield.exception.ApiException;
import com.swyp.voiceshield.exception.ErrorCode;
import java.util.Arrays;
import java.util.Collection;
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
    public CaseSimulationStepResponse getSimulationStep(String scenarioId, String channel) {
        CaseScenario scenario = findScenario(scenarioId);
        CaseVariant variant = findVariant(scenario, channel);
        List<String> scriptLines = splitScriptLines(variant);

        return CaseSimulationStepResponse.from(scenario, variant, scriptLines);
    }

    @Transactional(readOnly = true)
    public CaseQuizStepResponse getQuizStep(String scenarioId, String channel) {
        CaseScenario scenario = findScenario(scenarioId);
        CaseVariant variant = findVariant(scenario, channel);

        ensureQuizExists(variant);
        return CaseQuizStepResponse.from(scenario, variant);
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
        CaseOptionKind kind = singleKindOf(selectedOptions);
        // 퀴즈 채점은 기존대로 퀴즈가 없으면 실패한다. 행동 선택지는 퀴즈에 종속되지 않는다.
        CaseVariantQuiz quiz = kind == CaseOptionKind.QUIZ ? ensureQuizExists(variant) : null;
        List<CaseVariantOption> correctOptions = findCorrectOptions(variant, kind, selectedOptions);
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

        // 조회 범위는 퀴즈 보기 + 행동 선택지 전체다. 채점 범위는 아래에서 종류별로 좁힌다.
        Map<String, CaseVariantOption> optionsById = variant.getAllOptions().stream()
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

    /**
     * 제출된 선택지의 종류. 퀴즈 보기와 행동 선택지는 채점 기준이 달라 한 요청에 섞을 수 없다.
     */
    private CaseOptionKind singleKindOf(List<CaseVariantOption> selectedOptions) {
        Set<CaseOptionKind> kinds = selectedOptions.stream()
                .map(CaseVariantOption::getOptionKind)
                .collect(Collectors.toSet());
        if (kinds.size() > 1) {
            throw new ApiException(ErrorCode.INVALID_INPUT_VALUE);
        }
        return kinds.iterator().next();
    }

    /** 제출된 행동 선택지의 회차. 정본 message 3건은 분기가 2회이고, 회차를 섞어 낼 수 없다. */
    private int singleStepOf(List<CaseVariantOption> selectedOptions) {
        Set<Integer> stepNumbers = selectedOptions.stream()
                .map(CaseVariantOption::getStepNumber)
                .collect(Collectors.toSet());
        if (stepNumbers.size() > 1) {
            throw new ApiException(ErrorCode.INVALID_INPUT_VALUE);
        }
        return stepNumbers.iterator().next();
    }

    private List<CaseVariantOption> findCorrectOptions(
            CaseVariant variant,
            CaseOptionKind kind,
            List<CaseVariantOption> selectedOptions
    ) {
        Collection<CaseVariantOption> candidates = kind == CaseOptionKind.ACTION
                ? variant.getActionChoices(singleStepOf(selectedOptions))
                : variant.getOptions();

        List<CaseVariantOption> correctOptions = candidates.stream()
                .filter(CaseVariantOption::isCorrect)
                .sorted(Comparator.comparingInt(CaseVariantOption::getStepNumber)
                        .thenComparingInt(CaseVariantOption::getOptionNumber))
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
