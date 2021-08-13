FROM openjdk:8-jdk-alpine
ARG JAR=target/*.jar
COPY ${JAR} docker-service-registry.jar
ENTRYPOINT ["java","-jar","docker-service-registry.jar"]
EXPOSE 8761
