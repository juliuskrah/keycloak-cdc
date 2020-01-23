FROM adoptopenjdk/openjdk11-openj9:jdk-11.0.1.13-alpine-slim
COPY build/libs/app-client-*-all.jar app-client.jar
EXPOSE 2000
CMD java -jar app-client.jar