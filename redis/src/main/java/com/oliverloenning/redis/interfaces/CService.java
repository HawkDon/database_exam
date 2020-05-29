package com.oliverloenning.redis.interfaces;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.oliverloenning.redis.dtos.RedisCourse;
import com.oliverloenning.redis.enums.Operation;
import com.oliverloenning.redis.enums.Operator;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.io.IOException;
import java.util.List;

public interface CService {

    public List<RedisCourse> getAllCourses() throws IOException;
    public ResponseEntity<HttpStatus> createCourse(RedisCourse course) throws IOException;
    public ResponseEntity<HttpStatus> updateCourse(RedisCourse course) throws IOException;
    public ResponseEntity<HttpStatus> deleteCourse(String id, Operation database) throws IOException;
    public List<RedisCourse> getFilteredRedisCourses(List<String> tags, String level, Integer price, Operator operator) throws IOException;

}
