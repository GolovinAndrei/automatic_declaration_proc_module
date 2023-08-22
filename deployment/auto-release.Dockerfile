# This image is harbor.declarant.by/dev/ms-auto-release:v1
FROM openjdk:8u212-jre-alpine3.9

ENV TZ Europe/Minsk

ADD ./target/auto-release.jar /app/
WORKDIR /app/
RUN apk update --no-cache && apk upgrade

CMD ["sh", "-c", "java -server -jar auto-release.jar"]
EXPOSE 7034
