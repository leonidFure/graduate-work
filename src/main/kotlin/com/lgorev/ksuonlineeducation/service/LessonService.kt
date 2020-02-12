package com.lgorev.ksuonlineeducation.service

import com.lgorev.ksuonlineeducation.domain.lesson.*
import com.lgorev.ksuonlineeducation.exception.NotFoundException
import com.lgorev.ksuonlineeducation.repository.lesson.LessonEntity
import com.lgorev.ksuonlineeducation.repository.lesson.LessonRepository
import org.springframework.data.repository.findByIdOrNull
import org.springframework.stereotype.Service
import java.time.LocalDateTime
import java.util.*

@Service
class LessonService(private val lessonRepository: LessonRepository,
                    private val lessonLogService: LessonLogService) {

    @Throws(NotFoundException::class)
    fun getLessonById(id: UUID): LessonResponseModel {
        lessonRepository.findByIdOrNull(id)?.let { return it.toModel() }
        throw NotFoundException("Занятие не найдено")
    }

    fun getLessonPage(model: LessonRequestPageModel) = lessonRepository.findLessonPage(model)

    fun addLesson(model: LessonRequestModel): LessonResponseModel {
        val lesson = lessonRepository.save(model.toEntity()).toModel()
        val lessonLog = LessonLogModel(model.id, LocalDateTime.now(), null, LessonStatus.CREATED)
        lessonLogService.addLessonLog(lessonLog)
        return lesson
    }

    fun updateLesson(model: LessonRequestModel): LessonResponseModel {
        lessonRepository.findByIdOrNull(model.id)?.let { lesson ->
            val oldStatus = lesson.status
            lesson.date = model.date
            lesson.status = model.status
            val lessonLog = LessonLogModel(model.id, LocalDateTime.now(), oldStatus, lesson.status)
            lessonLogService.addLessonLog(lessonLog)
            return lesson.toModel()
        }
        throw NotFoundException("Занятие не найдено")
    }

    fun deleteLesson(id: UUID) = lessonRepository.deleteById(id)
}

private fun LessonRequestModel.toEntity() = LessonEntity(id, timetableId, date, status)
private fun LessonEntity.toModel() = LessonResponseModel(id, timetableId, date, status)
