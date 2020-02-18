FROM gradle:jdk11 AS build

COPY --chown=gradle:gradle . /home/gradle/src
WORKDIR /home/gradle/src
RUN gradle assemble

FROM openjdk:11-jre-slim
EXPOSE 8081
COPY --from=build /home/gradle/src/build/libs/app-client-0.0.1.jar /app/app.jar
WORKDIR /app
CMD java -jar app.jar