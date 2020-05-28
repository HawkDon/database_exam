package com.oliverloenning.redis.dtos.neo4j;

public class Neo4jInstitution {
    private String id;
    private String name;

    public Neo4jInstitution() {
    }

    public Neo4jInstitution(String id, String name) {
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