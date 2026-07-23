# Voice Shield Backend

SWYP Web 14 Team 1의 **Voice Shield(보이스피싱 예방 학습 플랫폼)** 백엔드 레포지토리입니다.

Voice Shield는 **보이스피싱 사례 학습과 Voice / Message 시뮬레이션을 통해 사용자가 실제 피싱 상황을 안전하게 경험하고 대응 능력을 기를 수 있도록 돕는 PWA 서비스**입니다.

현재 기획 단계에서는 User Story와 ERD를 기반으로 서비스 구조를 구체화하고 있으며, 사례 학습, 시뮬레이션, AI 피드백, 퀴즈 기능을 중심으로 MVP를 설계하고 있습니다.

---

## 📖 Overview

이 레포지토리는 Voice Shield 서비스의 백엔드 애플리케이션을 위한 공간입니다.

초기 단계에서는 기능 구현에 앞서 프로젝트 구조와 협업 규칙을 정리하고, User Story와 데이터 모델을 기반으로 API 및 도메인 구조를 설계할 예정입니다.

향후 로그인, 사례 조회, 시뮬레이션 결과 저장, AI 피드백, 퀴즈 기능을 순차적으로 구현합니다.

---

## 🛠 Tech Stack

현재 확정된 기술 스택은 아래와 같습니다.

- Java
- Spring Boot

향후 프로젝트 진행에 따라 아래 기술이 추가될 예정입니다.

- Spring Data JPA
- PostgreSQL
- Swagger (OpenAPI)
- Gradle

> 세부 기술 스택 및 버전은 프로젝트 초기 세팅 과정에서 확정 후 반영할 예정입니다.

---

## 🎯 Current Scope

현재 MVP에서 구현 예정인 주요 기능입니다.

- 카카오 로그인
- 사용자 정보 관리
- 보이스피싱 사례 조회
- 카테고리별 사례 학습
- Voice / Message 시뮬레이션
- AI 피드백 결과 저장
- 퀴즈
- 학습 기록 관리
- 접근성 설정 관리

> 세부 정책 및 기능 우선순위는 프로젝트 진행 상황에 따라 변경될 수 있습니다.

---

## 🌱 Branch Strategy

| Branch | Description |
|--------|-------------|
| `main` | 안정 브랜치 |
| `develop` | 개발 통합 브랜치 |
| `feat/*` | 기능 개발 브랜치 |
| `fix/*` | 버그 수정 브랜치 |

---

## 💬 Commit Convention

커밋 메시지는 아래 규칙을 따릅니다.

| Type | Description |
|------|-------------|
| `feat` | 새로운 기능 추가 |
| `fix` | 버그 수정 |
| `docs` | 문서 수정 |
| `style` | 코드 스타일 변경 |
| `refactor` | 리팩터링 |
| `test` | 테스트 코드 |
| `chore` | 설정 및 기타 작업 |

### Example

```bash
feat: 사례 조회 API 구현
feat: 카카오 로그인 구현
docs: README 작성
refactor: User Entity 수정
```

---

## 🔀 Pull Request

- 기능 개발은 `feat/*` 브랜치에서 진행합니다.
- `main` 및 `develop` 브랜치에는 직접 Merge하지 않습니다.
- Pull Request 생성 후 코드 리뷰를 거쳐 Merge합니다.

---

## 📂 Project Structure (Planned)

```text
src
 ├── controller
 ├── service
 ├── repository
 ├── entity
 ├── dto
 ├── config
 ├── exception
 ├── common
 └── security
```

---

## 🚀 Project Status

아래 항목은 프로젝트 진행에 맞춰 순차적으로 업데이트될 예정입니다.

- 프로젝트 구조
- 실행 방법
- 환경 변수 설정
- API 명세
- ERD 및 데이터베이스 설계
- Swagger 문서
- 배포 환경

---

## 📌 MVP Features

- ✅ Kakao Login
- ✅ Scenario Learning
- ✅ Voice / Message Simulation
- ✅ AI Feedback
- ✅ Quiz
- ✅ Learning History
- ✅ Accessibility Settings

## 🐘 Local PostgreSQL

1. Create a local `.env` file from `.env.example`.
2. Start PostgreSQL with `docker compose up -d`.
3. Run the application with the `local` profile enabled:

```bash
./gradlew bootRun --args='--spring.profiles.active=local'
```
