FROM openjdk:8-jdk-alpine
COPY target/*.jar /deployments/application.jar
ENTRYPOINT ["java", "-jar", "/deployments/application.jar"]
