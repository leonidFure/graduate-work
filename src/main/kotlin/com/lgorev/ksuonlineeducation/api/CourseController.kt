package com.lgorev.ksuonlineeducation.api

import com.lgorev.ksuonlineeducation.domain.course.*
import com.lgorev.ksuonlineeducation.service.CourseService
import com.lgorev.ksuonlineeducation.service.CourseSubscriptionService
import com.lgorev.ksuonlineeducation.service.CoursesTeachersService
import com.lgorev.ksuonlineeducation.util.getUserId
import org.springframework.http.ResponseEntity
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
    fun getById(@RequestParam id: UUID, principal: Principal): ResponseEntity<CourseResponseModel> {
        val userId = getUserId(principal)
        return ok(courseService.getCourseById(id, userId))
    }

    @PostMapping("page")
    @PreAuthorize("isAuthenticated()")
    fun getPage(@RequestBody model: CourseRequestPageModel, principal: Principal): ResponseEntity<*> {
        val userId = getUserId(principal)
        if (model.isSelf)
            model.userId = userId
        model.subscriberId = userId
        return ok(courseService.getCoursePage(model))
    }

    @PostMapping
    @PreAuthorize("hasAuthority('ADMIN') or hasAuthority('TEACHER')")
    fun add(@RequestBody model: CourseRequestModel, principal: Principal) = ok(courseService.addCourse(model, principal))

    @PutMapping
    @PreAuthorize("hasAuthority('ADMIN') or hasAuthority('TEACHER')")
    fun update(@RequestBody model: CourseRequestModel) = ok(courseService.updateCourse(model))

    @DeleteMapping
    @PreAuthorize("hasAuthority('ADMIN') or hasAuthority('TEACHER')")
    fun delete(@RequestParam id: UUID) = ok(courseService.deleteCourse(id))

    @PostMapping("teacher/add")
    @PreAuthorize("hasAuthority('ADMIN') or hasAuthority('TEACHER')")
    fun addTeacherToCourse(@RequestBody model: CoursesTeachersModel) =
            ok(coursesTeachersService.addTeacherToCourse(model))

    @PostMapping("teacher/remove")
    @PreAuthorize("hasAuthority('ADMIN') or hasAuthority('TEACHER')")
    fun removeTeacherFromCourse(@RequestBody model: CoursesTeachersModel) =
            ok(coursesTeachersService.removeTeacherFromCourse(model))

    @PostMapping("subscribe")
    @PreAuthorize("hasAuthority('STUDENT') or hasAuthority('ADMIN')")
    fun subscribe(@RequestBody model: CourseSubscriptionModel, principal: Principal) {
        val userId = getUserId(principal)
        if (userId != null)
            model.userId = userId
        courseSubscriptionService.subscribeUserOnCourse(model)
    }

    @PostMapping("unsubscribe")
    @PreAuthorize("hasAuthority('STUDENT') or hasAuthority('ADMIN')")
    fun unsubscribe(@RequestBody model: CourseSubscriptionModel, principal: Principal) {
        val userId = getUserId(principal)
        if (userId != null)
            model.userId = userId
        courseSubscriptionService.unsubscribeUserFromCourse(model)
    }

    @GetMapping("teacher")
    @PreAuthorize("isAuthenticated()")
    fun getCourseIdListForByTeacherId(@RequestParam id: UUID) = courseService.getCourseIdListForByTeacherId(id)
}