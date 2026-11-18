FROM maven:3.9-eclipse-temurin-17 AS build
WORKDIR /app

COPY pom.xml .
COPY src ./src

RUN mvn -B -DskipTests package

FROM eclipse-temurin:17-jre-jammy
WORKDIR /app

RUN groupadd --system corebank \
    && useradd --system --gid corebank --home-dir /app corebank

COPY --from=build /app/target/corebank-*.jar /app/corebank.jar
RUN chown corebank:corebank /app/corebank.jar

USER corebank
EXPOSE 8088

ENTRYPOINT ["java", "-jar", "/app/corebank.jar"]
