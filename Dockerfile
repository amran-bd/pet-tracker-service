# Stage 1: Build with Maven
FROM maven:3.9-eclipse-temurin-24 AS mvn_build

ENV HOME=/usr/app
RUN mkdir -p $HOME
WORKDIR $HOME
ADD pom.xml $HOME
ADD . $HOME
# Cache Maven dependencies in a separate layer
RUN mvn -B dependency:go-offline

COPY src src
RUN mvn -B clean install -DskipTests

#Nginix For report
FROM nginx:alpine as report-viewer
COPY --from=mvn_build /usr/app/target/site/jacoco/* /usr/share/nginx/html
EXPOSE 80

# Build the application and extract layers
FROM eclipse-temurin:24-jre AS builder
WORKDIR /application
COPY --from=mvn_build /usr/app/target/*.jar application.jar

RUN java -Djarmode=layertools -jar application.jar extract

FROM eclipse-temurin:24-jre
# Copy layers from builder (ordered by least likely to change)
WORKDIR /application

# Copy layers in the correct order
COPY --from=builder application/dependencies/ ./
COPY --from=builder application/spring-boot-loader/ ./
COPY --from=builder application/snapshot-dependencies/ ./
COPY --from=builder application/application/ ./

# Health check and startup
HEALTHCHECK --interval=30s --timeout=3s \
    CMD curl -f http://localhost:8080/actuator/health || exit 1

EXPOSE 8080
ENTRYPOINT ["java", "org.springframework.boot.loader.launch.JarLauncher"]