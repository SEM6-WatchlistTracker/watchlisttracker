# syntax=docker/dockerfile:experimental
FROM maven as build
WORKDIR /code
COPY . /code/
RUN --mount=type=cache,target=/root/.m2 mvn package

FROM openjdk:8-jre-alpine
COPY --from=build /code/target/mediastats-service-1.1.jar mediastats-service-1.1.jar
EXPOSE 8080
ENTRYPOINT ["java", "-jar", "/mediastats-service-1.1.jar"]