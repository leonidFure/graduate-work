package com.lgorev.ksuonlineeducation.service

import com.lgorev.ksuonlineeducation.domain.timetable.TimetableRequestModel
import com.lgorev.ksuonlineeducation.domain.timetable.TimetableResponseModel
import com.lgorev.ksuonlineeducation.exception.BadRequestException
import com.lgorev.ksuonlineeducation.exception.NotFoundException
import com.lgorev.ksuonlineeducation.repository.course.CourseRepository
import com.lgorev.ksuonlineeducation.repository.timetable.TimetableEntity
import com.lgorev.ksuonlineeducation.repository.timetable.TimetableRepository
import org.springframework.data.repository.findByIdOrNull
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import java.util.*

@Service
@Transactional
class TimetableService(private val timetableRepository: TimetableRepository,
                       private val courseRepository: CourseRepository) {

    @Throws(NotFoundException::class)
    fun getTimetableById(id: UUID): TimetableResponseModel {
        timetableRepository.findByIdOrNull(id)?.let { return it.toModel() }
        throw NotFoundException("Расписание не найдено")
    }

    fun getTimetablesByCourseId(courseId: UUID) =
            timetableRepository.findAllByCourseId(courseId).map { it.toModel() }

    @Throws(NotFoundException::class, BadRequestException::class)
    fun addTimetable(model: TimetableRequestModel): TimetableResponseModel {
        if (courseRepository.existsById(model.courseId))
            throw NotFoundException("Курс не найден")
        if (model.startTime.isAfter(model.endTime) || model.startTime == model.endTime)
            throw BadRequestException("Некоректное время проведение занятия")
        return timetableRepository.save(model.toEntity()).toModel()
    }

    @Throws(NotFoundException::class, BadRequestException::class)
    fun updateTimetable(model: TimetableRequestModel): TimetableResponseModel {
        if (courseRepository.existsById(model.courseId))
            throw NotFoundException("Курс не найден")
        if (model.startTime.isAfter(model.endTime) || model.startTime == model.endTime)
            throw BadRequestException("Некоректное время проведение занятия")

        timetableRepository.findByIdOrNull(model.id)?.let { timetable ->
            timetable.dayOfWeek = model.dayOfWeek
            timetable.startTime = model.startTime
            timetable.endTime = model.endTime
            timetable.type = model.type
            timetable.isActual = model.isActual
            return timetable.toModel()
        }
        throw NotFoundException("Расписание не найдено")
    }

    fun deleteTimetable(id: UUID) = timetableRepository.deleteById(id)
}

private fun TimetableEntity.toModel() = TimetableResponseModel(id, courseId, dayOfWeek, startTime, endTime, type, isActual)
private fun TimetableRequestModel.toEntity() = TimetableEntity(id, courseId, dayOfWeek, startTime, endTime, type, isActual)