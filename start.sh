cd neo4jmicroservice;

./gradlew build -x test;

cd ../redis;

./gradlew build;

cd ..;

docker-compose up --build;