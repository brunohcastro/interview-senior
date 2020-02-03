FROM openjdk:11-jre-slim-sid

LABEL maintainer="Brainweb"
LABEL version="1.0"

EXPOSE 8082

COPY maven/*.jar gateway.jar

ENTRYPOINT [ "java" , "-Xms128m",  "-Xmx400m", "-jar", "-DLog4jContextSelector=org.apache.logging.log4j.core.async.AsyncLoggerContextSelector", "gateway.jar", "--spring.profiles.active=docker"]