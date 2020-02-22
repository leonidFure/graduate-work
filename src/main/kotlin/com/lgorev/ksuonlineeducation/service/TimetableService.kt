package com.lgorev.ksuonlineeducation.service

import com.lgorev.ksuonlineeducation.domain.timetable.TimetableRequestModel
import com.lgorev.ksuonlineeducation.domain.timetable.TimetableResponseModel
import com.lgorev.ksuonlineeducation.domain.timetable.TimetablesRequestModel
import com.lgorev.ksuonlineeducation.exception.BadRequestException
import com.lgorev.ksuonlineeducation.exception.NotFoundException
import com.lgorev.ksuonlineeducation.repository.timetable.TimetableEntity
import com.lgorev.ksuonlineeducation.repository.timetable.TimetableRepository
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.data.repository.findByIdOrNull
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import java.util.*

@Service
@Transactional
class TimetableService(private val timetableRepository: TimetableRepository) {

    @Autowired
    private lateinit var courseService: CourseService
    @Autowired
    private lateinit var lessonService: LessonService

    @Throws(NotFoundException::class)
    fun getTimetableById(id: UUID): TimetableResponseModel {
        timetableRepository.findByIdOrNull(id)?.let { return it.toModel() }
        throw NotFoundException("Расписание не найдено")
    }

    fun getTimetableByIdOrNull(id: UUID) = timetableRepository.findByIdOrNull(id)

    fun getTimetablesByCourseId(courseId: UUID) =
            timetableRepository.findAllByCourseId(courseId).map { it.toModel() }

    @Throws(NotFoundException::class, BadRequestException::class)
    fun addTimetable(model: TimetableRequestModel): TimetableResponseModel {
        if (courseService.existCourseById(model.courseId))
            throw NotFoundException("Курс не найден")
        if (model.startTime.isAfter(model.endTime) || model.startTime == model.endTime)
            throw BadRequestException("Некоректное время проведение занятия")
        val timetable = timetableRepository.save(model.toEntity()).toModel()
        lessonService.addLessonsForCourse(mutableListOf(timetable))
        return timetable
    }

    @Throws(BadRequestException::class)
    fun addTimetables(model: TimetablesRequestModel): List<TimetableResponseModel> {
        val hasNotValidTimetable = model.timetables.any { it.endTime.isBefore(it.startTime) }
        if (hasNotValidTimetable)
            throw BadRequestException("Период занятия задан некоректно")
        val list = timetableRepository.saveAll(model.timetables.map { it.toEntity() }).map { it.toModel() }
        lessonService.addLessonsForCourse(list)
        return list
    }

    @Throws(NotFoundException::class, BadRequestException::class)
    fun updateTimetable(model: TimetableRequestModel): TimetableResponseModel {
        if (courseService.existCourseById(model.courseId))
            throw NotFoundException("Курс не найден")
        if (model.startTime.isAfter(model.endTime) || model.startTime == model.endTime)
            throw BadRequestException("Некоректное время проведение занятия")

        val timetable = timetableRepository.findByIdOrNull(model.id)
        if (timetable != null) {
            timetable.dayOfWeek = model.dayOfWeek
            timetable.startTime = model.startTime
            timetable.endTime = model.endTime
            timetable.type = model.type
            timetable.isActual = model.isActual
            val responseModel = timetable.toModel()
            lessonService.updateLessonsForCourse(mutableListOf(responseModel))
            return responseModel
        } else throw NotFoundException("Расписание не найдено")
    }

    fun deleteTimetable(id: UUID) {
        if (timetableRepository.existsById(id))
            timetableRepository.deleteById(id)
    }
}

private fun TimetableEntity.toModel() = TimetableResponseModel(id, courseId, dayOfWeek, startTime, endTime, type, isActual)
private fun TimetableRequestModel.toEntity() = TimetableEntity(id, courseId, dayOfWeek, startTime, endTime, type, isActual)