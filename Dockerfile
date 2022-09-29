FROM gradle:7.5.1-jdk11-focal as gradle

COPY --chown=gradle:gradle . /home/gradle/src
WORKDIR /home/gradle/src
RUN gradle build --no-daemon

FROM eclipse-temurin:11.0.16.1_1-jdk-focal

EXPOSE 8080

RUN mkdir /app

COPY --from=gradle /home/gradle/src/build/libs/*.jar /app/transfer-api.jar

WORKDIR /app

ENTRYPOINT ["java", "-jar", "transfer-api.jar"]