package com.oliverloenning.redis.dtos.mongodb;

public class MongoDBMeta {
    private String origin;

    public MongoDBMeta() {
    }

    public MongoDBMeta(String origin) {
        this.origin = origin;
    }

    public String getOrigin() {
        return origin;
    }

    public void setOrigin(String origin) {
        this.origin = origin;
    }
}
