package com.swyp.voiceshield.casecatalog;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;

@Entity
@Table(name = "case_variant_quizzes")
public class CaseVariantQuiz {

    @Id
    @Column(name = "quiz_id")
    private String id;

    @OneToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "variant_id", nullable = false)
    private CaseVariant variant;

    @Column(name = "quiz_number", nullable = false)
    private int quizNumber;

    @Column(nullable = false, columnDefinition = "TEXT")
    private String question;

    @Column(nullable = false, columnDefinition = "TEXT")
    private String explanation;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "recommended_scenario_id")
    private CaseScenario recommendedScenario;

    protected CaseVariantQuiz() {
    }

    public String getId() {
        return id;
    }

    public int getQuizNumber() {
        return quizNumber;
    }

    public String getQuestion() {
        return question;
    }

    public String getExplanation() {
        return explanation;
    }

    public CaseScenario getRecommendedScenario() {
        return recommendedScenario;
    }
}
