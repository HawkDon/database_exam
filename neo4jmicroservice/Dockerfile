FROM openjdk:11
COPY /build/libs/neo4jmicroservice-0.0.1-SNAPSHOT.jar neo4jmicroservice.jar
COPY sleep.sh sleep.sh
RUN chmod +x sleep.sh
EXPOSE 8080
ENTRYPOINT ["./sleep.sh"]
