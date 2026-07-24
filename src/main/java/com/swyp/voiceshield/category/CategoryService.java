package com.swyp.voiceshield.category;

import com.swyp.voiceshield.casecatalog.CaseScenario;
import com.swyp.voiceshield.casecatalog.CaseScenarioRepository;
import com.swyp.voiceshield.exception.ApiException;
import com.swyp.voiceshield.exception.ErrorCode;
import java.util.List;
import org.springframework.stereotype.Service;

@Service
public class CategoryService {

    private final CategoryRepository categoryRepository;
    private final CaseScenarioRepository caseScenarioRepository;

    public CategoryService(CategoryRepository categoryRepository, CaseScenarioRepository caseScenarioRepository) {
        this.categoryRepository = categoryRepository;
        this.caseScenarioRepository = caseScenarioRepository;
    }

    public List<CategoryResponse> getCategories() {
        return categoryRepository.findAll().stream()
                .map(CategoryResponse::from)
                .toList();
    }

    public CategoryCaseListResponse getCasesByCategory(String categoryId) {
        Category category = categoryRepository.findById(categoryId)
                .orElseThrow(() -> new ApiException(ErrorCode.CATEGORY_NOT_FOUND));
        List<CaseScenario> scenarios = caseScenarioRepository.findAllByCategory_IdOrderByNameAsc(categoryId);
        return CategoryCaseListResponse.from(category, scenarios);
    }
}
