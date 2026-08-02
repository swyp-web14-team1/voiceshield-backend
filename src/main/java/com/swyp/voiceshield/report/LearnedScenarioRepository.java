package com.swyp.voiceshield.report;

import com.swyp.voiceshield.learning.LearningHistory;
import java.util.List;
import org.springframework.data.repository.Repository;

/**
 * 추천에서 "이미 학습한 사례"를 걸러내기 위한 조회 전용 리포지터리.
 *
 * <p>{@code learning} 패키지의 {@code LearningHistoryRepository}에 메서드를 추가하는 대신
 * 이 패키지에 따로 둔다. 같은 엔티티에 리포지터리를 여러 개 두는 것은 Spring Data가 지원하며,
 * 이렇게 하면 병행 작업 중인 다른 사람의 파일을 건드리지 않는다.
 */
interface LearnedScenarioRepository extends Repository<LearningHistory, Long> {

    List<LearningHistory> findAllByUserId(String userId);
}
