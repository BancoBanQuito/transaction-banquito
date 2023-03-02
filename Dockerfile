FROM eclipse-temurin:17-jdk-focal

COPY transaction/target/transaction-0.0.1-SNAPSHOT.jar transaction-0.0.1-SNAPSHOT.jar
ENTRYPOINT ["java","-jar","/transaction-0.0.1-SNAPSHOT.jar"]
EXPOSE 76
