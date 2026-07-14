# ---- Stage 1: Build ----
# Compile from source inside the image so the jar can never be stale.
FROM maven:3.9-eclipse-temurin-17 AS build
WORKDIR /build

# Cache dependencies first: only re-download if pom.xml changes.
COPY pom.xml .
RUN mvn -B dependency:go-offline

# Now build the application from source.
COPY src ./src
RUN mvn -B -DskipTests clean package

# ---- Stage 2: Runtime ----
FROM eclipse-temurin:17-jre-alpine
WORKDIR /app

# Copy ONLY the jar produced by stage 1 above.
COPY --from=build /build/target/*.jar app.jar

EXPOSE 8080

ENTRYPOINT ["java","-jar","/app/app.jar"]
