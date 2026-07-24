# Elastic Beanstalk GitHub Actions Deployment

## Goal
- `main` 브랜치에 머지되면 GitHub Actions가 JAR를 빌드하고 Elastic Beanstalk 운영 환경에 자동 배포한다.

## Workflow file
- `.github/workflows/deploy-eb.yml`

## Required GitHub Secrets
- `AWS_ACCESS_KEY_ID`
- `AWS_SECRET_ACCESS_KEY`
- `AWS_REGION`
- `EB_APPLICATION_NAME`
- `EB_ENVIRONMENT_NAME`
- `S3_BUCKET`

## Deployment flow
1. `main` 브랜치에 push 발생
2. GitHub Actions가 JDK 21 환경 준비
3. `./gradlew clean bootJar` 실행
4. 생성된 JAR를 S3에 업로드
5. Elastic Beanstalk Application Version 생성
6. 운영 Environment를 새 Version으로 업데이트
7. 배포 완료까지 대기 후 상태 출력

## Notes
- 현재 workflow는 AWS Access Key 기반이다.
- 추후 보안 강화를 위해 GitHub OIDC + IAM Role 방식으로 전환하는 것이 좋다.
- `S3_BUCKET`은 미리 생성되어 있어야 한다.
- 자동화가 안정화되기 전까지는 수동 업로드/배포 절차를 백업 수단으로 유지한다.

## First-time setup checklist
- GitHub Repository Secrets 등록
- AWS IAM 배포 사용자 권한 검토
- S3 버킷 생성 및 접근 권한 검토
- Elastic Beanstalk Application / Environment 이름 재확인
- 테스트 커밋으로 workflow 1회 실행
- 배포 후 Swagger 및 주요 API 정상 동작 확인
