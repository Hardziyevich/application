FROM adoptopenjdk/openjdk16:ubi
WORKDIR /application
COPY ./target /target
WORKDIR /target
EXPOSE 8081
ENTRYPOINT ["java","-jar","root.jar"]