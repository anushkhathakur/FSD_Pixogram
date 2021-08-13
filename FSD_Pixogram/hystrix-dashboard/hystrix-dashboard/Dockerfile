FROM openjdk:8-jdk-alpine
ARG JAR=target/*.jar
COPY ${JAR} hystrix-dashboard.jar
ENTRYPOINT ["java","-jar","hystrix-dashboard.jar"]
EXPOSE 9295
