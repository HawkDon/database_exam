package com.oliverloenning.redis.dtos.neo4j;

public class Neo4jSubject {
    private String name;

    public Neo4jSubject() {
    }

    public Neo4jSubject(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}