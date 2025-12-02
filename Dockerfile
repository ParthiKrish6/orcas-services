 # Stage 1: Build the application
FROM maven:3.8.4-openjdk-17 AS build
WORKDIR /Orcas
COPY pom.xml .
COPY src ./src
RUN mvn clean package -DskipTests

# Stage 2: Create the final image with JRE
FROM eclipse-temurin:17-jre-alpine
WORKDIR /Orcas
COPY --from=build /Orcas/target/*.jar orcas-team-v1.jar
ENTRYPOINT ["java", "-jar", "orcas-team-v1.jar"]




