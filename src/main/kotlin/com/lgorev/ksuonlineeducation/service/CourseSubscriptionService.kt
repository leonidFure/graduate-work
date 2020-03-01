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

@Service
@Transactional
class CourseSubscriptionService(private val courseSubscriptionRepository: CourseSubscriptionRepository) {

    @Autowired
    private lateinit var courseService: CourseService
    @Autowired
    private lateinit var userService: UserService

    @Throws(NotFoundException::class, UniqueConstraintException::class)
    fun subscribeUserOnCourse(model: CourseSubscriptionModel) {
        if (!courseService.existCourseById(model.courseId))
            throw NotFoundException("Курс не найден")
        if (!userService.existUserById(model.userId))
            throw NotFoundException("Пользователь не найден")
        val id = CourseSubscriptionId(model.courseId, model.userId)
        if (courseSubscriptionRepository.existsById(id))
            throw UniqueConstraintException("Пользователь уже подписан на курс")
        courseSubscriptionRepository.save(model.toEntity())
    }


    fun unsubscribeUserFromCourse(model: CourseSubscriptionModel) {
        val id = CourseSubscriptionId(model.courseId, model.userId)
        if (courseSubscriptionRepository.existsById(id))
            courseSubscriptionRepository.deleteById(id)
    }
}

private fun CourseSubscriptionModel.toEntity() = CourseSubscriptionEntity(CourseSubscriptionId(courseId, userId))
