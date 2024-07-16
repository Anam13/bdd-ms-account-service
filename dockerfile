FROM openjdk:17-jdk-alpine
WORKDIR /app
COPY target/bdd-ms-account-service-0.0.4-SNAPSHOT.jar /app/app.jar
CMD ["java", "-jar", "app.jar"]
#docker build -t bdd-ms-account-service .
#docker run -p 9090:9090 bdd-ms-account-service
# docker tag <local_image>:<tag> <username>/<repository>:<tag>
# docker tag bdd-ms-account-service:latest anamika1303/bdd-ms-account-service:latest#
# docker push anamika1303/bdd-ms-account-service:latest
#docker pull anamika1303/bdd-ms-account-service