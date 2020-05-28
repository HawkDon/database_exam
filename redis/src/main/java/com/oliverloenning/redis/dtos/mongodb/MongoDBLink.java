package com.oliverloenning.redis.dtos.mongodb;

public class MongoDBLink {
    private String href;
    private String rel;
    private String params;
    private String type;

    public MongoDBLink() {
    }

    public MongoDBLink(String href, String rel, String params, String type) {
        this.href = href;
        this.rel = rel;
        this.params = params;
        this.type = type;
    }

    public String getHref() {
        return href;
    }

    public void setHref(String href) {
        this.href = href;
    }

    public String getRel() {
        return rel;
    }

    public void setRel(String rel) {
        this.rel = rel;
    }

    public String getParams() {
        return params;
    }

    public void setParams(String params) {
        this.params = params;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }
}