FROM openjdk:11
EXPOSE  8094
WORKDIR /app
ADD     ./target/*.jar /app/card-service.jar
ENTRYPOINT ["java","-jar","/app/card-service.jar"]