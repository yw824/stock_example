## 재고시스템으로 알아 보는 동시성 이슈 해결 방법 
- inflearn 

### Docker - MySQL 
```command
docker pull mysql 
docker run -d -p 3306:3306 -e MYSQL_ROOT_PASSWORD=1234 --name mysql-stock mysql
docker ps
docker exec -it mysql /bin/bash
mysql -u root -p
```

- mysql database example:  
```mysql-sql
create database stock_example;
use stock_example;
```

### SpringBoot Config 
#### Libraries 
- Spring Web 
- Lombok 
- JDBC API
- Spring JPA
- MySQL Driver

#### application.yml: 
```yaml
# application.yml
server:
  port: 8003

spring:
  application:
    name: stock

  config:
    import: "classpath:/env.yml"

  jpa:
    hibernate:
      ddl-auto: create
    show-sql: true

  datasource:
    driver-class-name: com.mysql.cj.jdbc.Driver
    url: jdbc:mysql://localhost:{$mysql.port_num}/${mysql.db_name}
    username: ${mysql.username}
    password: ${mysql.password}

    properties:
      hibernate.dialect: org.hibernate.dialect.MySQLDialect

logging:
  level:
    org:
      hibernate:
        SQL: DEBUG
        type:
          descriptor:
            sql:
              BasicBinder: TRACE
```
- env.yml: 
```yaml
# env.yml:
mysql:
  username: 
  password: 
  db_name: 
  port_num: 
```