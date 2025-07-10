# 📊 차트보드 (ChartBoard)

## 📢 서비스 한줄 소개  
**ChartBoard** — SQL 쿼리 결과를 다양한 차트로 시각화하고, 대시보드에서 차트들을 한눈에 조회하는 데이터 시각화 웹 서비스

---

## 📝 서비스 소개  
**ChartBoard**는 사용자가 입력한 SQL 쿼리 결과를 **다양한 형태의 차트로 시각화**하고, 이를 대시보드에 추가하여 데이터를 직관적으로 관리할 수 있도록 돕는 데이터 시각화 도구입니다.


---

## 👥 개발자 소개

| 이름   | 역할         |
|--------|--------------|
| 곽채연 | Frontend 개발, Backend 개발 |

---

## 🛠 기술 스택

- **Framework**: Spring Boot  
- **Language**: Java  
- **Database**: MariaDB  
- **ORM**: Spring Data JPA  
- **Build Tool**: Maven
- **Documentation**: Swagger  

---

## 📁 주요 파일 구조
```
ChartBoard/
├── src/
│ ├── main/
│ │ ├── java/
│ │ │ └── com/chartboard/
│ │ │ ├── common
│ │ │ │ └── JPAUtil.java
│ │ │
│ │ │ ├── config
│ │ │ │ └── SecurityConfig.java
│ │ │
│ │ │ ├── dto/
│ │ │ │ ├── UserLoginDto.java
│ │ │ │ ├── UserRegisterDto.java
│ │ │ │ ├── InsertDbConnectionDto.java
│ │ │ │ ├── ChartInfoDto.java
│ │ │ │ ├── DashboardInfoDto.java
│ │ │ │ ├── ChartDashboardConnectDto.java
│ │ │ │ └── ChartsIntoDashboardDto.java
│ │ │ │
│ │ │ ├── service/ # JPA 인터페이스
│ │ │ │ ├── UserService.java
│ │ │ │ └── QueryResultTableService.java
│ │ │ │
│ │ │ ├── controller/ # API 요청 처리
│ │ │ │ ├── UserController.java
│ │ │ │ └── QueryResultTableController.java
│ │ └── resources/
│ │ └── application.properties
│
│ └── test/
│
└── pom.xml
```
---

## 📌 주요 기능

### ✅ SQL 쿼리 실행
- 사용자가 입력한 SELECT 쿼리를 실행하고 결과를 반환  
- 프론트엔드에서 다양한 차트로 시각화 가능하도록 JSON 데이터 제공

### 📋 대시보드 관리
- 대시보드 생성, 삭제 기능  
- 대시보드에 차트 배치 및 위치, 크기 저장

---

## 🚀 백엔드 서버 실행 방법


```bash
# 프로젝트 클론
git clone https://github.com/kwak513/chart-board-back.git
cd chart-board-back

# 실행 (IntelliJ에서 실행하거나 CLI에서)
./mvnw spring-boot:run

```

## 💡Eclipse 실행 방법
- 프로젝트를 Import → Maven → Existing Maven Projects로 불러오기
- 프로젝트 선택 후 Run As → Spring Boot App 실행

  
⚠️ src/main/resources/application.properties 파일에서 DB 연결 정보 등 환경 설정 필요
```
spring.datasource.url=jdbc:mariadb://localhost:3306/dbname
spring.datasource.username=your_username
spring.datasource.password=your_password
server.port=8080
spring.jpa.hibernate.ddl-auto=update
```
## 🚀 API 문서 (Swagger)
Swagger UI로 API 문서 확인 가능:
http://localhost:8080/swagger-ui/index.html

## 🧩 관련 레포지토리
**Frontend**: [Link to Frontend Repo](https://github.com/kwak513/chart-board-front) 

---


