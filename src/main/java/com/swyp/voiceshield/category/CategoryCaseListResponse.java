package com.swyp.voiceshield.category;

import com.swyp.voiceshield.casecatalog.CaseScenario;
import java.util.List;

public record CategoryCaseListResponse(
        String categoryId,
        String categoryName,
        List<CategoryCaseSummaryResponse> cases
) {

    public static CategoryCaseListResponse from(Category category, List<CaseScenario> scenarios) {
        return new CategoryCaseListResponse(
                category.getId(),
                category.getName(),
                scenarios.stream()
                        .map(CategoryCaseSummaryResponse::from)
                        .toList()
        );
    }
}
