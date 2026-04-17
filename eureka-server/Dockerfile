FROM maven:3.9-eclipse-temurin-17-alpine AS build
#Maven + JDK use karke project build karte hain

WORKDIR /app
COPY pom.xml .
RUN mvn dependency:go-offline -B
COPY src src
RUN mvn package -DskipTests -B
#Jar file ban rahi hai

FROM eclipse-temurin:17-jre-alpine
#Lightweight runtime image

WORKDIR /app
RUN addgroup -S spring && adduser -S spring -G spring
USER spring:spring
COPY --from=build /app/target/*.jar app.jar
EXPOSE 8761
#Eureka dashboard port expose
ENTRYPOINT ["java", "-Xmx256m", "-jar", "app.jar"]
