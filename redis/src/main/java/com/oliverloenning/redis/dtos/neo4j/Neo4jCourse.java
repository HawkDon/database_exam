package com.oliverloenning.redis.dtos.neo4j;

public class Neo4jCourse {
    private String id;
    private String name;
    private Integer participants;
    private Integer audited;
    private String date;
    private Integer price;

    public Neo4jCourse() {
    }

    public Neo4jCourse(String id, String name, Integer participants, Integer audited, String date, Integer price) {
        this.id = id;
        this.name = name;
        this.participants = participants;
        this.audited = audited;
        this.date = date;
        this.price = price;
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

    public Integer getParticipants() {
        return participants;
    }

    public void setParticipants(Integer participants) {
        this.participants = participants;
    }

    public Integer getAudited() {
        return audited;
    }

    public void setAudited(Integer audited) {
        this.audited = audited;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public Integer getPrice() {
        return price;
    }

    public void setPrice(Integer price) {
        this.price = price;
    }
}
