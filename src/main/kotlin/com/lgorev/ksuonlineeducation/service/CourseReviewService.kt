package com.lgorev.ksuonlineeducation.service

import com.lgorev.ksuonlineeducation.domain.coursereview.CourseReviewModel
import com.lgorev.ksuonlineeducation.domain.user.UserResponseModel
import com.lgorev.ksuonlineeducation.exception.UniqueConstraintException
import com.lgorev.ksuonlineeducation.repository.course.CourseEntity
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
    fun getRatings(id: UUID): List<CourseReviewModel> {
        val ratings = courseReviewRepository.findAllById(id)
        val ids = ratings.map { it.courseReviewId.userId }.toMutableSet()
        val map = userService.getUsersByIds2(ids).map { it.id to it }.toMap()
        return ratings.map { it.toModel(map[it.courseReviewId.userId]) }
    }
}

private fun CourseReviewModel.toEntity() =
        CourseReviewEntity(
                CourseReviewId(courseId, userId),
                rating,
                commentHead,
                commentBody
        )

private fun CourseReviewEntity.toModel(user: UserResponseModel?) = CourseReviewModel(courseReviewId.courseId, courseReviewId.userId, rating, commentHead, commentBody, user)