cd neo4jmicroservice;

./gradlew build;

cd ../redis;

./gradlew build;

cd ..;

docker-compose up --build;