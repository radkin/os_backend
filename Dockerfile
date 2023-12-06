FROM openjdk:22-bullseye
LABEL authors="noel"
VOLUME /tmp
ARG JAR_FILE
ADD /src/main/resources/application.properties /application.properties
COPY target/*.jar app.jar
ENTRYPOINT ["java","-jar","/app.jar"]