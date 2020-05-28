package com.databaseexam.neo4jmicroservice.rest

import com.databaseexam.neo4jmicroservice.data.DataCourse
import com.databaseexam.neo4jmicroservice.dto.CourseDTO
import com.databaseexam.neo4jmicroservice.enums.Operation
import com.databaseexam.neo4jmicroservice.nodes.Course
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*

@RestController
class RestCalls(private val dataCourse: DataCourse) {

    @GetMapping("/courses")
    fun getAll(): List<CourseDTO> = dataCourse.getAll()

    @PostMapping("/course")
    fun createOne(@RequestBody course: CourseDTO) = dataCourse.createOne(course)

    @GetMapping("/course")
    fun readOne(@RequestParam("id") id: String) = dataCourse.readOne(id)

    @PutMapping("/course")
    fun updateOne(@RequestBody course: CourseDTO) = dataCourse.updateOne(course)

    @DeleteMapping("/course")
    fun deleteOne(@RequestParam("id") id: String) = dataCourse.deleteOne(id)

    @GetMapping("/courses/filter")
    fun filterCourses(@RequestParam("tags") tags: List<String>?, @RequestParam("difficulty") level: String?, @RequestParam("price") price: Int?, @RequestParam("operation") operator: Operation?): List<CourseDTO> = dataCourse.filter(tags, level, price, operator)
}