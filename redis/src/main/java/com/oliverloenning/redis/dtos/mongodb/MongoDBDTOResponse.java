package com.oliverloenning.redis.dtos.mongodb;

import java.util.List;

public class MongoDBDTOResponse {
    private int status;
    private String message;
    private List<MongoDBCourse> data;
    private List<MongoDBLink> links;
    private MongoDBMeta meta;


    public MongoDBDTOResponse() {
    }

    public MongoDBDTOResponse(int status, String message, List<MongoDBCourse> data, List<MongoDBLink> links, MongoDBMeta meta) {
        this.status = status;
        this.message = message;
        this.data = data;
        this.links = links;
        this.meta = meta;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public List<MongoDBCourse> getData() {
        return data;
    }

    public void setData(List<MongoDBCourse> data) {
        this.data = data;
    }

    public List<MongoDBLink> getLinks() {
        return links;
    }

    public void setLinks(List<MongoDBLink> links) {
        this.links = links;
    }

    public MongoDBMeta getMeta() {
        return meta;
    }

    public void setMeta(MongoDBMeta meta) {
        this.meta = meta;
    }
}
