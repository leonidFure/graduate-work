package com.lgorev.ksuonlineeducation.api

import com.lgorev.ksuonlineeducation.domain.course.CourseRequestModel
import com.lgorev.ksuonlineeducation.domain.course.CourseRequestPageModel
import com.lgorev.ksuonlineeducation.domain.educationprogram.EducationProgramRequestModel
import com.lgorev.ksuonlineeducation.domain.educationprogram.EducationProgramRequestPageModel
import com.lgorev.ksuonlineeducation.service.CourseService
import org.springframework.http.ResponseEntity.ok
import org.springframework.security.access.prepost.PreAuthorize
import org.springframework.web.bind.annotation.*
import java.util.*

@RestController
@RequestMapping("api/courses")
class CourseController(private val courseService: CourseService) {

    @GetMapping
    @PreAuthorize("isAuthenticated()")
    fun getById(@RequestParam id: UUID) = ok(courseService.getCourseById(id))

    @PostMapping("page")
    @PreAuthorize("isAuthenticated()")
    fun getPage(@RequestBody model: CourseRequestPageModel) = ok(courseService.getCoursePage(model))

    @PostMapping
    @PreAuthorize("hasAuthority('ADMIN') or hasAuthority('TEACHER')")
    fun add(@RequestBody model: CourseRequestModel) = ok(courseService.addCourse(model))

    @PutMapping
    @PreAuthorize("hasAuthority('ADMIN') or hasAuthority('TEACHER')")
    fun update(@RequestBody model: CourseRequestModel) = ok(courseService.updateCourse(model))

    @DeleteMapping
    @PreAuthorize("hasAuthority('ADMIN') or hasAuthority('TEACHER')")
    fun delete(@RequestParam id: UUID) = ok(courseService.deleteCourse(id))
}