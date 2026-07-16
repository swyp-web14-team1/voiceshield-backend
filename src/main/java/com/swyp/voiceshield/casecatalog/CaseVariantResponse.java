package com.swyp.voiceshield.casecatalog;

import java.util.Comparator;
import java.util.List;

public record CaseVariantResponse(
        String variantId,
        CaseChannel channel,
        String content,
        List<CaseVariantOptionResponse> options
) {

    static CaseVariantResponse from(CaseVariant variant) {
        List<CaseVariantOptionResponse> options = variant.getOptions().stream()
                .sorted(Comparator.comparingInt(CaseVariantOption::getOptionNumber))
                .map(CaseVariantOptionResponse::from)
                .toList();
        return new CaseVariantResponse(variant.getId(), variant.getChannel(), variant.getContent(), options);
    }
}
