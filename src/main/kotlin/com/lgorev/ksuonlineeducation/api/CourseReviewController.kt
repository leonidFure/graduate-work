package com.lgorev.ksuonlineeducation.api

import com.lgorev.ksuonlineeducation.domain.coursereview.CourseReviewModel
import com.lgorev.ksuonlineeducation.service.CourseReviewService
import com.lgorev.ksuonlineeducation.util.getUserId
import org.springframework.security.access.prepost.PreAuthorize
import org.springframework.web.bind.annotation.*
import java.security.Principal
import java.util.*

@RestController
@RequestMapping("api/course-review")
class CourseReviewController(private val courseReviewService: CourseReviewService) {

    @PostMapping
    @PreAuthorize("hasAuthority('STUDENT') or hasAuthority('ADMIN')")
    fun add(@RequestBody model: CourseReviewModel, principal: Principal) {
        val userId = getUserId(principal)
        if (userId != null) model.userId = userId
        courseReviewService.addReview(model)
    }

    @GetMapping("list")
    @PreAuthorize("isAuthenticated()")
    fun getListByCourseId(@RequestParam id: UUID) = courseReviewService.getRatings(id)

    @DeleteMapping
    @PreAuthorize("hasAuthority('STUDENT')")
    fun delete(@RequestParam courseId: UUID, principal: Principal) {
        val userId = getUserId(principal)
        if (userId != null) courseReviewService.deleteReview(courseId, userId)
    }
}