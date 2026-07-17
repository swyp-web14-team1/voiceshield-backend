package com.swyp.voiceshield.category;

public record CategoryResponse(String categoryId, String categoryName) {

    static CategoryResponse from(Category category) {
        return new CategoryResponse(category.getId(), category.getName());
    }
}
