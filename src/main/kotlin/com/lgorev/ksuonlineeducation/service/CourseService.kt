package com.lgorev.ksuonlineeducation.service

import com.lgorev.ksuonlineeducation.domain.common.PageResponseModel
import com.lgorev.ksuonlineeducation.domain.common.map
import com.lgorev.ksuonlineeducation.domain.course.CourseRequestModel
import com.lgorev.ksuonlineeducation.domain.course.CourseRequestPageModel
import com.lgorev.ksuonlineeducation.domain.course.CourseResponseModel
import com.lgorev.ksuonlineeducation.exception.BadRequestException
import com.lgorev.ksuonlineeducation.exception.NotFoundException
import com.lgorev.ksuonlineeducation.repository.course.*
import com.lgorev.ksuonlineeducation.util.getUserId
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.data.repository.findByIdOrNull
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Propagation
import org.springframework.transaction.annotation.Transactional
import java.security.Principal
import java.util.*

@Service
@Transactional
class CourseService(private val courseRepository: CourseRepository) {

    @Autowired
    private lateinit var educationProgramService: EducationProgramService

    @Autowired
    private lateinit var courseSubscriptionService: CourseSubscriptionService

    @Autowired
    private lateinit var courseReviewService: CourseReviewService

    @Autowired
    private lateinit var coursesTeachersService: CoursesTeachersService

    @Throws(NotFoundException::class)
    fun getCourseById(id: UUID, userId: UUID?): CourseResponseModel {
        val courseEntity = courseRepository.findByIdOrNull(id)
        if (courseEntity != null) {
            val model = courseEntity.toModel()
            val educationProgram = educationProgramService.getEducationProgramById(courseEntity.educationProgramId)
            val courseRating = courseReviewService.getCourseRating(model.id)
            val subscriptions = courseSubscriptionService.getByUserId(userId, model.id)
            model.rating = courseRating?.rating
            model.ratingCount = courseRating?.count
            model.educationProgram = educationProgram
            model.hasSubscription = subscriptions != null
            return model
        }
        throw NotFoundException("Курс не найден")
    }

    fun getCoursePage(model: CourseRequestPageModel): PageResponseModel<CourseResponseModel> {
        // получение списка курсов, на которые подписан пользователь
        val subscribers = courseSubscriptionService.getListByUserId(model.userId)
        model.ids = subscribers?.map { it.id.courseId }?.toMutableSet()

        // для проверки подписан ли данный пользователь на курс
        val subscriptions = courseSubscriptionService.getListByUserId(model.subscriberId)

        // фильтрация по имени
        if (model.nameFilter != null) {
            val educationPrograms = educationProgramService.getListByName(model.nameFilter)
            model.educationProgramIds = educationPrograms.map { it.id }.toMutableSet()
        }
        val courses = courseRepository.findPage(model)
        val ids = courses.content.map { it.educationProgramId }.toMutableSet()
        val courseIds = courses.content.map { it.id }.toMutableSet()
        val educationPrograms = educationProgramService.getEducationProgramsByIds(ids)
        val ratings = courseReviewService.getCoursesRating(courseIds)
        val result = courses.map { it.toModel() }

        result.content.forEach { c ->
            c.educationProgram = educationPrograms
                    .find { ed -> ed.id == c.educationProgramId }
            val rating = ratings.find { r -> r.courseId == c.id }
            c.rating = rating?.rating ?: 0.0
            c.ratingCount = rating?.count ?: 0
            model.subscriberId?.let { userId ->
                val courseSubscription = CourseSubscriptionEntity(CourseSubscriptionId(c.id, userId))
                if (subscriptions != null) c.hasSubscription = subscriptions.contains(courseSubscription)
            }
        }
        return result
    }

    fun existCourseById(id: UUID) = courseRepository.existsById(id)

    @Throws(NotFoundException::class, BadRequestException::class)
    fun addCourse(model: CourseRequestModel, principal: Principal): CourseResponseModel {
        if (model.endDate.isBefore(model.startDate))
            throw BadRequestException("Период обучени задан некоретно")
        if (!educationProgramService.existEducationProgramById(model.educationProgramId))
            throw NotFoundException("Программа обучения не найдена")
        val userId = getUserId(principal)

        return courseRepository.save(model.toEntity(userId)).toModel()
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

    fun getSubjectId(id: UUID) = courseRepository.getSubjectIdById(id)

    fun getCourseIdListForByTeacherId(id: UUID): List<CourseResponseModel> {
        val coursesTeachers = coursesTeachersService.getCoursesTeachersByTeacherId(id)
        val courseIds = coursesTeachers.map { it.courseId }
        val courses = courseRepository.findAllById(courseIds).map { it.toModel() }
        val ids = courses.map { it.educationProgramId }.toMutableSet()
        val educationPrograms = educationProgramService.getEducationProgramsByIds(ids)
        courses.forEach { c -> c.educationProgram = educationPrograms.find { ed -> ed.id == c.educationProgramId } }
        return courses
    }

    fun setImageId(courseId: UUID, imageId: UUID) {
        courseRepository.findByIdOrNull(courseId)?.let { it.imageId = imageId }
    }
}

private fun CourseRequestModel.toEntity(creatorId: UUID?) =
        CourseEntity(id, educationProgramId, status, startDate, endDate, creationDate, isActual, creatorId, null)

private fun CourseEntity.toModel() =
        CourseResponseModel(
                id,
                educationProgramId,
                null,
                status,
                startDate,
                endDate,
                creationDate,
                isActual,
                "/api/files/course/open?id=${id}",
                creatorId = creatorId,
                imageId = imageId
        )


