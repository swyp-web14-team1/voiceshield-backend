package com.swyp.voiceshield.casecatalog;

public record CaseVariantResponse(String variantId, CaseChannel channel, String content) {

    static CaseVariantResponse from(CaseVariant variant) {
        return new CaseVariantResponse(variant.getId(), variant.getChannel(), variant.getContent());
    }
}
