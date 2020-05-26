package com.oliverloenning.redis.rest;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.oliverloenning.redis.Constants;
import com.oliverloenning.redis.Utils;
import com.oliverloenning.redis.dtos.RedisCourse;
import com.oliverloenning.redis.dtos.mongodb.MongoDBCourse;
import com.oliverloenning.redis.dtos.neo4j.Neo4jCourse;
import com.oliverloenning.redis.dtos.postgres.PostgresCourse;
import com.oliverloenning.redis.enums.Operation;
import com.oliverloenning.redis.interfaces.CService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import java.io.IOException;
import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@RestController("/service")
public class CourseService implements CService {

    private ObjectMapper om = new ObjectMapper();

    @GetMapping("/courses")
    @Override
    public List<RedisCourse> getAllCourses() throws IOException {
        String neo4jJson = Utils.requestResource(Constants.NEO4J_RESOURCE + "/courses");
        String mongodbJson = Utils.requestResource(Constants.MONGODB_RESOURCE + "/courses");
        String postgresQLJson = Utils.requestResource(Constants.POSTGRESQL_RESOURCE + "/courses");
        List<Neo4jCourse> neo4jCourses = om.readValue(neo4jJson, new TypeReference<List<Neo4jCourse>>(){});
        List<MongoDBCourse> mongoDBCourses = om.readValue(mongodbJson, new TypeReference<List<MongoDBCourse>>(){});
        List<PostgresCourse> postgresCourses = om.readValue(postgresQLJson, new TypeReference<List<PostgresCourse>>(){});
        List<RedisCourse> pCourses = postgresCourses.stream().map(postgresCourse -> new RedisCourse(Integer.toString(postgresCourse.getId()), postgresCourse.getTitle(), postgresCourse.getPrice())).collect(Collectors.toList());
        List<RedisCourse> mCourses = mongoDBCourses.stream().map(mongoDBCourse -> new RedisCourse(mongoDBCourse.get_id(), mongoDBCourse.getName(), mongoDBCourse.getPrice())).collect(Collectors.toList());
        List<RedisCourse> nCourses = neo4jCourses.stream().map(neo4jCourse -> new RedisCourse(neo4jCourse.getId(), neo4jCourse.getName(), neo4jCourse.getPrice())).collect(Collectors.toList());
        return Stream.of(mCourses, pCourses, nCourses).flatMap(Collection::stream).collect(Collectors.toList());
    }

    @Override
    public ResponseEntity<HttpStatus> createCourse(RedisCourse course, Operation operation) {
        switch(operation) {
            case NEO4J: {

            }
            case MONGODB: {

            }
            case POSTGRESQL: {

            }
            default: {
                System.out.println("Database does not exist!");
            }
        }
        return null;
    }

    @GetMapping("/course")
    @Override
    public RedisCourse readCourse(@RequestParam("id") String id) throws IOException {
        String json = Utils.requestResource(Constants.NEO4J_RESOURCE + "/course?title=" + Utils.cascadeString(id));
        Neo4jCourse c = om.readValue(json, Neo4jCourse.class);
        return null;
    }

    @Override
    public ResponseEntity<HttpStatus> updateCourse(RedisCourse course) {
        return null;
    }

    @Override
    public ResponseEntity<HttpStatus> deleteCourse(String id) {
        return null;
    }
}
