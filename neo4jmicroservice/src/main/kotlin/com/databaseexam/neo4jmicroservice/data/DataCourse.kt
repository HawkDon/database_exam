package com.databaseexam.neo4jmicroservice.data

import com.databaseexam.neo4jmicroservice.*
import com.databaseexam.neo4jmicroservice.dto.CourseDTO
import com.databaseexam.neo4jmicroservice.enums.Operation
import com.databaseexam.neo4jmicroservice.interfaces.DataLayer
import com.databaseexam.neo4jmicroservice.nodes.*
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.stereotype.Component
import java.lang.Exception
import java.util.*


@Component
class DataCourse  : DataLayer<CourseDTO> {
    override fun getAll(): List<CourseDTO> {
        val result = query("""
        MATCH (course:Course)-[:INSTRUCTED_BY]-(instructor:Instructor)
        WITH course as course, collect(instructor) AS instructors
        MATCH (course)-[:COURSE_AT]->(institution:Institution)
        WITH course, instructors, collect(institution) AS institutions
        MATCH (course)-[:SUBJECT_OF]->(subject:Subject)
        WITH course, instructors, institutions, collect(subject) as subjects
        MATCH (course)-[:HAS_LEVEL]->(difficulty:Difficulty)
        return course, instructors, institutions, subjects, difficulty
        """.trimIndent())

        val courses = mutableListOf<CourseDTO>()
        while(result.hasNext()) {
            val record = result.next()
            val courseDTO = record.createCourseDTO()
            courses.add(courseDTO)
        }
        return courses
    }


    override fun createOne(courseDTO: CourseDTO): ResponseEntity<HttpStatus> {
        val result = query("""CREATE (c:Course {id: "${UUID.randomUUID()}",
                |name: "${courseDTO.course.name}", 
                |participants: "${courseDTO.course.participants}",
                |audited: "${courseDTO.course.audited}",
                |date: "${courseDTO.course.date}",
                |price: "${courseDTO.course.price}"}) return c""".trimMargin())

        val course = result.next()[0].asMap().toCourse()

        for (instructor in courseDTO.instructors) {
            query("""MATCH (c:Course {id: "${course.id}"})
            |MERGE c-[:INSTRUCTED_BY]->(i:Instructor {id: "${instructor.id}"})
        """.trimMargin())
        }
        for (institution in courseDTO.institutions) {
            query("""MATCH (c:Course {id: "${course.id}"})
            |MERGE c-[:COURSE_AT]->(i:institution {id: "${institution.id}"})
        """.trimMargin())
        }

        for (subject in courseDTO.tags) {
            query("""MATCH (c:Course {id: "${course.id}"})
            |MERGE c-[:SUBJECT_OF]->(s:Subject {name: "${subject.name}"})
        """.trimMargin())
        }

        query("""MATCH (c:Course {id: "${course.id}"})
            |MERGE c-[:HAS_LEVEL]->(d:Difficulty {name: "${courseDTO.difficulty.name}"})
        """.trimMargin())
        return ResponseEntity(HttpStatus.CREATED)
    }

    override fun readOne(id: String): CourseDTO {
        val result = query("""
        MATCH (course:Course {id: "$id" })-[:INSTRUCTED_BY]-(instructor:Instructor)
        WITH course as course, collect(instructor) AS instructors
        MATCH (course)-[:COURSE_AT]->(institution:Institution)
        WITH course, instructors, collect(institution) AS institutions
        MATCH (course)-[:SUBJECT_OF]->(subject:Subject)
        WITH course, instructors, institutions, collect(subject) as subjects
        MATCH (course)-[:HAS_LEVEL]->(difficulty:Difficulty)
        return course, instructors, institutions, subjects, difficulty
        """.trimIndent())
        val record = result.next()
        val courseDTO = record.createCourseDTO()
        return courseDTO
    }

