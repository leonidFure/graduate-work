package com.lgorev.ksuonlineeducation.service

import com.lgorev.ksuonlineeducation.domain.lesson.LessonLogResponseModel
import com.lgorev.ksuonlineeducation.domain.lesson.LessonRequestModel
import com.lgorev.ksuonlineeducation.domain.lesson.LessonRequestPageModel
import com.lgorev.ksuonlineeducation.domain.lesson.LessonResponseModel
import com.lgorev.ksuonlineeducation.exception.NotFoundException
import com.lgorev.ksuonlineeducation.repository.course.CourseRepository
import com.lgorev.ksuonlineeducation.repository.lesson.LessonEntity
import com.lgorev.ksuonlineeducation.repository.lesson.LessonLogEntity
import com.lgorev.ksuonlineeducation.repository.lesson.LessonRepository
import org.springframework.data.repository.findByIdOrNull
import org.springframework.stereotype.Service
import java.util.*

@Service
class LessonService(private val lessonRepository: LessonRepository,
                    private val courseRepository: CourseRepository) {

    @Throws(NotFoundException::class)
    fun getLessonById(id: UUID): LessonResponseModel {
        lessonRepository.findByIdOrNull(id)?.let { return it.toModel() }
        throw NotFoundException("Занятие не найдено")
    }

    fun getLessonPage(model: LessonRequestPageModel) = lessonRepository.findLessonPage(model)

    fun addLesson(model: LessonRequestModel): LessonResponseModel {
        if (!courseRepository.existsById(model.courseId))
            throw NotFoundException("Курс не найден")
        return lessonRepository.save(model.toEntity()).toModel()
    }

    fun updateLesson(model: LessonRequestModel): LessonResponseModel {
        lessonRepository.findByIdOrNull(model.id)?.let { lesson ->
            lesson.date = model.date
            lesson.status = model.status
            return lesson.toModel()
        }
        throw NotFoundException("Занятие не найдено")
    }

    fun deleteLesson(id: UUID) = lessonRepository.deleteById(id)
}

private fun LessonRequestModel.toEntity() = LessonEntity(id, courseId, date, status)
private fun LessonEntity.toModel() = LessonResponseModel(id, courseId, date, status)
private fun LessonLogEntity.toModel() = LessonLogResponseModel(id, lessonId, datetime, oldStatus, newStatus)