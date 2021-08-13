FROM openjdk:8-jdk-alpine
ARG JAR=target/*.jar
COPY ${JAR} cloud-gateway.jar
ENTRYPOINT ["java","-jar","cloud-gateway.jar"]
EXPOSE 9191
