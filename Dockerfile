# Use a slim JRE for the final image
FROM eclipse-temurin:21-jre-jammy
WORKDIR /app
# Expecting the JAR to be provided by the CI pipeline
COPY target/*.jar app.jar
EXPOSE 8080
ENTRYPOINT ["java", "-jar", "app.jar"]