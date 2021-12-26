FROM maven:3.8.4-eclipse-temurin-16 AS builder
COPY src /home/app/src
COPY pom.xml /home/app
RUN mvn -f /home/app/pom.xml clean package

FROM adoptopenjdk/openjdk16:ubi
COPY --from=builder /home/app/target /usr/local/lib
EXPOSE 8081
ENTRYPOINT ["java","-jar","/usr/local/lib/root.jar"]