# Base Image
FROM eclipse-temurin:21-jre

# Metadata
LABEL authors="royarg"
LABEL maintainer="Arghya"
LABEL version="1.0"

# Environment variables
ENV TZ=Asia/Kolkata

# Working directory
WORKDIR /app

# Copy files
COPY target/*.jar app.jar

# Install dependencies
#RUN

# Expose port
EXPOSE 8080

# Health check
#HEALTHCHECK CMD curl --fail http://localhost:8080/actuator/health || exit 1

# Volume mount point
#VOLUME ...

# User permissions
#USER ...

# Startup command
ENTRYPOINT ["java","-jar","app.jar"]
CMD ["java","-jar","app.jar"]




#docker build -t app .
#docker run -p 8080:8080 app
#docker inspect app
#docker logs app
#docker exec -it container-id sh