FROM openjdk:8-jdk-alpine
ARG JAR=target/*.jar
COPY ${JAR} media-service.jar
ENTRYPOINT ["java","-jar","media-service.jar"]
EXPOSE 9001
