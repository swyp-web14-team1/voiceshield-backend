package com.swyp.voiceshield.category;

import com.swyp.voiceshield.common.response.ApiResponse;
import java.util.List;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/categories")
public class CategoryController {

    private final CategoryService categoryService;

    public CategoryController(CategoryService categoryService) {
        this.categoryService = categoryService;
    }

    @GetMapping
    public ApiResponse<List<CategoryResponse>> getCategories() {
        return ApiResponse.success(categoryService.getCategories());
    }

    @GetMapping("/{categoryId}/cases")
    public ApiResponse<CategoryCaseListResponse> getCasesByCategory(@PathVariable String categoryId) {
        return ApiResponse.success(categoryService.getCasesByCategory(categoryId));
    }
}
