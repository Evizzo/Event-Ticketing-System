FROM openjdk:21-oracle

WORKDIR /app

COPY target/event-ticketing-system-0.0.1-SNAPSHOT.jar app.jar

EXPOSE 8080

ENTRYPOINT ["java", "-jar", "app.jar"]