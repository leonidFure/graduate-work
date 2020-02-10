package com.lgorev.ksuonlineeducation.api

import com.lgorev.ksuonlineeducation.domain.course.CourseRequestModel
import com.lgorev.ksuonlineeducation.domain.course.CourseRequestPageModel
import com.lgorev.ksuonlineeducation.domain.educationprogram.EducationProgramRequestModel
import com.lgorev.ksuonlineeducation.domain.educationprogram.EducationProgramRequestPageModel
import com.lgorev.ksuonlineeducation.service.CourseService
import org.springframework.http.ResponseEntity.ok
import org.springframework.web.bind.annotation.*
import java.util.*

@RestController
@RequestMapping("api/courses")
class CourseController (private val courseService: CourseService) {

    @GetMapping
    fun getById(@RequestParam id: UUID) = ok(courseService.getCourseById(id))

    @PostMapping("page")
    fun getPage(@RequestBody model: CourseRequestPageModel) = ok(courseService.getCoursePage(model))

    @PostMapping
    fun add(@RequestBody model: CourseRequestModel) = ok(courseService.addCourse(model))

    @PutMapping
    fun update(@RequestBody model: CourseRequestModel) = ok(courseService.updateCourse(model))

    @DeleteMapping
    fun delete(@RequestParam id: UUID) = ok(courseService.deleteCourse(id))
}