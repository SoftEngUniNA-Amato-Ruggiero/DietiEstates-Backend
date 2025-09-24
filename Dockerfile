FROM eclipse-temurin:21-jdk AS build
WORKDIR /app
COPY ./pom.xml ./pom.xml
COPY ./.mvn ./.mvn
COPY ./mvnw ./mvnw
COPY ./src ./src
RUN ./mvnw clean package -DskipTests

FROM eclipse-temurin:21-jre
WORKDIR /app
COPY --from=build /app/target/*.jar app.jar
EXPOSE 8081
ENTRYPOINT ["java", "-jar", "app.jar"]