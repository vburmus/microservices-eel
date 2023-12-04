# Certificates Shop 
#### *Microservices Rest API | E-commerce application*

## List of microservices:
- ### API-Gateway
    - unified entry point
    - scaling and load balancing
- ### Eureka-Server
    - service discovery
    - load balancing
    - health monitoring
- ### Config-Server 
    - [config repository](https://github.com/vburmus/microservices-config)
    - centralized configuration
    - configuration management
- ### Aws-Utils
  - AWS S3 image loading
- ### Auth-Service (auth database)
  - authentication and authorization
  - access and refresh tokens
  - other services use [Auth-Library by vburmus](https://github.com/vburmus/auth-library) to verify tokens
  - user roles and credentials
- ### User-Service (user database)
    - user profile data
- ### Commerce-Service (commerce database)
    - certificates 
    - tags 
    - purchases
- ### Notification-Service
    - registration email verification 
    - purchase notifications

## Used technologies:
 - **Java 17**
 - **Kotlin**
 - **Spring Security** for authentication and authorization
 - **Spring Cloud** 
   - Api-Gateway
   - Eureka-Server
   - Config-Server
 - **Spring Boot** for microservices
 - **Hibernate** for data access
 - **PostgreSQL** for data storage (3 separate databases)
 - **AWS** for image storage
 - **Docker** for containerization
 - **RabbitMQ** for messaging
 - **Zipkin** for tracing
 - **JWT** for authentication
 - **Gradle** 
 - **Thymeleaf** with HTML for email templates
 - **Sonar** for code quality
 - **Caffeine Cache**

## Used additional libraries:
 - Zalando Problem
 - Json Merge Patch
 - Lombok
 - Mapstruct
 - [hypersistence-utils](https://github.com/vladmihalcea/hypersistence-utils) for Enum mapping
 - [Auth-Library by vburmus](https://github.com/vburmus/auth-library)

### Soon:
 - Death lists in ampq
 - Kubernetes Deployment
 - Tests