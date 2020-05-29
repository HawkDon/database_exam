package com.oliverloenning.redis.dtos.neo4j;

public class Neo4jDifficulty {
    private String name;

    public Neo4jDifficulty() {
    }

    public Neo4jDifficulty(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}