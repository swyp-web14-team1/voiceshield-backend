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
}
