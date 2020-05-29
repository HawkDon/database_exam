package com.oliverloenning.redis.dtos.neo4j;


public class Neo4jInstructor {
    private String id;
    private String name;

    public Neo4jInstructor() {
    }

    public Neo4jInstructor(String id, String name) {
        this.id = id;
        this.name = name;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}