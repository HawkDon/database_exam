package com.oliverloenning.redis.dtos.mongodb;

import java.util.List;

public class MongoDBCourse {
    public String _id;
    public String Name;
    public String Url;
    public Integer Price;
    public String Level;
    public List<String> Tags;

    public String get_id() {
        return _id;
    }

    public void set_id(String _id) {
        this._id = _id;
    }

    public String getName() {
        return Name;
    }

    public void setName(String Name) {
        this.Name = Name;
    }

    public String getUrl() {
        return Url;
    }

    public void setUrl(String Url) {
        this.Url = Url;
    }

    public Integer getPrice() {
        return Price;
    }

    public void setPrice(Integer Price) {
        this.Price = Price;
    }

    public String getLevel() {
        return Level;
    }

    public void setLevel(String Level) {
        this.Level = Level;
    }

    public List<String> getTags() {
        return Tags;
    }

    public void setTags(List<String> Tags) {
        this.Tags = Tags;
    }
}