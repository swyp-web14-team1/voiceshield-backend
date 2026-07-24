package com.swyp.voiceshield.reportguide;

import java.util.List;

public record ReportGuideResponse(
        List<String> reportSteps,
        List<ReportGuideContactResponse> emergencyContacts,
        List<String> realActionGuide,
        List<String> preventionTips
) {
}
