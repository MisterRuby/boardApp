## 1. 개요

- 게시판 제작 프로젝트
- 개발 인원 : 1명
- 개발 기간 : 2022.5.30 ~ 2022.6.14
- 개발 언어 : Java 11
- 개발 기술
    - Springboot, Spring Security, Spring Data JPA & QueryDSL, JUnit, AWS (EC2 & S3)
- 프로젝트 목적
    - 게시판 제작을 통해 웹의 기본적인 흐름을 이해
- 배포
    - [http://ec2-3-35-250-139.ap-northeast-2.compute.amazonaws.com:8080/](http://ec2-3-35-250-139.ap-northeast-2.compute.amazonaws.com:8080/)

## 2. 주요 기능

- 사용자
    - 회원가입, 이메일 인증, 프로필 수정
- 게시판
    - CRUD 기능, 게시글 검색, 페이징 처리, 댓글 등록 및 삭제
    

## 3. DB 설계

![Untitled](https://s3-us-west-2.amazonaws.com/secure.notion-static.com/88094e62-2092-4b2f-bb2d-d17477273826/Untitled.png)

- Account - 계정
    
    ![Untitled](https://s3-us-west-2.amazonaws.com/secure.notion-static.com/7150ded7-4fd4-47a1-8d8f-9abe156b512e/Untitled.png)
    

- Board - 게시글
    
    ![Untitled](https://s3-us-west-2.amazonaws.com/secure.notion-static.com/880c9c8a-888c-4df0-a1e4-098fe5216496/Untitled.png)
    

- Comment - 댓글
    
    ![Untitled](https://s3-us-west-2.amazonaws.com/secure.notion-static.com/b2ba988b-24a7-44f7-b416-f6e6fafce65c/Untitled.png)
    

- Persistent_Logins - 로그인 상태 유지 여부
    
    ![Untitled](https://s3-us-west-2.amazonaws.com/secure.notion-static.com/9a8f9471-1b1e-4eec-b053-188a87c6b9ac/Untitled.png)
