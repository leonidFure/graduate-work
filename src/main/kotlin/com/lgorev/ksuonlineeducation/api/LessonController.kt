package com.lgorev.ksuonlineeducation.api

import com.lgorev.ksuonlineeducation.domain.lesson.LessonLogPageRequestModel
import com.lgorev.ksuonlineeducation.domain.lesson.LessonRequestModel
import com.lgorev.ksuonlineeducation.domain.lesson.LessonRequestPageModel
import com.lgorev.ksuonlineeducation.service.LessonLogService
import com.lgorev.ksuonlineeducation.service.LessonService
import org.springframework.web.bind.annotation.*
import java.util.*

@RestController
@RequestMapping("api/lessons")
class LessonController(private val lessonService: LessonService,
                       private val lessonLogService: LessonLogService) {
    @GetMapping
    fun getById(@RequestParam id: UUID) = lessonService.getLessonById(id)

    @PostMapping("page")
    fun getPage(@RequestBody model: LessonRequestPageModel) = lessonService.getLessonPage(model)

    @PostMapping
    fun add(@RequestBody model: LessonRequestModel) = lessonService.addLesson(model)

    @PutMapping
    fun update(@RequestBody model: LessonRequestModel) = lessonService.updateLesson(model)

    @DeleteMapping
    fun delete(@RequestParam id: UUID) = lessonService.deleteLesson(id)

    @PostMapping("logs/page")
    fun getPage(@RequestBody model: LessonLogPageRequestModel) = lessonLogService.getLessonLogPage(model)

}