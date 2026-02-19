# 📊 ChartBoard

## 📢 Introduction
**ChartBoard** — A data visualization web service that transforms SQL query results into various charts and manages them at a glance through customizable dashboards.

<img width="1639" height="760" alt="image" src="https://github.com/user-attachments/assets/fd895e5c-9744-476b-9281-d34f2f5e0c63" />


## 📝 Service Overview
ChartBoard is a data visualization tool designed to help users manage data intuitively. By inputting SQL queries, users can visualize results in various chart formats and add them to personalized dashboards for efficient data monitoring.



## 👥 Developer

| Name      | Role                  |
|--------|--------------|
| Chaeyeon Kwak | Full-stack development |


## 🛠 Tech Stack

- **Framework**: Spring Boot  
- **Language**: Java  
- **Database**: MariaDB  
- **ORM**: Spring Data JPA  
- **Build Tool**: Maven
- **Documentation**: Swagger  

## 📊 Database Entity Relationship Diagram (ERD)
<img width="1582" height="562" alt="image" src="https://github.com/user-attachments/assets/0c031fda-ec94-465a-a254-bbe720f4a46c" />


## 📁 Key File Structure
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
│ │ │ ├── service/ 
│ │ │ │ ├── UserService.java
│ │ │ │ └── QueryResultTableService.java
│ │ │ │
│ │ │ ├── controller/ 
│ │ │ │ ├── UserController.java
│ │ │ │ └── QueryResultTableController.java
│ │ └── resources/
│ │ └── application.properties
│
│ └── test/
│
└── pom.xml
```

## 📌 Key Features

### ✅ SQL Query Execution
- Dynamic Query Processing: Executes user-defined SELECT statements and returns real-time results.
- Data Serialization: Provides structured JSON data optimized for frontend visualization into various chart types.

### 📋 Dashboard Management
- Dashboard CRUD: Supports creating, viewing, and deleting customized dashboards.
- Layout Persistence: Persists chart arrangements, including specific coordinates (position) and dimensions (size), for a consistent user experience.


## 🚀 Getting Started


```bash
# Clone the repository
git clone https://github.com/kwak513/chart-board-back.git
cd chart-board-back

# Run the application (via CLI or IntelliJ)
./mvnw spring-boot:run

```

## 💡How to Run in Eclipse
- Import Project: Select Import → Maven → Existing Maven Projects.
- Select Directory: Browse to the cloned project folder.
- Run Application: Right-click the project → Run As → Spring Boot App.

  
⚠️ Before running the application, ensure you configure the environment settings in the src/main/resources/application.properties file:
```
spring.datasource.url=jdbc:mariadb://localhost:3306/dbname
spring.datasource.username=your_username
spring.datasource.password=your_password
server.port=8080
spring.jpa.hibernate.ddl-auto=update
```
## 🚀 API Documentation (Swagger)
The API documentation is automatically generated and can be accessed via Swagger UI once the server is running:
http://localhost:8080/swagger-ui/index.html

## 🧩 Related Repository
**Frontend**: [Link to Frontend Repo](https://github.com/kwak513/chart-board-front) 



