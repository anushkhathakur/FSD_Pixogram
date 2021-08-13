FROM openjdk:8-jdk-alpine
ARG JAR=target/*.jar
COPY ${JAR} cloud-config-server.jar
ENTRYPOINT ["java","-jar","cloud-config-server.jar"]
EXPOSE 9296
