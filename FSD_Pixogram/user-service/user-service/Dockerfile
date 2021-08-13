FROM openjdk:8-jdk-alpine
ARG JAR=target/*.jar
COPY ${JAR} user-service.jar
ENTRYPOINT ["java","-jar","user-service.jar"]
EXPOSE 9002
