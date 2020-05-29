package com.oliverloenning.redis.dtos.neo4j;

import java.util.List;

public class Neo4jCourseDTO {
    private Neo4jCourse course;
    private Neo4jDifficulty difficulty;
    private List<Neo4jInstitution> institutions;
    private List<Neo4jInstructor> instructors;
    private List<Neo4jSubject> tags;

    public Neo4jCourseDTO() {
    }

    public Neo4jCourseDTO(Neo4jCourse course, Neo4jDifficulty difficulty, List<Neo4jInstitution> institutions, List<Neo4jInstructor> instructors, List<Neo4jSubject> tags) {
        this.course = course;
        this.difficulty = difficulty;
        this.institutions = institutions;
        this.instructors = instructors;
        this.tags = tags;
    }

    public Neo4jCourse getCourse() {
        return course;
    }

    public void setCourse(Neo4jCourse course) {
        this.course = course;
    }

    public Neo4jDifficulty getDifficulty() {
        return difficulty;
    }

    public void setDifficulty(Neo4jDifficulty difficulty) {
        this.difficulty = difficulty;
    }

    public List<Neo4jInstitution> getInstitutions() {
        return institutions;
    }

    public void setInstitutions(List<Neo4jInstitution> institutions) {
        this.institutions = institutions;
    }

    public List<Neo4jInstructor> getInstructors() {
        return instructors;
    }

    public void setInstructors(List<Neo4jInstructor> instructors) {
        this.instructors = instructors;
    }

    public List<Neo4jSubject> getTags() {
        return tags;
    }

    public void setTags(List<Neo4jSubject> tags) {
        this.tags = tags;
    }
}
