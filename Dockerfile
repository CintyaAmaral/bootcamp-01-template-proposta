## Builder Image
FROM maven:3.6.3-jdk-11-slim AS builder
COPY src /usr/src/app/src
COPY pom.xml /usr/src/app
RUN mvn -f /usr/src/app/pom.xml clean package

## Runner Image
FROM openjdk:11-jdk-slim
COPY --from=builder /usr/src/app/target/proposta-0.0.1-SNAPSHOT.jar /usr/app/app.jar
EXPOSE 8080
ENTRYPOINT ["java","-jar","/usr/app/app.jar"]

## FROM openjdk:11
## ARG JAR_FILE=target/proposta-0.0.1-SNAPSHOT.jar
## COPY ${JAR_FILE} app.jar
## ENTRYPOINT ["java","-jar","/app.jar"]