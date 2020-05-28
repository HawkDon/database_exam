package com.oliverloenning.redis.dtos;

import java.util.List;

public class RedisCourse {
    private String id;
    private String title;
    private Integer price;
    private List<String> tags;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public Integer getPrice() {
        return price;
    }

    public void setPrice(Integer price) {
        this.price = price;
    }

    public List<String> getTags() {
        return tags;
    }

    public void setTags(List<String> tags) {
        this.tags = tags;
    }

    public RedisCourse(String id, String title, Integer price) {
        this.id = id;
        this.title = title;
        this.price = price;
    }

    public RedisCourse(String title, Integer price) {
        this.title = title;
        this.price = price;
    }

}
