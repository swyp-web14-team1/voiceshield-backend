package com.swyp.voiceshield.casecatalog;

import com.swyp.voiceshield.category.Category;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;

import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "case_scenarios")
public class CaseScenario {

    @Id
    @Column(name = "scenario_id")
    private String id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "category_id")
    private Category category;

    @Column(name = "case_name", nullable = false)
    private String name;

    private String difficulty;

    @Column(name = "estimated_learning_time")
    private String estimatedLearningTime;

    @Column(name = "completion_rate")
    private String completionRate;

    @OneToMany(mappedBy = "scenario", fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    private List<CaseVariant> variants = new ArrayList<>();

    protected CaseScenario() {
    }

    public String getId() {
        return id;
    }

    public Category getCategory() {
        return category;
    }

    public String getName() {
        return name;
    }

    public String getDifficulty() {
        return difficulty;
    }

    public String getEstimatedLearningTime() {
        return estimatedLearningTime;
    }

    public String getCompletionRate() {
        return completionRate;
    }

    public List<CaseVariant> getVariants() {
        return variants;
    }
}
