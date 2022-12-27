# SNS 제작
## 📢 목표

```
**SNS CRUD API를 제작하자.**
```

## ☑️ 요구 사항

- AWS EC2에 Docker 배포
- Gitlab CI & Crontab CD
- Swagger
- 회원가입
- 로그인
- 포스트 CRUD

## 아키텍처

![img.png](image/img.png)

- 인텔리제이에서 깃랩으로 프로젝트를 push한다.
- 깃랩에서는 파이프라인을 통해 gradle 빌드와 docker 빌드를 진행한다.
- AWS EC2에서 깃랩 repository의 docker 이미지를 pull한다.
- AWS EC2에서 docker run을 통해 컨테이너를 띄우고 이미지를 실행한다.

## 프로젝트 아키텍처

![img_1.png](image/img_1.png)

- 해당 프로젝트는 Layered Archiectur를 따르고 있다.

## ERD

![img_2.png](image/img_2.png)

## DI 클래스 다이어그램

![img_3.png](image/img_3.png)

- UserController는 UserService를 DI한다.
- UserService는 UserRepository와 EncoderConfig를 DI한다.

![img_4.png](image/img_4.png)

- SecurityConfig는 Authentication, JwtFilter를 DI한다.

![img_5.png](image/img_5.png)

- PostController는 PostService를 DI한다.
- PostService는 PostRepository와 UserRepository를 DI한다.

## ✏️ 미션 수행

