package com.swyp.voiceshield.casecatalog;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Entity
@Table(name = "case_variants")
public class CaseVariant {

    @Id
    @Column(name = "variant_id")
    private String id;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "scenario_id", nullable = false)
    private CaseScenario scenario;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 20)
    private CaseChannel channel;

    @Column(columnDefinition = "TEXT")
    private String content;

    @OneToMany(mappedBy = "variant", fetch = FetchType.LAZY)
    private Set<CaseVariantOption> options = new LinkedHashSet<>();

    @OneToMany(mappedBy = "variant", fetch = FetchType.LAZY)
    private List<CaseVariantQuiz> quizzes = new ArrayList<>();

    protected CaseVariant() {
    }

    public String getId() {
        return id;
    }

    public CaseChannel getChannel() {
        return channel;
    }

    public String getContent() {
        return content;
    }

    /**
     * 퀴즈 보기만 반환한다. 퀴즈 채점·조회 경로가 쓰는 기존 계약이다.
     *
     * <p>V14 부터 같은 테이블에 행동 선택지({@link CaseOptionKind#ACTION})가 함께 저장되므로
     * 여기서 걸러내지 않으면 채점 대상에 행동 선택지가 섞인다.
     */
    public Set<CaseVariantOption> getOptions() {
        return options.stream()
                .filter(option -> option.getOptionKind() == CaseOptionKind.QUIZ)
                .collect(Collectors.toCollection(LinkedHashSet::new));
    }

    /** 정본 '🎮 선택지' — 시뮬레이션 진행 중 사용자가 고르는 행동. 회차·번호 순. */
    public List<CaseVariantOption> getActionChoices() {
        return options.stream()
                .filter(option -> option.getOptionKind() == CaseOptionKind.ACTION)
                .sorted(Comparator.comparingInt(CaseVariantOption::getStepNumber)
                        .thenComparingInt(CaseVariantOption::getOptionNumber))
                .toList();
    }

    public CaseVariantQuiz getQuiz() {
        return quizzes.stream()
                .min(Comparator.comparingInt(CaseVariantQuiz::getQuizNumber))
                .orElse(null);
    }
}
