package com.lgorev.ksuonlineeducation.api

import com.lgorev.ksuonlineeducation.domain.course.CourseRequestModel
import com.lgorev.ksuonlineeducation.domain.course.CourseRequestPageModel
import com.lgorev.ksuonlineeducation.domain.course.CourseSubscriptionModel
import com.lgorev.ksuonlineeducation.domain.course.CoursesTeachersRequestModel
import com.lgorev.ksuonlineeducation.service.CourseService
import com.lgorev.ksuonlineeducation.service.CourseSubscriptionService
import com.lgorev.ksuonlineeducation.service.CoursesTeachersService
import com.lgorev.ksuonlineeducation.util.getUserId
import org.springframework.http.ResponseEntity.ok
import org.springframework.security.access.prepost.PreAuthorize
import org.springframework.web.bind.annotation.*
import java.security.Principal
import java.util.*

@RestController
@RequestMapping("api/courses")
class CourseController(private val courseService: CourseService,
                       private val coursesTeachersService: CoursesTeachersService,
                       private val courseSubscriptionService: CourseSubscriptionService) {

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

    @PostMapping("teacher/add")
    @PreAuthorize("hasAuthority('ADMIN') or hasAuthority('TEACHER')")
    fun addTeacherToCourse(@RequestBody model: CoursesTeachersRequestModel) =
            ok(coursesTeachersService.addTeacherToCourse(model))

    @PostMapping("teacher/remove")
    @PreAuthorize("hasAuthority('ADMIN') or hasAuthority('TEACHER')")
    fun removeTeacherFromCourse(@RequestBody model: CoursesTeachersRequestModel) =
            ok(coursesTeachersService.removeTeacherFromCourse(model))


    @PostMapping("subscribe")
    @PreAuthorize("hasAuthority('STUDENT')")
    fun subscribe(@RequestBody model: CourseSubscriptionModel, principal: Principal) {
        val userId = getUserId(principal)
        if(userId != null)
            model.userId = userId
        courseSubscriptionService.subscribeUserOnCourse(model)
    }

    @PostMapping("unsubscribe")
    @PreAuthorize("hasAuthority('STUDENT')")
    fun unsubscribe(@RequestBody model: CourseSubscriptionModel, principal: Principal) {
        val userId = getUserId(principal)
        if(userId != null)
            model.userId = userId
        courseSubscriptionService.unsubscribeUserFromCourse(model)
    }
}