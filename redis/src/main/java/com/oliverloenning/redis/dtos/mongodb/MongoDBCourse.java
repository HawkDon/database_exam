package com.oliverloenning.redis.dtos.mongodb;

import java.util.ArrayList;
import java.util.List;

public class MongoDBCourse {
    public String _id;
    public String name;
    public String url;
    public Integer price;
    public String level;
    public List<String> Tags = new ArrayList<>();

    public MongoDBCourse() {
    }

    public MongoDBCourse(String _id, String name, String url, Integer price, String level) {
        this._id = _id;
        this.name = name;
        this.url = url;
        this.price = price;
        this.level = level;
    }

    public MongoDBCourse(String _id, String name, String url, Integer price, String level, List<String> tags) {
        this._id = _id;
        this.name = name;
        this.url = url;
        this.price = price;
        this.level = level;
        this.Tags = tags;
    }

    public String get_id() {
        return _id;
    }

    public void set_id(String _id) {
        this._id = _id;
    }

    public String getName() {
        return name;
    }

    public void setName(String Name) {
        this.name = Name;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String Url) {
        this.url = Url;
    }

    public Integer getPrice() {
        return price;
    }

    public void setPrice(Integer Price) {
        this.price = Price;
    }

    public String getLevel() {
        return level;
    }

    public void setLevel(String Level) {
        this.level = Level;
    }

    public List<String> getTags() {
        return Tags;
    }

    public void setTags(List<String> Tags) {
        this.Tags = Tags;
    }
}