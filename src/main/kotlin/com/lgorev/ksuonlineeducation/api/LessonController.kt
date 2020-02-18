package com.lgorev.ksuonlineeducation.api

import com.lgorev.ksuonlineeducation.domain.lesson.LessonLogPageRequestModel
import com.lgorev.ksuonlineeducation.domain.lesson.LessonRequestModel
import com.lgorev.ksuonlineeducation.domain.lesson.LessonRequestPageModel
import com.lgorev.ksuonlineeducation.service.LessonLogService
import com.lgorev.ksuonlineeducation.service.LessonService
import org.springframework.http.ResponseEntity.ok
import org.springframework.security.access.prepost.PreAuthorize
import org.springframework.web.bind.annotation.*
import java.util.*

@RestController
@RequestMapping("api/lessons")
class LessonController(private val lessonService: LessonService,
                       private val lessonLogService: LessonLogService) {
    @GetMapping
    @PreAuthorize("isAuthenticated()")
    fun getById(@RequestParam id: UUID) = ok(lessonService.getLessonById(id))

    @PostMapping("page")
    @PreAuthorize("isAuthenticated()")
    fun getPage(@RequestBody model: LessonRequestPageModel) = ok(lessonService.getLessonPage(model))

    @PostMapping
    @PreAuthorize("hasAuthority('ADMIN') or hasAuthority('TEACHER')")
    fun add(@RequestBody model: LessonRequestModel) = ok(lessonService.addLesson(model))

    @PutMapping
    @PreAuthorize("hasAuthority('ADMIN') or hasAuthority('TEACHER')")
    fun update(@RequestBody model: LessonRequestModel) = ok(lessonService.updateLesson(model))

    @DeleteMapping
    @PreAuthorize("hasAuthority('ADMIN') or hasAuthority('TEACHER')")
    fun delete(@RequestParam id: UUID) = ok(lessonService.deleteLesson(id))

    @PostMapping("logs/page")
    @PreAuthorize("hasAuthority('ADMIN') or hasAuthority('TEACHER')")
    fun getPage(@RequestBody model: LessonLogPageRequestModel) = ok(lessonLogService.getLessonLogPage(model))

}