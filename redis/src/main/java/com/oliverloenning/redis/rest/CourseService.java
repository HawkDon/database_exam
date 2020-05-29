package com.oliverloenning.redis.rest;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.oliverloenning.redis.Constants;
import com.oliverloenning.redis.Utils;
import com.oliverloenning.redis.dtos.RedisCourse;
import com.oliverloenning.redis.dtos.mongodb.MongoDBBodyDTO;
import com.oliverloenning.redis.dtos.mongodb.MongoDBCourse;
import com.oliverloenning.redis.dtos.mongodb.MongoDBDTOResponse;
import com.oliverloenning.redis.dtos.neo4j.*;
import com.oliverloenning.redis.dtos.postgres.PostgresCourse;
import com.oliverloenning.redis.enums.Operation;
import com.oliverloenning.redis.interfaces.CService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import javax.websocket.server.PathParam;
import java.io.IOException;
import java.util.Collection;
import java.util.List;
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
                Neo4jCourseDTO neo4jCourseDTO = course.toNeo4jCourseDTO();
                Integer statusCode = Utils.sendResource(Constants.NEO4J_RESOURCE + "/course", "POST", om.writeValueAsBytes(neo4jCourseDTO));
                return Utils.sendStatusCode(statusCode);
            }
            case MONGODB: {
                MongoDBBodyDTO body = new MongoDBBodyDTO();
                MongoDBCourse mongoDBCourse = course.toMongoDBCourse();
                body.getCourses().add(mongoDBCourse);
                Integer statusCode = Utils.sendResource(Constants.MONGODB_RESOURCE + "/courses", "POST", om.writeValueAsBytes(body));
                return Utils.sendStatusCode(statusCode);
            }
            case POSTGRESQL: {
                PostgresCourse postgresCourse = course.toPostgresCourse();
                Integer statusCode = Utils.sendResource(Constants.POSTGRESQL_RESOURCE + "/courses", "POST", om.writeValueAsBytes(postgresCourse));
                return Utils.sendStatusCode(statusCode);
            }
            default: {
                System.out.println("Database does not exist!");
                return Utils.sendStatusCode(404);
            }
        }
    }

    @PutMapping("/course")
    @Override
    public ResponseEntity<HttpStatus> updateCourse(RedisCourse course) throws IOException {
        switch(course.getDatabase()) {
            case NEO4J: {
                Neo4jCourseDTO neo4jCourseDTO = course.toNeo4jCourseDTO();
                Integer statusCode = Utils.sendResource(Constants.NEO4J_RESOURCE + "/course", "PUT", om.writeValueAsBytes(neo4jCourseDTO));
                return Utils.sendStatusCode(statusCode);
            }
            case MONGODB: {
                MongoDBCourse mongoDBCourse = course.toMongoDBCourse();
                Integer statusCode = Utils.sendResource(Constants.MONGODB_RESOURCE + "/courses/" + mongoDBCourse.get_id(), "PUT", om.writeValueAsBytes(mongoDBCourse));
                return Utils.sendStatusCode(statusCode);
            }
            case POSTGRESQL: {
                PostgresCourse postgresCourse = course.toPostgresCourse();
                Integer statusCode = Utils.sendResource(Constants.POSTGRESQL_RESOURCE + "/courses", "PUT", om.writeValueAsBytes(postgresCourse));
                return Utils.sendStatusCode(statusCode);
            }
            default: {
                System.out.println("Database does not exist!");
                return Utils.sendStatusCode(404);
            }
        }
    }

    @DeleteMapping("/course")
    @Override
    public ResponseEntity<HttpStatus> deleteCourse(@PathParam("id") String id, @PathParam("database") Operation database) throws IOException {
        switch(database) {
            case NEO4J: {
                Integer statusCode = Utils.deleteResource(Constants.NEO4J_RESOURCE + "/course?id=" + id);
                return Utils.sendStatusCode(statusCode);
            }
            case MONGODB: {
                Integer statusCode = Utils.deleteResource(Constants.MONGODB_RESOURCE + "/courses/" + id);
                return Utils.sendStatusCode(statusCode);
            }
            case POSTGRESQL: {
                Integer statusCode = Utils.deleteResource(Constants.POSTGRESQL_RESOURCE + "/courses/" + Integer.parseInt(id));
                return Utils.sendStatusCode(statusCode);
            }
            default: {
                System.out.println("Database does not exist!");
                return Utils.sendStatusCode(404);
            }
        }
    }
}
