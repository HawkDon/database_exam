package com.oliverloenning.redis;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.oliverloenning.redis.dtos.RedisCourse;
import com.oliverloenning.redis.dtos.mongodb.MongoDBDTOResponse;
import com.oliverloenning.redis.dtos.neo4j.Neo4jCourse;
import com.oliverloenning.redis.dtos.neo4j.Neo4jCourseDTO;
import com.oliverloenning.redis.dtos.neo4j.Neo4jSubject;
import com.oliverloenning.redis.dtos.postgres.PostgresCourse;
import com.oliverloenning.redis.enums.Operation;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class Utils {

    public static String cascadeString(String title) {
     return title.replace(" ", "%20");
    }

    public static String requestResource(String resource ) throws IOException {
        URL url = new URL(resource);
        HttpURLConnection con = (HttpURLConnection) url.openConnection();
        con.setRequestMethod("GET");

        BufferedReader br = new BufferedReader(new InputStreamReader(con.getInputStream()));
        String inputLine;
        StringBuffer content = new StringBuffer();
        while ((inputLine = br.readLine()) != null) {
            content.append(inputLine);
        }
        br.close();

        return content.toString();
    }

    public static Integer sendResource(String resource, String method, byte[] body) throws IOException {
        URL url = new URL(resource);
        HttpURLConnection con = (HttpURLConnection) url.openConnection();
        con.setRequestMethod(method);
        if(method.equals("POST") | method.equals("PUT")) {
            con.setRequestProperty("Accept", "application/json");
            con.setRequestProperty("Content-Type", "application/json; charset=UTF-8");
            con.setFixedLengthStreamingMode(body.length);
            con.setDoOutput(true);
        }
        try(OutputStream os = con.getOutputStream()) {
            os.write(body);
        }

        return con.getResponseCode();
    }

    public static Integer deleteResource(String resource) throws IOException {
        URL url = new URL(resource);
        HttpURLConnection con = (HttpURLConnection) url.openConnection();
        con.setRequestMethod("DELETE");

        return con.getResponseCode();
    }

    public static List<RedisCourse> convertFromMongoCourseToRedisCourseList(MongoDBDTOResponse mongoDBCourses) {
        return mongoDBCourses.getData().stream().map(mongoDBCourse -> {
            RedisCourse redisCourse = new RedisCourse(mongoDBCourse.get_id(), mongoDBCourse.getName(), mongoDBCourse.getPrice(), mongoDBCourse.getTags(), mongoDBCourse.getLevel(), Operation.MONGODB);
            redisCourse.setUrl(mongoDBCourse.getUrl());
            return redisCourse;
        }).collect(Collectors.toList());
    }

    public static List<RedisCourse> convertFromPostgresCourseToRedisCourseList (List<PostgresCourse> postgresCourses) {
        return postgresCourses.stream().map(postgresCourse ->
        {
            RedisCourse redisCourse = new RedisCourse(Integer.toString(postgresCourse.getId()), postgresCourse.getTitle(), postgresCourse.getPrice(), postgresCourse.getTags(), postgresCourse.getLevel(), Operation.POSTGRESQL);
            redisCourse.setUrl(postgresCourse.getUrl());
            redisCourse.setParticipants(postgresCourse.getNumberSubscribers());
            return redisCourse;
        }).collect(Collectors.toList());
    }

    public static List<RedisCourse> convertFromNeo4jCourseToRedisCourseList (List<Neo4jCourseDTO> neo4jCourses) {
        return neo4jCourses.stream().map(neo4jCourse -> {
                    RedisCourse redisCourse = new RedisCourse(neo4jCourse.getCourse().getId(), neo4jCourse.getCourse().getName(), neo4jCourse.getCourse().getPrice(), neo4jCourse.getTags().stream().map(Neo4jSubject::getName).collect(Collectors.toList()), neo4jCourse.getDifficulty().getName(), Operation.NEO4J);
                    redisCourse.setParticipants(neo4jCourse.getCourse().getParticipants());
                    redisCourse.setInstitutions(neo4jCourse.getInstitutions());
                    redisCourse.setInstructors(neo4jCourse.getInstructors());
                    return redisCourse;
        }).collect(Collectors.toList());
    }

    public static ResponseEntity<HttpStatus> sendStatusCode(Integer statusCode) {
        switch (statusCode) {
            case 200: { // OK
                return new ResponseEntity<HttpStatus>(HttpStatus.OK);
            }
            case 201: { // CREATED
                return new ResponseEntity<HttpStatus>(HttpStatus.CREATED);
            }
            default: { // EVERYTHING ELSE
                return new ResponseEntity<HttpStatus>(HttpStatus.BAD_REQUEST);
            }
        }
    }

}
