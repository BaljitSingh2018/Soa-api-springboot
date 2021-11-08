FROM openjdk:latest
EXPOSE 8080
ARG JAR_FILE=/target/favorite_coin_list_api.jar
ADD ${JAR_FILE} app.jar
ENTRYPOINT ["java","-jar","/app.jar"]