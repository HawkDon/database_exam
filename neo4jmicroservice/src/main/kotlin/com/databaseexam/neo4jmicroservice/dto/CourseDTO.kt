package com.databaseexam.neo4jmicroservice.dto

import com.databaseexam.neo4jmicroservice.nodes.*

class CourseDTO(
        val course: Course,
        val difficulty: Difficulty,
        val institutions: MutableList<Institution> = mutableListOf(),
        val instructors: MutableList<Instructor> = mutableListOf(),
        val tags: MutableList<Subject> = mutableListOf()
) {
}