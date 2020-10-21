FROM maven:3.6-jdk-8 as maven
WORKDIR /app
COPY ./pom.xml ./pom.xml
RUN mvn dependency:go-offline -B
COPY ./src ./src

# TODO: jollof-* should be replaced with the proper prefix
RUN mvn package && cp target/CertExercise-0.0.1-SNAPSHOT.war app.war

# Rely on Docker's multi-stage build to get a smaller image based on JRE
FROM tomcat:latest
RUN rm -rf /usr/local/tomcat/webapps/*
COPY --from=maven /app/app.war /usr/local/tomcat/webapps/ROOT.war

# VOLUME /tmp  # optional
EXPOSE 8080

CMD ["catalina.sh", "run"]