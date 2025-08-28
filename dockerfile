FROM eclipse-temurin:21-jdk AS build
COPY ./pom.xml /app/pom.xml
COPY ./.mvn /app/.mvn
COPY ./mvnw /app/mvnw
COPY ./src /app/src
WORKDIR /app
RUN ./mvnw clean package -DskipTests

FROM eclipse-temurin:21-jre
WORKDIR /app
COPY --from=build /app/target/*.jar app.jar
EXPOSE 8081
ENTRYPOINT ["java", "-jar", "app.jar"]