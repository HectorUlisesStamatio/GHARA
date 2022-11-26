FROM openjdk:8-jre-alpine

RUN apk add --no-cache ttf-liberation
RUN mkdir -p /usr/src/app

WORKDIR /usr/src/app

RUN apk add --no-cache tzdata
ENV TZ=America/Lima
RUN ln -snf /usr/share/zoneinfo/$TZ /etc/localtime && echo $TZ > /etc/timezone

COPY target/taller_sba-0.0.1-SNAPSHOT.jar /usr/src/app/app.jar

EXPOSE 8080

CMD java -jar -Dspring.profiles.active=prod app.jar