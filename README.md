# Figure Alert

피규어의 과거 발매 정보부터 현재 및 발매 예정 정보를 한곳에서 확인하고,
관심 캐릭터의 신제품과 재발매 정보를 추적할 수 있는 서비스를 목표로 개발하고 있습니다.

기존 판매 사이트에서는 판매가 종료된 피규어 정보를 다시 찾기 어렵다는 점에서
프로젝트를 시작했습니다.

현재는 피규어 카탈로그의 기본 기능을 구현한 상태이며,
도메인 구조와 API를 리팩터링하면서 기능을 확장하고 있습니다.

## Tech Stack

### Backend
- Java 21
- Spring Boot
- Spring Data JPA

### Database
- MySQL

### Build
- Gradle

## Current Features

- 피규어 등록 / 조회 / 삭제
- 피규어 이름 검색
- 캐릭터 기반 피규어 검색
- 캐릭터 관리
- 제조사 관리
- 발매 정보 관리
- JPA / MySQL 연동
- 요청 / 응답 DTO
- 예외 처리

## Domain

- Figure
- Character
- Manufacturer
- Release

## Planned Features

- 검색 기능 개선
- 페이지네이션
- 회원가입 / 로그인
- 위시리스트
- 캐릭터 구독
- 신제품 / 재발매 알림
- 테스트 코드
- Docker 및 배포

## Status

🚧 개발 진행 중