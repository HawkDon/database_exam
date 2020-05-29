package com.oliverloenning.redis.dtos;

import com.oliverloenning.redis.dtos.neo4j.Neo4jInstitution;
import com.oliverloenning.redis.dtos.neo4j.Neo4jInstructor;
import com.oliverloenning.redis.enums.Operation;

import java.util.ArrayList;
import java.util.List;

public class RedisCourse {
    private String id;
    private String title;
    private Integer price;
    private List<String> tags;
    private String level;
    private String url;
    private Integer participants = 0;
    private List<Neo4jInstructor> instructors = new ArrayList<>();
    private List<Neo4jInstitution> institutions = new ArrayList<>();
    private Operation database;

    public Operation getDatabase() {
        return database;
    }

    public void setDatabase(Operation database) {
        this.database = database;
    }

    public Integer getParticipants() {
        return participants;
    }

    public void setParticipants(Integer participants) {
        this.participants = participants;
    }

    public List<Neo4jInstructor> getInstructors() {
        return instructors;
    }

    public void setInstructors(List<Neo4jInstructor> instructors) {
        this.instructors = instructors;
    }

    public List<Neo4jInstitution> getInstitutions() {
        return institutions;
    }

    public void setInstitutions(List<Neo4jInstitution> institutions) {
        this.institutions = institutions;
    }

    public String getLevel() {
        return level;
    }

    public void setLevel(String level) {
        this.level = level;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public Integer getPrice() {
        return price;
    }

    public void setPrice(Integer price) {
        this.price = price;
    }

    public List<String> getTags() {
        return tags;
    }

    public void setTags(List<String> tags) {
        this.tags = tags;
    }

    public RedisCourse() {
    }

    public RedisCourse(String id, String title, Integer price, List<String> tags, String level, Operation database) {
        this.id = id;
        this.title = title;
        this.price = price;
        this.tags = tags;
        this.level = level;
        this.database = database;
    }

    public RedisCourse(String title, Integer price, List<String> tags, String level) {
        this.title = title;
        this.price = price;
        this.tags = tags;
        this.level = level;
    }

}
