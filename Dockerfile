FROM amazoncorretto:18
RUN mkdir /app

WORKDIR /app

ADD ./api/target/delivery-time-api-1.0.0-SNAPSHOT.jar /app

EXPOSE 8080

CMD ["java", "-jar", "delivery-time-api-1.0.0-SNAPSHOT.jar"]
