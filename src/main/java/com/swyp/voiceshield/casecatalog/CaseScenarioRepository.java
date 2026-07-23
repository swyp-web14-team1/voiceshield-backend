package com.swyp.voiceshield.casecatalog;

import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface CaseScenarioRepository extends JpaRepository<CaseScenario, String> {

    @EntityGraph(attributePaths = {"category", "variants", "variants.options", "variants.quizzes", "variants.quizzes.recommendedScenario"})
    Optional<CaseScenario> findWithCategoryAndVariantsById(String id);
}
