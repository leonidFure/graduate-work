package com.lgorev.ksuonlineeducation.service

import com.lgorev.ksuonlineeducation.domain.coursereview.CourseReviewModel
import com.lgorev.ksuonlineeducation.exception.UniqueConstraintException
import com.lgorev.ksuonlineeducation.repository.coursereview.CourseReviewEntity
import com.lgorev.ksuonlineeducation.repository.coursereview.CourseReviewId
import com.lgorev.ksuonlineeducation.repository.coursereview.CourseReviewRepository
import javassist.NotFoundException
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import java.util.*

@Service
@Transactional
class CourseReviewService(private val courseReviewRepository: CourseReviewRepository) {

    @Autowired
    private lateinit var courseService: CourseService
    @Autowired
    private lateinit var userService: UserService

    @Throws(UniqueConstraintException::class, NotFoundException::class)
    fun addReview(model: CourseReviewModel) {
        if (!courseService.existCourseById(model.courseId))
            throw NotFoundException("Курс не найден")
        if (!userService.existUserById(model.userId))
            throw NotFoundException("Пользователь не найден")
        val id = CourseReviewId(model.courseId, model.userId)
        if (courseReviewRepository.existsById(id))
            throw UniqueConstraintException("Вы уже оценили данный курс")
        courseReviewRepository.save(model.toEntity())
    }

    fun deleteReview(courseId: UUID, userId: UUID) {
        val id = CourseReviewId(courseId, userId)
        if (courseReviewRepository.existsById(id))
            courseReviewRepository.deleteById(id)
    }

    fun getCourseRating(id: UUID) = courseReviewRepository.findAllByCourseId(id)

    fun getCoursesRating(ids: MutableSet<UUID>) = courseReviewRepository.findAllByCourseIds(ids)
}

private fun CourseReviewModel.toEntity() =
        CourseReviewEntity(
                CourseReviewId(courseId, userId),
                rating,
                commentHead,
                commentBody
        )