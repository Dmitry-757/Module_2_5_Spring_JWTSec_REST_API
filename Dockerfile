FROM openjdk:17-jdk-alpine
VOLUME /main-app
ADD build/libs/Module_2_5_Spring_JWTSec_REST_API-0.0.1-SNAPSHOT.jar app.jar
EXPOSE 8090
ENTRYPOINT ["java", "/app.jar"]