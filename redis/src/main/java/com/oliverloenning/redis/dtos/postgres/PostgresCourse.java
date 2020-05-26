package com.oliverloenning.redis.dtos.postgres;


import java.util.List;

public class PostgresCourse {
    private int id;
    private String title;
    private String url;
    private Boolean paid;
    private Integer price;
    public Integer number_subscribers;
    public Integer number_reviews;
    public Integer number_of_lectures;
    private String duration;
    private String level;
    private List<String> tags;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public Boolean getPaid() {
        return paid;
    }

    public void setPaid(Boolean paid) {
        this.paid = paid;
    }

    public Integer getPrice() {
        return price;
    }

    public void setPrice(Integer price) {
        this.price = price;
    }

    public Integer getNumberSubscribers() {
        return number_subscribers;
    }

    public void setNumberSubscribers(Integer numberSubscribers) {
        this.number_subscribers = numberSubscribers;
    }

    public Integer getNumberReviews() {
        return number_reviews;
    }

    public void setNumberReviews(Integer numberReviews) {
        this.number_reviews = numberReviews;
    }

    public Integer getNumberOfLectures() {
        return number_of_lectures;
    }

    public void setNumberOfLectures(Integer numberOfLectures) {
        this.number_of_lectures = numberOfLectures;
    }

    public String getDuration() {
        return duration;
    }

    public void setDuration(String duration) {
        this.duration = duration;
    }

    public String getLevel() {
        return level;
    }

    public void setLevel(String level) {
        this.level = level;
    }

    public List<String> getTags() {
        return tags;
    }

    public void setTags(List<String> tags) {
        this.tags = tags;
    }
}