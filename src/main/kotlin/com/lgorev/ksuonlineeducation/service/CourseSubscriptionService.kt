package com.lgorev.ksuonlineeducation.service

import com.lgorev.ksuonlineeducation.domain.course.CourseSubscriptionModel
import com.lgorev.ksuonlineeducation.exception.UniqueConstraintException
import com.lgorev.ksuonlineeducation.repository.course.CourseSubscriptionEntity
import com.lgorev.ksuonlineeducation.repository.course.CourseSubscriptionId
import com.lgorev.ksuonlineeducation.repository.course.CourseSubscriptionRepository
import javassist.NotFoundException
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import java.util.*

@Service
@Transactional
class CourseSubscriptionService(private val courseSubscriptionRepository: CourseSubscriptionRepository) {

    @Autowired
    private lateinit var courseService: CourseService

    @Autowired
    private lateinit var userService: UserService

    @Throws(NotFoundException::class, UniqueConstraintException::class)
    fun subscribeUserOnCourse(model: CourseSubscriptionModel) {
        model.userId?.let { userId ->
            if (!courseService.existCourseById(model.courseId))
                throw NotFoundException("Курс не найден")
            if (!userService.existUserById(userId))
                throw NotFoundException("Пользователь не найден")
            val id = CourseSubscriptionId(model.courseId, userId)
            if (courseSubscriptionRepository.existsById(id))
                throw UniqueConstraintException("Пользователь уже подписан на курс")
            courseSubscriptionRepository.save(model.toEntity())
        }
    }


    fun unsubscribeUserFromCourse(model: CourseSubscriptionModel) {
        model.userId?.let { userId ->
            val id = CourseSubscriptionId(model.courseId, userId)
            if (courseSubscriptionRepository.existsById(id))
                courseSubscriptionRepository.deleteById(id)
        }
    }

    fun getListByUserId(id: UUID?): MutableSet<CourseSubscriptionEntity>? {
        if (id == null) return null
        return courseSubscriptionRepository.findListByUserId(id)
    }

    fun getByUserId(id: UUID?, courseId: UUID): CourseSubscriptionEntity? {
        if(id == null) return null
        return courseSubscriptionRepository.findByUserId(id, courseId)
    }

    fun getByCourseId(courseId: UUID) = courseSubscriptionRepository.findByCourseId(courseId)
}

private fun CourseSubscriptionModel.toEntity() = CourseSubscriptionEntity(CourseSubscriptionId(courseId, userId))
