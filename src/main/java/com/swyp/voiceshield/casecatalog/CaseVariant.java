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
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;

import java.util.LinkedHashSet;
import java.util.Set;

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

    @OneToOne(mappedBy = "variant", fetch = FetchType.LAZY)
    private CaseVariantQuiz quiz;

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

    public Set<CaseVariantOption> getOptions() {
        return options;
    }

    public CaseVariantQuiz getQuiz() {
        return quiz;
    }
}
