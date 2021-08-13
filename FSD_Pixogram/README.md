# Project
# Microservices for pixogram
# 1.service-registry
# 2.cloud-config-server
# 3.cloud-gateway
# 4.hystrix-dashboard
# 5.media-service
# 6.user-service

#Angular8
#1. pixogram-ui


## Run Spring Boot application-  run each microservice in same above sequence order.
```
mvn spring-boot:run

#Local urls:
service-registry : http://localhost:8761/
http://localhost:9002/users/signin
http://localhost:9002/users/signup
http://localhost:9001/producer/media/singleFileUpload/
http://localhost:9001/producer/media/multipleFileUpload/
http://localhost:9001/producer/media/userfiles/{userId}

```

## Run following SQL insert statements
```
INSERT INTO roles(name) VALUES('ROLE_USER');
INSERT INTO roles(name) VALUES('ROLE_MODERATOR');
INSERT INTO roles(name) VALUES('ROLE_ADMIN');
```

