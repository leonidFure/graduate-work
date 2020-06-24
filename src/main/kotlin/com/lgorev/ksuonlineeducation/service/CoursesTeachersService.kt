package com.lgorev.ksuonlineeducation.service

import com.lgorev.ksuonlineeducation.domain.course.CoursesTeachersModel
import com.lgorev.ksuonlineeducation.exception.BadRequestException
import com.lgorev.ksuonlineeducation.exception.NotFoundException
import com.lgorev.ksuonlineeducation.repository.course.CoursesTeachersEntity
import com.lgorev.ksuonlineeducation.repository.course.CoursesTeachersId
import com.lgorev.ksuonlineeducation.repository.course.CoursesTeachersRepository
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Isolation
import org.springframework.transaction.annotation.Transactional
import java.util.*

@Service
@Transactional(isolation = Isolation.READ_UNCOMMITTED)
class CoursesTeachersService(private val coursesTeachersRepository: CoursesTeachersRepository) {

    @Autowired
    private lateinit var courseService: CourseService

    @Autowired
    private lateinit var userService: UserService

    @Throws(NotFoundException::class, BadRequestException::class)
    fun addTeacherToCourse(model: CoursesTeachersModel) {
        if (!courseService.existCourseById(model.courseId))
            throw BadRequestException("Курс не найден")
        coursesTeachersRepository.save(model.toEntity())
    }

    fun removeTeacherFromCourse(model: CoursesTeachersModel) {
        val entity = model.toEntity()
        if (coursesTeachersRepository.existsById(entity.coursesTeachersId)) {
            coursesTeachersRepository.delete(entity)
        }
    }

    fun getCoursesTeachersByCourseId(id: UUID) = coursesTeachersRepository.findByCourseId(id).map { it.toModel() }

    fun getCoursesTeachersByTeacherId(id: UUID) = coursesTeachersRepository.findByTeacherId(id).map { it.toModel() }
}

private fun CoursesTeachersModel.toEntity() = CoursesTeachersEntity(CoursesTeachersId(courseId, teacherId))

private fun CoursesTeachersEntity.toModel() = CoursesTeachersModel(coursesTeachersId.courseId, coursesTeachersId.teacherId)


