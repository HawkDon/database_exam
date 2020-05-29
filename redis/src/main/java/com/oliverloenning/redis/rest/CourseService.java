package com.oliverloenning.redis.rest;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.oliverloenning.redis.Constants;
import com.oliverloenning.redis.Utils;
import com.oliverloenning.redis.dtos.RedisCourse;
import com.oliverloenning.redis.dtos.mongodb.MongoDBDTOResponse;
import com.oliverloenning.redis.dtos.neo4j.*;
import com.oliverloenning.redis.dtos.postgres.PostgresCourse;
import com.oliverloenning.redis.enums.Operation;
import com.oliverloenning.redis.interfaces.CService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Random;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@RestController("/service")
public class CourseService implements CService {

    private ObjectMapper om = new ObjectMapper().configure(DeserializationFeature.ACCEPT_SINGLE_VALUE_AS_ARRAY, true);

    @GetMapping("/courses")
    @Override
    public List<RedisCourse> getAllCourses() throws IOException {
        String neo4jJson = Utils.requestResource(Constants.NEO4J_RESOURCE + "/courses");
        String mongodbJson = Utils.requestResource(Constants.MONGODB_RESOURCE + "/api/v1/courses");
        String postgresQLJson = Utils.requestResource(Constants.POSTGRESQL_RESOURCE + "/courses");
        List<Neo4jCourseDTO> neo4jCourses = om.readValue(neo4jJson, new TypeReference<List<Neo4jCourseDTO>>(){});
        MongoDBDTOResponse mongoDBCourses = om.readValue(mongodbJson, MongoDBDTOResponse.class);
        List<PostgresCourse> postgresCourses = om.readValue(postgresQLJson, new TypeReference<List<PostgresCourse>>(){});
        List<RedisCourse> pCourses = Utils.convertFromPostgresCourseToRedisCourseList(postgresCourses);
        List<RedisCourse> mCourses = Utils.convertFromMongoCourseToRedisCourseList(mongoDBCourses);
        List<RedisCourse> nCourses = Utils.convertFromNeo4jCourseToRedisCourseList(neo4jCourses);
        return Stream.of(mCourses, pCourses, nCourses).flatMap(Collection::stream).collect(Collectors.toList());
    }

    @PostMapping("/course")
    @Override
    public ResponseEntity<HttpStatus> createCourse(@RequestBody RedisCourse course) throws IOException {
        switch(course.getDatabase()) {
            case NEO4J: {
                Neo4jCourse neo4jCourse = new Neo4jCourse("-1",
                        course.getTitle(),
                        course.getParticipants(),
                        0,
                        "20/20/2000",
                        new Random().nextInt(100)
                        );
                Neo4jDifficulty diff = new Neo4jDifficulty(course.getLevel());
                List<Neo4jInstitution> institutions = course.getInstitutions().stream().map(neo4jInstitution -> new Neo4jInstitution(neo4jInstitution.getId(), neo4jInstitution.getName())).collect(Collectors.toList());
                List<Neo4jInstructor> instructors = course.getInstructors().stream().map(neo4jInstructor -> new Neo4jInstructor(neo4jInstructor.getId(), neo4jInstructor.getName())).collect(Collectors.toList());

                List<Neo4jSubject> tags = course.getTags().stream().map(Neo4jSubject::new).collect(Collectors.toList());
                Neo4jCourseDTO neo4jCourseDTO = new Neo4jCourseDTO(neo4jCourse, diff, institutions, instructors, tags);
                Integer statusCode = Utils.sendResource(Constants.NEO4J_RESOURCE + "/course", "POST", om.writeValueAsBytes(neo4jCourseDTO));
                if(statusCode == 201) {  // created
                    return new ResponseEntity<HttpStatus>(HttpStatus.CREATED);
                }
                return new ResponseEntity<HttpStatus>(HttpStatus.BAD_REQUEST);
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

    @Override
    public ResponseEntity<HttpStatus> updateCourse(RedisCourse course) {
        return null;
    }

    @Override
    public ResponseEntity<HttpStatus> deleteCourse(String id) {
        return null;
    }
}