    override fun updateOne(courseDTO: CourseDTO): ResponseEntity<HttpStatus> {
        try {
            query("""
                |MATCH (c:Course {id: "${courseDTO.course.id}" })
                |SET c.name = "${courseDTO.course.name}"
                |SET c.participants = "${courseDTO.course.participants}"
                |SET c.audited = "${courseDTO.course.audited}"
                |SET c.date = "${courseDTO.course.date}"
                |SET c.price = "${courseDTO.course.price}"
            """.trimMargin())

            query("""
                |MATCH (c:Course {id: "${courseDTO.course.id}" })-[:HAS_LEVEL]->(d:Difficulty)
                |SET d.name = "${courseDTO.difficulty.name}"
            """.trimMargin())

            for (instructor in courseDTO.instructors) {
                query("""
                |MATCH (:Course {id: "${courseDTO.course.id}" })-[:INSTRUCTED_BY]->(i:Instructor {id: "${instructor.id}"})
                |SET i.name = "${instructor.name}"
            """.trimMargin())
            }

            for (institution in courseDTO.institutions) {
                query("""
                |MATCH (:Course {id: "${courseDTO.course.id}" })-[:COURSE_AT]->(i:Institution {id: "${institution.id}"})
                |SET i.name = "${institution.name}"
            """.trimMargin())
            }

            for (subject in courseDTO.tags) {
                query("""
                |MATCH (:Course {id: "${courseDTO.course.id}" })-[:SUBJECT_OF]->(s:Subject {name: "${subject.name}"})
                |SET i.name = "${subject.name}"
            """.trimMargin())
            }
        } catch (e: Exception) {
            return ResponseEntity(HttpStatus.BAD_REQUEST)
        }
        return ResponseEntity(HttpStatus.OK)
    }

    override fun deleteOne(id: String): ResponseEntity<HttpStatus> {
        try {
            query("""MATCH (c:Course)-[rel]->() WHERE c.id = "$id" DELETE c, rel""")
        } catch (e: Exception) {
            return ResponseEntity(HttpStatus.BAD_REQUEST)
        }
        return ResponseEntity(HttpStatus.OK)
    }

    fun filter(tags: List<String>?, level: String?, price: Int?, operator: Operation?): List<CourseDTO> {
        var query = "MATCH (course:Course)-[:SUBJECT_OF]->(subject:Subject) " +
                "WITH course, subject " +
                "MATCH (course)-[:HAS_LEVEL]->(difficulty:Difficulty) " +
                "WHERE "

        tags?.run {
            val tagsQuery = this.map { "\"$it\"" }
            query += "subject.name IN $tagsQuery "
        }

        level?.run {
            query += if(query.substring(query.length - 6) == "WHERE ") {
                "difficulty.name = \"$this\" "
            } else {
                "AND difficulty.name = \"$this\" "
            }
        }
        price?.run {
            if(operator != null) {
                when(operator) {
                    Operation.greaterThan -> {
                        query += if(query.substring(query.length - 6) == "WHERE ") {
                            "course.price > $this "
                        } else {
                            "AND course.price > $this "
                        }
                    }
                    Operation.lessThan -> {
                        query += if(query.substring(query.length - 6) == "WHERE ") {
                            "course.price < $this "
                        } else {
                            "AND course.price < $this "
                        }
                    }
                }
            } else {
                query += if(query.substring(query.length - 6) == "WHERE ") {
                    "course.price = $this "
                } else {
                    "AND course.price = $this "
                }
            }

        }
        val result = query(query + "MATCH (course)-[:INSTRUCTED_BY]-(instructor:Instructor)\n" +
                "WITH course, collect(instructor) AS instructors\n" +
                "MATCH (course)-[:COURSE_AT]->(institution:Institution)\n" +
                "WITH course, instructors, collect(institution) AS institutions\n" +
                "MATCH (course)-[:SUBJECT_OF]->(subject:Subject)\n" +
                "WITH course, instructors, institutions, collect(subject) as subjects\n" +
                "MATCH (course)-[:HAS_LEVEL]->(difficulty:Difficulty)\n" +
                "return course, instructors, institutions, subjects, difficulty")

        val courses = mutableListOf<CourseDTO>()
        while(result.hasNext()) {
            val record = result.next()
            val courseDTO = record.createCourseDTO()
            courses.add(courseDTO)
        }
        return courses
    }
}

/*
MATCH (course:Course)-[:SUBJECT_OF]->(subject:Subject)
WITH course, subject
MATCH (course)-[:HAS_LEVEL]->(difficulty:Difficulty)
WHERE subject.name IN ["Government"]
AND
difficulty.name = "Expert"
AND
course.price < 40
return course
*/