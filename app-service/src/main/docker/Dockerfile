FROM gradle:jdk11 AS build

COPY --chown=gradle:gradle . /home/gradle/src
WORKDIR /home/gradle/src
RUN gradle assemble

FROM openjdk:11-jre-slim
EXPOSE 8082
COPY --from=build /home/gradle/src/build/libs/app-service-0.1-all.jar /app/app.jar
WORKDIR /app
CMD java -jar app.jar