package com.swyp.voiceshield;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.webmvc.test.autoconfigure.AutoConfigureMockMvc;
import org.springframework.test.web.servlet.MockMvc;

import static org.hamcrest.Matchers.containsString;
import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.nullValue;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
class CaseCatalogApiTest {

    @Autowired
    private MockMvc mockMvc;

    @Test
    void returnsMessageAndVoiceVariantsForScenario() throws Exception {
        mockMvc.perform(get("/api/v1/cases/case-return-delivery"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.success").value(true))
                .andExpect(jsonPath("$.data.scenarioId").value("case-return-delivery"))
                .andExpect(jsonPath("$.data.caseName").value("반품 택배"))
                .andExpect(jsonPath("$.data.variants.message.channel").value("MESSAGE"))
                .andExpect(jsonPath("$.data.variants.voice.channel").value("VOICE"));
    }

    @Test
    void keepsMobileRepairAsItsOwnCategory() throws Exception {
        mockMvc.perform(get("/api/v1/cases/case-mobile-repair"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.success").value(true))
                .andExpect(jsonPath("$.data.caseName").value("휴대폰 고장"))
                .andExpect(jsonPath("$.data.categoryId").doesNotExist())
                .andExpect(jsonPath("$.data.categoryName").doesNotExist());
    }

    @Test
    void returnsVoiceScriptAndQuizOptionsForMobileRepair() throws Exception {
        mockMvc.perform(get("/api/v1/cases/case-mobile-repair"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.success").value(true))
                .andExpect(jsonPath("$.data.variants.voice.content").value(containsString("휴대폰이 고장 나서")))
                .andExpect(jsonPath("$.data.variants.voice.options", hasSize(4)))
                .andExpect(jsonPath("$.data.variants.voice.options[1].optionNumber").value(2))
                .andExpect(jsonPath("$.data.variants.voice.options[1].isCorrect").value(false))
                .andExpect(jsonPath("$.data.variants.voice.options[2].isCorrect").value(true))
                .andExpect(jsonPath("$.data.variants.voice.options[3].isCorrect").value(false));
    }

    @Test
    void returnsVoiceScenarioStepWithScriptLinesAndChoices() throws Exception {
        mockMvc.perform(get("/api/v1/cases/case-mobile-repair/variants/voice/scenario-step"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.success").value(true))
                .andExpect(jsonPath("$.data.scenarioId").value("case-mobile-repair"))
                .andExpect(jsonPath("$.data.variantId").value("case-mobile-repair-voice"))
                .andExpect(jsonPath("$.data.channel").value("VOICE"))
                .andExpect(jsonPath("$.data.quiz.quizId").value("case-mobile-repair-voice-quiz-1"))
                .andExpect(jsonPath("$.data.quiz.quizNumber").value(1))
                .andExpect(jsonPath("$.data.quiz.question").value("다음 중 사기임을 가장 강하게 의심할 수 있는 단서는 무엇일까요?"))
                .andExpect(jsonPath("$.data.scriptLines", hasSize(23)))
                .andExpect(jsonPath("$.data.scriptLines[0]").value("[전화벨]"))
                .andExpect(jsonPath("$.data.scriptLines[1]").value("나"))
                .andExpect(jsonPath("$.data.scriptLines[2]").value("\"여보세요?\""))
                .andExpect(jsonPath("$.data.choices", hasSize(4)))
                .andExpect(jsonPath("$.data.choices[0].optionText").value("휴대폰이 고장 나서 다른 번호로 연락했다고 말했다."))
                .andExpect(jsonPath("$.data.choices[1].optionText").value("병원에서 치료를 받고 있다고 말했다."))
                .andExpect(jsonPath("$.data.choices[2].optionText").value("병원비를 개인 계좌로 바로 송금해 달라고 했다."))
                .andExpect(jsonPath("$.data.choices[3].optionText").value("간호사가 기다리고 있다고 말했다."))
                .andExpect(jsonPath("$.data.choices[2].isCorrect").value(true));
    }

    @Test
    void returnsMessageScenarioStepWithQuizAndChoices() throws Exception {
        mockMvc.perform(get("/api/v1/cases/case-mobile-repair/variants/message/scenario-step"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.success").value(true))
                .andExpect(jsonPath("$.data.scenarioId").value("case-mobile-repair"))
                .andExpect(jsonPath("$.data.variantId").value("case-mobile-repair-message"))
                .andExpect(jsonPath("$.data.channel").value("MESSAGE"))
                .andExpect(jsonPath("$.data.quiz.quizId").value("case-mobile-repair-message-quiz-1"))
                .andExpect(jsonPath("$.data.quiz.quizNumber").value(1))
                .andExpect(jsonPath("$.data.quiz.question").value("다음 중 가족 사칭 메신저 피싱을 가장 강하게 의심해야 하는 상황은 무엇일까요?"))
                .andExpect(jsonPath("$.data.choices", hasSize(4)))
                .andExpect(jsonPath("$.data.choices[0].optionText").value("새로운 번호 또는 임시폰으로 연락했다."))
                .andExpect(jsonPath("$.data.choices[1].optionText").value("병원비를 개인 계좌로 바로 송금해 달라고 했다."))
                .andExpect(jsonPath("$.data.choices[2].optionText").value("기존 번호로는 연락하지 말라고 했다."))
                .andExpect(jsonPath("$.data.choices[3].optionText").value("다쳤다고 말했다."))
                .andExpect(jsonPath("$.data.choices[1].isCorrect").value(true));
    }

    @Test
    void evaluatesSelectedVoiceScenarioChoice() throws Exception {
        mockMvc.perform(post("/api/v1/cases/case-mobile-repair/variants/voice/choices")
                        .contentType("application/json")
                        .content("{\"choiceOptionId\":\"case-mobile-repair-voice-option-3\"}"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.success").value(true))
                .andExpect(jsonPath("$.data.choiceOptionId").value("case-mobile-repair-voice-option-3"))
                .andExpect(jsonPath("$.data.optionNumber").value(3))
                .andExpect(jsonPath("$.data.isCorrect").value(true))
                .andExpect(jsonPath("$.data.quiz.quizId").value("case-mobile-repair-voice-quiz-1"))
                .andExpect(jsonPath("$.data.quiz.question").value("다음 중 사기임을 가장 강하게 의심할 수 있는 단서는 무엇일까요?"))
                .andExpect(jsonPath("$.data.selectedOption.optionId").value("case-mobile-repair-voice-option-3"))
                .andExpect(jsonPath("$.data.selectedOption.optionText").value("병원비를 개인 계좌로 바로 송금해 달라고 했다."))
                .andExpect(jsonPath("$.data.correctOption.optionId").value("case-mobile-repair-voice-option-3"))
                .andExpect(jsonPath("$.data.explanation").value("휴대폰 고장이나 병원 치료는 실제로 발생할 수 있는 상황이며, 간호사가 기다리고 있다는 말도 긴급한 상황에서는 있을 수 있습니다. 하지만 전화로 개인 계좌에 즉시 송금을 요구하는 것은 가족 사칭 보이스피싱의 대표적인 수법입니다. 이런 전화를 받았다면 송금하기 전에 기존에 저장된 가족 연락처로 직접 확인하는 것이 가장 안전한 대응입니다."))
                .andExpect(jsonPath("$.data.recommendedLearning.scenarioId").value("case-return-delivery"))
                .andExpect(jsonPath("$.data.recommendedLearning.title").value("반품 택배"));
    }

    @Test
    void evaluatesSelectedMessageScenarioChoice() throws Exception {
        mockMvc.perform(post("/api/v1/cases/case-mobile-repair/variants/message/choices")
                        .contentType("application/json")
                        .content("{\"choiceOptionId\":\"case-mobile-repair-message-option-2\"}"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.success").value(true))
                .andExpect(jsonPath("$.data.choiceOptionId").value("case-mobile-repair-message-option-2"))
                .andExpect(jsonPath("$.data.optionNumber").value(2))
                .andExpect(jsonPath("$.data.isCorrect").value(true))
                .andExpect(jsonPath("$.data.quiz.quizId").value("case-mobile-repair-message-quiz-1"))
                .andExpect(jsonPath("$.data.quiz.question").value("다음 중 가족 사칭 메신저 피싱을 가장 강하게 의심해야 하는 상황은 무엇일까요?"))
                .andExpect(jsonPath("$.data.selectedOption.optionId").value("case-mobile-repair-message-option-2"))
                .andExpect(jsonPath("$.data.selectedOption.optionText").value("병원비를 개인 계좌로 바로 송금해 달라고 했다."))
                .andExpect(jsonPath("$.data.correctOption.optionId").value("case-mobile-repair-message-option-2"))
                .andExpect(jsonPath("$.data.explanation").value("새로운 번호로 연락하거나, 다쳤다고 말하는 것은 실제 상황에서도 발생할 수 있습니다. 하지만 개인 계좌로 즉시 송금을 요구하는 행동은 가족 사칭 메신저 피싱에서 자주 사용되는 대표적인 수법입니다. 송금을 하기 전에는 반드시 기존에 알고 있던 전화번호로 직접 연락하여 사실 여부를 확인하는 것이 중요합니다."));
    }

    @Test
    void returnsReturnDeliveryVoiceScenarioStepWithQuizAndChoices() throws Exception {
        mockMvc.perform(get("/api/v1/cases/case-return-delivery/variants/voice/scenario-step"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.success").value(true))
                .andExpect(jsonPath("$.data.scenarioId").value("case-return-delivery"))
                .andExpect(jsonPath("$.data.variantId").value("case-return-delivery-voice"))
                .andExpect(jsonPath("$.data.channel").value("VOICE"))
                .andExpect(jsonPath("$.data.quiz.quizId").value("case-return-delivery-voice-quiz-1"))
                .andExpect(jsonPath("$.data.quiz.quizNumber").value(1))
                .andExpect(jsonPath("$.data.quiz.question").value("다음 중 사기임을 판단할 수 있는 결정적인 단서를 모두 선택하세요."))
                .andExpect(jsonPath("$.data.choices", hasSize(3)))
                .andExpect(jsonPath("$.data.choices[0].optionText").value("택배 기사가 방문 전 전화한다."))
                .andExpect(jsonPath("$.data.choices[0].isCorrect").value(false))
                .andExpect(jsonPath("$.data.choices[1].optionText").value("개인 휴대전화 번호로 주소를 다시 요구한다."))
                .andExpect(jsonPath("$.data.choices[1].isCorrect").value(true))
                .andExpect(jsonPath("$.data.choices[2].optionText").value("문자 링크 접속 및 앱 설치를 요구한다."))
                .andExpect(jsonPath("$.data.choices[2].isCorrect").value(true));
    }

    @Test
    void evaluatesSelectedReturnDeliveryVoiceScenarioChoice() throws Exception {
        mockMvc.perform(post("/api/v1/cases/case-return-delivery/variants/voice/choices")
                        .contentType("application/json")
                        .content("{\"choiceOptionId\":\"case-return-delivery-voice-option-3\"}"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.success").value(true))
                .andExpect(jsonPath("$.data.choiceOptionId").value("case-return-delivery-voice-option-3"))
                .andExpect(jsonPath("$.data.optionNumber").value(3))
                .andExpect(jsonPath("$.data.isCorrect").value(true))
                .andExpect(jsonPath("$.data.quiz.quizId").value("case-return-delivery-voice-quiz-1"))
                .andExpect(jsonPath("$.data.selectedOption.optionText").value("문자 링크 접속 및 앱 설치를 요구한다."))
                .andExpect(jsonPath("$.data.correctOption.optionId").value("case-return-delivery-voice-option-3"))
                .andExpect(jsonPath("$.data.explanation").value("택배 기사가 방문 전에 연락하는 것은 일반적인 상황일 수 있습니다. 하지만 개인 번호를 이용해 개인정보를 다시 요구하거나, 문자 링크를 통한 주소 입력과 앱 설치를 요구하는 경우는 대표적인 택배 사칭 피싱 수법입니다."));
    }

    @Test
    void returnsReturnDeliveryMessageScenarioStepWithQuizAndChoices() throws Exception {
        mockMvc.perform(get("/api/v1/cases/case-return-delivery/variants/message/scenario-step"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.success").value(true))
                .andExpect(jsonPath("$.data.scenarioId").value("case-return-delivery"))
                .andExpect(jsonPath("$.data.variantId").value("case-return-delivery-message"))
                .andExpect(jsonPath("$.data.channel").value("MESSAGE"))
                .andExpect(jsonPath("$.data.quiz.quizId").value("case-return-delivery-message-quiz-1"))
                .andExpect(jsonPath("$.data.quiz.quizNumber").value(1))
                .andExpect(jsonPath("$.data.quiz.question").value("다음 중 사기 문자의 결정적인 단서를 모두 선택하세요."))
                .andExpect(jsonPath("$.data.choices", hasSize(4)))
                .andExpect(jsonPath("$.data.choices[0].optionText").value("반품 신청 후 문자가 왔다."))
                .andExpect(jsonPath("$.data.choices[0].isCorrect").value(false))
                .andExpect(jsonPath("$.data.choices[1].optionText").value("공식 홈페이지가 아닌 단축 URL 링크를 보냈다."))
                .andExpect(jsonPath("$.data.choices[1].isCorrect").value(true))
                .andExpect(jsonPath("$.data.choices[2].optionText").value("개인정보를 다시 입력하도록 요구했다."))
                .andExpect(jsonPath("$.data.choices[2].isCorrect").value(true))
                .andExpect(jsonPath("$.data.choices[3].optionText").value("택배 관련 문자가 왔다."))
                .andExpect(jsonPath("$.data.choices[3].isCorrect").value(false));
    }

    @Test
    void evaluatesSelectedReturnDeliveryMessageScenarioChoice() throws Exception {
        mockMvc.perform(post("/api/v1/cases/case-return-delivery/variants/message/choices")
                        .contentType("application/json")
                        .content("{\"choiceOptionId\":\"case-return-delivery-message-option-3\"}"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.success").value(true))
                .andExpect(jsonPath("$.data.choiceOptionId").value("case-return-delivery-message-option-3"))
                .andExpect(jsonPath("$.data.optionNumber").value(3))
                .andExpect(jsonPath("$.data.isCorrect").value(true))
                .andExpect(jsonPath("$.data.quiz.quizId").value("case-return-delivery-message-quiz-1"))
                .andExpect(jsonPath("$.data.selectedOption.optionText").value("개인정보를 다시 입력하도록 요구했다."))
                .andExpect(jsonPath("$.data.correctOption.optionId").value("case-return-delivery-message-option-3"))
                .andExpect(jsonPath("$.data.explanation").value("반품 신청 이후 문자가 오는 것 자체는 정상적인 상황일 수 있습니다. 하지만 출처가 불분명한 링크를 보내거나 개인정보를 다시 입력하도록 유도하는 경우는 대표적인 택배 피싱 수법입니다."));
    }

    @Test
    void returnsFireAgencyMessageScenarioStepWithQuizAndChoices() throws Exception {
        mockMvc.perform(get("/api/v1/cases/case-fire-agency/variants/message/scenario-step"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.success").value(true))
                .andExpect(jsonPath("$.data.scenarioId").value("case-fire-agency"))
                .andExpect(jsonPath("$.data.variantId").value("case-fire-agency-message"))
                .andExpect(jsonPath("$.data.channel").value("MESSAGE"))
                .andExpect(jsonPath("$.data.quiz.quizId").value("case-fire-agency-message-quiz-1"))
                .andExpect(jsonPath("$.data.quiz.quizNumber").value(1))
                .andExpect(jsonPath("$.data.quiz.question").value("다음 중 사기임을 의심할 수 있는 결정적인 단서를 선택하세요."))
                .andExpect(jsonPath("$.data.choices", hasSize(4)))
                .andExpect(jsonPath("$.data.choices[0].optionText").value("최근 소방시설법이 개정되었다고 안내했다."))
                .andExpect(jsonPath("$.data.choices[0].isCorrect").value(false))
                .andExpect(jsonPath("$.data.choices[1].optionText").value("소방 안전점검이 예정되어 있다고 말했다."))
                .andExpect(jsonPath("$.data.choices[1].isCorrect").value(false))
                .andExpect(jsonPath("$.data.choices[2].optionText").value("특정 업체에서만 장비를 구매하라고 안내했다."))
                .andExpect(jsonPath("$.data.choices[2].isCorrect").value(true))
                .andExpect(jsonPath("$.data.choices[3].optionText").value("정부 지원금을 받을 수 있다고 설명했다."))
                .andExpect(jsonPath("$.data.choices[3].isCorrect").value(false));
    }

    @Test
    void evaluatesSelectedFireAgencyMessageScenarioChoice() throws Exception {
        mockMvc.perform(post("/api/v1/cases/case-fire-agency/variants/message/choices")
                        .contentType("application/json")
                        .content("{\"choiceOptionId\":\"case-fire-agency-message-option-3\"}"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.success").value(true))
                .andExpect(jsonPath("$.data.choiceOptionId").value("case-fire-agency-message-option-3"))
                .andExpect(jsonPath("$.data.optionNumber").value(3))
                .andExpect(jsonPath("$.data.isCorrect").value(true))
                .andExpect(jsonPath("$.data.quiz.quizId").value("case-fire-agency-message-quiz-1"))
                .andExpect(jsonPath("$.data.selectedOption.optionText").value("특정 업체에서만 장비를 구매하라고 안내했다."))
                .andExpect(jsonPath("$.data.correctOption.optionId").value("case-fire-agency-message-option-3"))
                .andExpect(jsonPath("$.data.explanation").value("법령 개정, 안전점검, 정부 지원사업은 실제로 있을 수 있는 내용입니다. 하지만 공공기관이 특정 업체를 지정하여 구매를 유도하거나 결제를 요구하는 것은 매우 의심해야 할 신호입니다. 이런 안내를 받았다면 문자나 전화에 있는 번호를 이용하지 말고, 관할 시청이나 소방서의 공식 대표번호로 직접 확인해야 합니다."));
    }

    @Test
    void returnsSpecialInvestmentMessageScenarioStepWithQuizAndChoices() throws Exception {
        mockMvc.perform(get("/api/v1/cases/case-special-investment/variants/message/scenario-step"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.success").value(true))
                .andExpect(jsonPath("$.data.scenarioId").value("case-special-investment"))
                .andExpect(jsonPath("$.data.variantId").value("case-special-investment-message"))
                .andExpect(jsonPath("$.data.channel").value("MESSAGE"))
                .andExpect(jsonPath("$.data.quiz.quizId").value("case-special-investment-message-quiz-1"))
                .andExpect(jsonPath("$.data.quiz.quizNumber").value(1))
                .andExpect(jsonPath("$.data.quiz.question").value("다음 중 투자사기임을 판단할 수 있는 가장 결정적인 단서는 무엇일까요?"))
                .andExpect(jsonPath("$.data.choices", hasSize(4)))
                .andExpect(jsonPath("$.data.choices[0].optionText").value("AI 반도체 투자 상품이라고 소개했다."))
                .andExpect(jsonPath("$.data.choices[0].isCorrect").value(false))
                .andExpect(jsonPath("$.data.choices[1].optionText").value("오늘까지만 가입할 수 있다고 안내했다."))
                .andExpect(jsonPath("$.data.choices[1].isCorrect").value(false))
                .andExpect(jsonPath("$.data.choices[2].optionText").value("원금 보장과 월 20% 수익을 약속했다."))
                .andExpect(jsonPath("$.data.choices[2].isCorrect").value(true))
                .andExpect(jsonPath("$.data.choices[3].optionText").value("VIP 고객 대상으로 안내했다고 말했다."))
                .andExpect(jsonPath("$.data.choices[3].isCorrect").value(false));
    }

    @Test
    void evaluatesSelectedSpecialInvestmentMessageScenarioChoice() throws Exception {
        mockMvc.perform(post("/api/v1/cases/case-special-investment/variants/message/choices")
                        .contentType("application/json")
                        .content("{\"choiceOptionId\":\"case-special-investment-message-option-3\"}"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.success").value(true))
                .andExpect(jsonPath("$.data.choiceOptionId").value("case-special-investment-message-option-3"))
                .andExpect(jsonPath("$.data.optionNumber").value(3))
                .andExpect(jsonPath("$.data.isCorrect").value(true))
                .andExpect(jsonPath("$.data.quiz.quizId").value("case-special-investment-message-quiz-1"))
                .andExpect(jsonPath("$.data.selectedOption.optionText").value("원금 보장과 월 20% 수익을 약속했다."))
                .andExpect(jsonPath("$.data.correctOption.optionId").value("case-special-investment-message-option-3"))
                .andExpect(jsonPath("$.data.explanation").value("AI 관련 투자 상품이나 VIP 대상 이벤트는 실제 금융상품에서도 있을 수 있습니다. 하지만 원금을 보장하면서 높은 수익을 동시에 약속하는 것은 대표적인 투자사기의 특징입니다. 또한 문자에 포함된 링크는 가짜 투자 사이트로 연결될 수 있으므로 절대 누르지 말고, 해당 금융회사가 실제 등록된 업체인지 공식 홈페이지나 금융당국을 통해 확인해야 합니다."));
    }

    @Test
    void returnsScenarioStepWithoutQuizWhenVariantHasNoSeededQuizYet() throws Exception {
        mockMvc.perform(get("/api/v1/cases/case-fire-agency/variants/voice/scenario-step"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.success").value(true))
                .andExpect(jsonPath("$.data.scenarioId").value("case-fire-agency"))
                .andExpect(jsonPath("$.data.variantId").value("case-fire-agency-voice"))
                .andExpect(jsonPath("$.data.channel").value("VOICE"))
                .andExpect(jsonPath("$.data.quiz").value(nullValue()))
                .andExpect(jsonPath("$.data.scriptLines", hasSize(0)))
                .andExpect(jsonPath("$.data.choices", hasSize(0)));
    }

    @Test
    void returnsNotFoundForUnknownApi() throws Exception {
        mockMvc.perform(get("/api/v1/unknown"))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.success").value(false))
                .andExpect(jsonPath("$.error.code").value("COMMON-002"))
                .andExpect(jsonPath("$.error.message").value("요청한 API를 찾을 수 없습니다."));
    }

    @Test
    void returnsMethodNotAllowedForUnsupportedHttpMethod() throws Exception {
        mockMvc.perform(post("/api/v1/categories"))
                .andExpect(status().isMethodNotAllowed())
                .andExpect(jsonPath("$.success").value(false))
                .andExpect(jsonPath("$.error.code").value("COMMON-004"))
                .andExpect(jsonPath("$.error.message").value("지원하지 않는 HTTP 메서드입니다."));
    }
}
