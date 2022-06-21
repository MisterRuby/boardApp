# 게시판 제작 프로젝트

## 1. 개요

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
<img width="700" alt="image" src="https://user-images.githubusercontent.com/93859705/174723799-2bcb26ad-1d12-4afb-942b-1bc6a54af11e.png">


- Account - 계정
    
    <img width="678" alt="image" src="https://user-images.githubusercontent.com/93859705/174723944-f5f51026-c1d0-4faa-96b8-7d633704869d.png">
    

- Board - 게시글
    
    <img width="679" alt="image" src="https://user-images.githubusercontent.com/93859705/174723990-85383b45-21ab-4c16-a04f-c47b2576fe7e.png">
    

- Comment - 댓글
    
    <img width="678" alt="image" src="https://user-images.githubusercontent.com/93859705/174724027-37da17db-90cb-4ddf-8831-00ba7c5af7bf.png">
    

- Persistent_Logins - 로그인 상태 유지 여부
    
    <img width="679" alt="image" src="https://user-images.githubusercontent.com/93859705/174724056-e01658c3-00fc-448c-8f27-4a4cfc3effd4.png">

