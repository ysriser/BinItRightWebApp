# Use a slim JRE for the final image
FROM eclipse-temurin:21-jre-jammy

# 1. Create a dedicated group and user for our application
# Using --system creates a user without a password or home directory
RUN groupadd --system --gid 1001 appgroup && \
    useradd --system --uid 1001 --gid appgroup appuser

WORKDIR /app

# 2. Copy the JAR file
COPY target/*.jar app.jar

# 3. Change ownership of the app directory to our new user
# This ensures 'appuser' can actually read/execute the JAR
RUN chown -R appuser:appgroup /app

# 4. Switch to the non-root user
USER 1001

EXPOSE 8080

# The process now runs as 'appuser' instead of 'root'
ENTRYPOINT ["java", "-jar", "app.jar"]