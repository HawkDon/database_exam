FROM openjdk:11
COPY /build/libs/redis-0.0.1-SNAPSHOT.jar redis.jar
COPY sleep.sh sleep.sh
RUN chmod +x sleep.sh
EXPOSE 4000
ENTRYPOINT ["./sleep.sh"]