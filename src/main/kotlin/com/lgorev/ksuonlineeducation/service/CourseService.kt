package com.lgorev.ksuonlineeducation.service

import com.lgorev.ksuonlineeducation.domain.course.CourseRequestModel
import com.lgorev.ksuonlineeducation.domain.course.CourseRequestPageModel
import com.lgorev.ksuonlineeducation.domain.course.CourseResponseModel
import com.lgorev.ksuonlineeducation.exception.BadRequestException
import com.lgorev.ksuonlineeducation.exception.NotFoundException
import com.lgorev.ksuonlineeducation.repository.course.*
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.data.repository.findByIdOrNull
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import java.util.*

@Service
@Transactional
class CourseService(private val courseRepository: CourseRepository) {

    @Autowired
    private lateinit var educationProgramService: EducationProgramService

    @Throws(NotFoundException::class)
    fun getCourseById(id: UUID): CourseResponseModel {
        courseRepository.findByIdOrNull(id)?.let { return it.toModel() }
        throw NotFoundException("Курс не найден")
    }

    fun getCoursePage(model: CourseRequestPageModel) =
            courseRepository.findPage(model).map { it.toModel() }

    fun existCourseById(id: UUID) = courseRepository.existsById(id)

    @Throws(NotFoundException::class, BadRequestException::class)
    fun addCourse(model: CourseRequestModel): CourseResponseModel {
        if (model.endDate.isBefore(model.startDate))
            throw BadRequestException("Период обучени задан некоретно")
        if (!educationProgramService.existEducationProgramById(model.educationProgramId))
            throw NotFoundException("Программа обучения не найдена")
        return courseRepository.save(model.toEntity()).toModel()
    }

    @Throws(NotFoundException::class, BadRequestException::class)
    fun updateCourse(model: CourseRequestModel): CourseResponseModel {
        if (model.endDate.isBefore(model.startDate))
            throw BadRequestException("Период обучени задан некоретно")
        if (!educationProgramService.existEducationProgramById(model.educationProgramId))
            throw NotFoundException("Программа обучения не найдена")
        courseRepository.findByIdOrNull(model.id)?.let { course ->
            course.status = model.status
            course.startDate = model.startDate
            course.endDate = model.endDate
            course.isActual = model.isActual
            return course.toModel()
        }
        throw NotFoundException("Курс не найден")
    }

    fun deleteCourse(id: UUID) {
        if (courseRepository.existsById(id))
            courseRepository.deleteById(id)
    }
}

private fun CourseRequestModel.toEntity() =
        CourseEntity(id, educationProgramId, status, startDate, endDate, creationDate, isActual)

private fun CourseEntity.toModel() =
        CourseResponseModel(id, educationProgramId, status, startDate, endDate, creationDate, isActual)


