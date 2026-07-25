package com.swyp.voiceshield.casecatalog;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;

@Entity
@Table(name = "case_variant_options")
public class CaseVariantOption {

    @Id
    @Column(name = "option_id")
    private String id;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "variant_id", nullable = false)
    private CaseVariant variant;

    @Enumerated(EnumType.STRING)
    @Column(name = "option_kind", nullable = false, length = 20)
    private CaseOptionKind optionKind;

    @Column(name = "step_number", nullable = false)
    private int stepNumber;

    @Column(name = "option_number", nullable = false)
    private int optionNumber;

    @Column(name = "option_text", nullable = false, columnDefinition = "TEXT")
    private String optionText;

    @Column(name = "is_correct", nullable = false)
    private boolean correct;

    protected CaseVariantOption() {
    }

    public String getId() {
        return id;
    }

    public CaseOptionKind getOptionKind() {
        return optionKind;
    }

    public int getStepNumber() {
        return stepNumber;
    }

    public int getOptionNumber() {
        return optionNumber;
    }

    public String getOptionText() {
        return optionText;
    }

    public boolean isCorrect() {
        return correct;
    }
}
