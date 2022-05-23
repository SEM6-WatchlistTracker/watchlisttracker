# syntax=docker/dockerfile:experimental
FROM maven as build
WORKDIR /code
COPY . /code/
RUN --mount=type=cache,target=/root/.m2 mvn package

FROM openjdk:8-jdk-alpine
COPY --from=build /code/target/discovery-1.0.jar discovery-1.0.jar
EXPOSE 8080
ENTRYPOINT ["java", "-jar", "/discovery-1.0.jar"]