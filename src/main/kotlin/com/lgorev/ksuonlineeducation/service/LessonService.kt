package com.lgorev.ksuonlineeducation.service

import com.lgorev.ksuonlineeducation.domain.lesson.*
import com.lgorev.ksuonlineeducation.exception.BadRequestException
import com.lgorev.ksuonlineeducation.exception.NotFoundException
import com.lgorev.ksuonlineeducation.repository.course.CourseRepository
import com.lgorev.ksuonlineeducation.repository.lesson.LessonEntity
import com.lgorev.ksuonlineeducation.repository.lesson.LessonRepository
import com.lgorev.ksuonlineeducation.repository.timetable.TimetableRepository
import org.springframework.data.repository.findByIdOrNull
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import java.time.LocalDateTime
import java.util.*

@Service
@Transactional
class LessonService(private val lessonRepository: LessonRepository,
                    private val lessonLogService: LessonLogService,
                    private val courseRepository: CourseRepository,
                    private val timetableRepository: TimetableRepository) {

    @Throws(NotFoundException::class)
    fun getLessonById(id: UUID): LessonResponseModel {
        lessonRepository.findByIdOrNull(id)?.let { return it.toModel() }
        throw NotFoundException("Занятие не найдено")
    }

    fun getLessonPage(model: LessonRequestPageModel) = lessonRepository.findLessonPage(model)

    @Throws(NotFoundException::class, BadRequestException::class)
    fun addLesson(model: LessonRequestModel): LessonResponseModel {
        if (!courseRepository.existsById(model.courseId))
            throw NotFoundException("Курс не найден")

        val timetable = timetableRepository.findByIdOrNull(model.timetableId)
        if (timetable != null) {
            if (timetable.courseId != model.courseId)
                throw BadRequestException("Курс занятия и расписания не совпадают")
        } else {
            throw NotFoundException("Расписание не найдено")
        }

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

private fun LessonRequestModel.toEntity() = LessonEntity(id, courseId, timetableId, date, status)
private fun LessonEntity.toModel() = LessonResponseModel(id, courseId, timetableId, date, status)
