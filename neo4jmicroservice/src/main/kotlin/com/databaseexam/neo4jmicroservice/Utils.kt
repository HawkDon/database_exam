package com.databaseexam.neo4jmicroservice

import com.databaseexam.neo4jmicroservice.dto.CourseDTO
import com.databaseexam.neo4jmicroservice.nodes.*
import org.neo4j.driver.Record
import org.neo4j.driver.Result
import org.neo4j.driver.Value
import java.io.File
import java.io.FileOutputStream
import java.io.IOException


fun getAllSubjects(records: MutableList<Array<String>>) = records.map {
    val subjects: MutableList<String> = it[5].split(", ") as MutableList<String>
    if(subjects[subjects.size - 1].substring(0..3) == "and ") {
        subjects[subjects.size - 1] = subjects[subjects.size - 1].substring(4);
    }
    subjects
}.flatten().toSet()

fun query(query: String) = driver.session().run(query)


fun getListOfCourses(result: Result): MutableList<Course> {
    val courses = mutableListOf<Course>()
    while(result.hasNext()) {
        val course = result.next()[0].asMap().toCourse()
        courses.add(course)
    }
    return courses
}

fun MutableMap<String, Any>.toCourse(): Course {
    val course = Course(
            id = this["id"] as String,
            name = this["name"] as String,
            participants = this["participants"].toString().toInt(),
            audited = this["audited"].toString().toInt(),
            date = this["date"] as String,
            price = this["price"].toString().toInt()
    )
    return course
}

fun MutableMap<String, Any>.toDifficulty(): Difficulty {
    val course = Difficulty(
            name = this["name"] as String
    )
    return course
}

fun <T> Value.toDTOList(cb: (MutableMap<String, Any>) -> T): MutableList<T> {
    val listOfDTOs = mutableListOf<T>()
    for (i in 0 until this.size()) {
        val result = this[i].asMap()
        val dto = cb(result)
        listOfDTOs.add(dto as T)
    }
    return listOfDTOs
}

fun Record.createCourseDTO(): CourseDTO {
    return CourseDTO(
            this[0].asMap().toCourse(),
            this[4].asMap().toDifficulty(),
            instructors = this[1].toDTOList() {Instructor(it["id"] as String, it["name"] as String)},
            institutions = this[2].toDTOList() { Institution(it["id"] as String, it["name"] as String) },
            tags = this[3].toDTOList { Subject(it["name"] as String) }
    )
}

fun getResourceAsFile(resourcePath: String?): File? {
    return try {
        val `in` = ClassLoader.getSystemClassLoader().getResourceAsStream(resourcePath) ?: return null
        val tempFile: File = File.createTempFile(`in`.hashCode().toString(), ".tmp")
        tempFile.deleteOnExit()
        FileOutputStream(tempFile).use { out ->
            //copy stream
            val buffer = ByteArray(1024)
            var bytesRead: Int
            while (`in`.read(buffer).also { bytesRead = it } != -1) {
                out.write(buffer, 0, bytesRead)
            }
        }
        tempFile
    } catch (e: IOException) {
        e.printStackTrace()
        null
    }
}