package com.databaseexam.neo4jmicroservice.interfaces

import com.databaseexam.neo4jmicroservice.enums.Operation
import com.databaseexam.neo4jmicroservice.nodes.Course
import com.databaseexam.neo4jmicroservice.nodes.Difficulty
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity

interface DataLayer<T> {

    fun getAll(): List<T>

    fun createOne(courseDTO: T): ResponseEntity<HttpStatus>

    fun readOne(id: String): T

    fun updateOne(courseDTO: T): ResponseEntity<HttpStatus>

    fun deleteOne(id: String): ResponseEntity<HttpStatus>

    // fun filter(tags: List<String>, level: String, price: Int, operator: Operation): List<T>
}