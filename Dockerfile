FROM openjdk:11

COPY target/geniusee-task-1.0-SNAPSHOT.jar geniusee-task-1.0-SNAPSHOT.jar

ENTRYPOINT ["java", "-jar", "/geniusee-task-1.0-SNAPSHOT.jar"]