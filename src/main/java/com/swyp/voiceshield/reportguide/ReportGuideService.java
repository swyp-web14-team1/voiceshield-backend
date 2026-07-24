package com.swyp.voiceshield.reportguide;

import java.util.List;
import org.springframework.stereotype.Service;

@Service
public class ReportGuideService {

    public ReportGuideResponse getReportGuide() {
        return new ReportGuideResponse(
                List.of(
                        "상대방이 요구하는 송금, 앱 설치, 계좌 이체를 즉시 중단한다.",
                        "통화를 종료한 뒤 해당 기관 또는 가족에게 저장된 번호로 다시 확인한다.",
                        "피해가 의심되면 즉시 112 또는 1332로 신고한다."
                ),
                List.of(
                        new ReportGuideContactResponse("경찰청", "112", "보이스피싱 및 범죄 신고"),
                        new ReportGuideContactResponse("금융감독원", "1332", "금융사기 상담 및 피해 대응")
                ),
                List.of(
                        "이미 송금했다면 즉시 은행 고객센터에 지급정지를 요청한다.",
                        "악성 앱을 설치했다면 휴대폰 전원을 끄고 가까운 대리점 또는 보호자 도움을 받는다."
                ),
                List.of(
                        "가족이나 기관을 사칭해도 저장된 번호로 다시 확인한다.",
                        "문자 링크나 앱 설치 요청은 바로 누르지 않는다.",
                        "계좌 이체를 재촉하는 경우 먼저 의심하고 끊고 확인한다."
                )
        );
    }
}