- AWS EC2 SWAGGER URL

  [**http://ec2-15-165-160-129.ap-northeast-2.compute.amazonaws.com:8080/swagger-ui/#/**](http://ec2-15-165-160-129.ap-northeast-2.compute.amazonaws.com:8080/swagger-ui/#/)


### 1️⃣ 회원가입 : Post /api/v1/users/join

- 회원가입 성공 시 `userId` , `userName` 을 반환한다.
- 실행 결과
    - 성공

    ```json
    {
      "resultCode": "SUCCESS",
      "result": {
        "userId": 3,
        "userName": "홍길동2"
      }
    }
    ```

    - 실패 (userName 중복인 경우)

    ```json
    {
      "resultCode": "ERROR",
      "result": {
        "message": "UserName이 중복됩니다.",
        "errorCode": "DUPLICATED_USER_NAME"
      }
    }
    ```


### 2️⃣ 로그인 : Post /api/v1/users/login

- 로그인 성공 시 `jwt` 를 반환한다.
- 실행결과
    - 성공

    ```json
    {
      "resultCode": "SUCCESS",
      "result": {
        "jwt": "eyJhbGciOiJIUzI1NiJ9.eyJ1c2VyTmFtZSI6Iu2Zjeq4uOuPmTEiLCJpYXQiOjE2NzIxMjcwMjgsImV4cCI6MTY3MjEzMDYyOH0.gQMHyIa-sI0f2tagE12OqfXAKk7rnMVnLz14w7AmGsY"
      }
    }
    ```

    - 실패 (패스워드가 다른 경우)

    ```json
    {
      "resultCode": "ERROR",
      "result": {
        "message": "패스워드가 다릅니다.",
        "errorCode": "INVALID_PASSWORD"
      }
    }
    ```

    - 실패(userName이 존재하지 않는 경우)

    ```json
    {
      "resultCode": "ERROR",
      "result": {
        "message": "존재하지 않는 UserName입니다.",
        "errorCode": "USERNAME_NOT_FOUND"
      }
    }
    ```


### 3️⃣ 포스트 작성 : Post /api/v1/posts

- 포스트 작성 성공 시 `message` , `postId` 를 반환한다.
- 실행결과
    - 성공

    ```json
    {
      "resultCode": "SUCCESS",
      "result": {
        "message": "포스트 등록 완료",
        "postId": 20
      }
    }
    ```

    - 실패 (토큰 인증 실패)

    ```json
    {
      "resultCode": "ERROR",
      "result": {
        "message": "잘못된 토큰입니다.",
        "errorCode": "UNAUTHORIZED"
      }
    }
    ```


### 4️⃣ 포스트 상세 조회 : Get /api/v1/posts/{id}

- 포스트 상세 조회 성공 시 `id` , `title`, `body`, `userName`, `createAt`, `lastModifiedAt` 을 반환한다.
- 실행결과
    - 성공

    ```json
    {
      "resultCode": "SUCCESS",
      "result": {
        "id": 3,
        "title": "spring boot",
        "body": "wellcome",
        "userName": "홍길동1",
        "createdAt": "2022-12-27T06:54:24.214842",
        "lastModifiedAt": "2022-12-27T06:55:32.224082"
      }
    }
    ```

    - 실패 (포스트가 존재하지 않는 경우)

    ```json
    {
      "resultCode": "ERROR",
      "result": {
        "message": "해당 포스트가 없습니다.",
        "errorCode": "POST_NOT_FOUND"
      }
    }
    ```


### 5️⃣ 포스트 수정 : Put /api/v1/posts

- 포스트 수정 성공 시 `message`, `postId` 를 반환한다.
- 실행 결과
    - 성공

    ```json
    {
      "resultCode": "SUCCESS",
      "result": {
        "message": "포스트 수정 완료",
        "postId": 20
      }
    }
    ```

    - 실패 (자신의 포스트가 아닌 경우)

    ```json
    {
      "resultCode": "ERROR",
      "result": {
        "message": "사용자가 권한이 없습니다.",
        "errorCode": "INVALID_PERMISSION"
      }
    }
    ```

    - 실패 (토큰 인증 실패한 경우)

    ```json
    {
      "resultCode": "ERROR",
      "result": {
        "message": "잘못된 토큰입니다.",
        "errorCode": "UNAUTHORIZED"
      }
    }
    ```


### 6️⃣ 포스트 삭제 : Delete /api/v1/posts

- 포스트 삭제 성공 시 `message` , `postId`  를 반환한다.
- 실행결과
    - 성공

    ```json
    Response body
    Download
    {
      "resultCode": "SUCCESS",
      "result": {
        "message": "포스트 삭제 완료",
        "postId": 22
      }
    }
    ```

    - 실패 (자신의 포스트가 아닌 경우)

    ```json
    {
      "resultCode": "ERROR",
      "result": {
        "message": "사용자가 권한이 없습니다.",
        "errorCode": "INVALID_PERMISSION"
      }
    }
    ```

    - 실패 (토큰 인증 실패한 경우)

    ```json
    {
      "resultCode": "ERROR",
      "result": {
        "message": "잘못된 토큰입니다.",
        "errorCode": "UNAUTHORIZED"
      }
    }
    ```


### 7️⃣ 포스트 목록 조회 : Get /api/v1/posts

- 포스트 목록 조회 성공 시 모든 포스트를 pageable 하여 반환한다.
- 실행 결과
    - 성공

    ```json
    {
      "resultCode": "SUCCESS",
      "result": {
        "content": [
          {
            "id": 21,
            "title": "hello-title",
            "body": "hello-body",
            "userName": "kyeongrok22",
            "createdAt": "2022-12-27T07:58:00.44822",
            "lastModifiedAt": "2022-12-27T07:58:00.44822"
          },
          {
            "id": 20,
            "title": "spring boot2",
            "body": "wellcome",
            "userName": "홍길동2",
            "createdAt": "2022-12-27T07:48:27.84908",
            "lastModifiedAt": "2022-12-27T07:56:04.8232"
          }
    ],
        "pageable": {
          "sort": {
            "empty": false,
            "sorted": true,
            "unsorted": false
          },
          "offset": 0,
          "pageNumber": 0,
          "pageSize": 20,
          "paged": true,
          "unpaged": false
        },
        "totalPages": 1,
        "totalElements": 20,
        "last": true,
        "size": 20,
        "number": 0,
        "sort": {
          "empty": false,
          "sorted": true,
          "unsorted": false
        },
        "numberOfElements": 20,
        "first": true,
        "empty": false
      }
    }
    ```


## 회고록

1️⃣ [https://velog.io/@goodjav/1220-SNS-제작-스웨거-CICD](https://velog.io/@goodjav/1220-SNS-%EC%A0%9C%EC%9E%91-%EC%8A%A4%EC%9B%A8%EA%B1%B0-CICD)

2️⃣ [https://velog.io/@goodjav/1220-SNS-제작-회원가입](https://velog.io/@goodjav/1220-SNS-%EC%A0%9C%EC%9E%91-%ED%9A%8C%EC%9B%90%EA%B0%80%EC%9E%85)

3️⃣ [https://velog.io/@goodjav/1221-SNS-제작-로그인](https://velog.io/@goodjav/1221-SNS-%EC%A0%9C%EC%9E%91-%EB%A1%9C%EA%B7%B8%EC%9D%B8)

4️⃣ [https://velog.io/@goodjav/1226-SNS-제작-포스트-작성](https://velog.io/@goodjav/1226-SNS-%EC%A0%9C%EC%9E%91-%ED%8F%AC%EC%8A%A4%ED%8A%B8-%EC%9E%91%EC%84%B1)

5️⃣ [https://velog.io/@goodjav/1227-SNS-제작-포스트-상세-조회-포스트-수정](https://velog.io/@goodjav/1227-SNS-%EC%A0%9C%EC%9E%91-%ED%8F%AC%EC%8A%A4%ED%8A%B8-%EC%83%81%EC%84%B8-%EC%A1%B0%ED%9A%8C-%ED%8F%AC%EC%8A%A4%ED%8A%B8-%EC%88%98%EC%A0%95)