package com.oliverloenning.redis.dtos.mongodb;

import java.util.ArrayList;
import java.util.List;

public class MongoDBBodyDTO {
    private List<MongoDBCourse> courses = new ArrayList<>();

    public MongoDBBodyDTO() {
    }

    public MongoDBBodyDTO(List<MongoDBCourse> courses) {
        this.courses = courses;
    }

    public List<MongoDBCourse> getCourses() {
        return courses;
    }

    public void setCourses(List<MongoDBCourse> courses) {
        this.courses = courses;
    }
}
