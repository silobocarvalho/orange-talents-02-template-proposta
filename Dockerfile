FROM openjdk:11
ARG JAR_FILE=target/Proposal-0.0.1-SNAPSHOT.jar
COPY ${JAR_FILE} proposal-sid-docker-app.jar
ENTRYPOINT ["java","-jar","/proposal-sid-docker-app.jar"]