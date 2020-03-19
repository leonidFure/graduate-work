package com.lgorev.ksuonlineeducation.service

import com.lgorev.ksuonlineeducation.domain.course.CoursesTeachersRequestModel
import com.lgorev.ksuonlineeducation.exception.BadRequestException
import com.lgorev.ksuonlineeducation.exception.NotFoundException
import com.lgorev.ksuonlineeducation.repository.course.CoursesTeachersEntity
import com.lgorev.ksuonlineeducation.repository.course.CoursesTeachersId
import com.lgorev.ksuonlineeducation.repository.course.CoursesTeachersRepository
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Isolation
import org.springframework.transaction.annotation.Transactional

@Service
@Transactional(isolation = Isolation.READ_UNCOMMITTED)
class CoursesTeachersService(private val coursesTeachersRepository: CoursesTeachersRepository) {

    @Autowired
    private lateinit var teacherService: TeacherService
    @Autowired
    private lateinit var courseService: CourseService

    @Throws(NotFoundException::class, BadRequestException::class)
    fun addTeacherToCourse(model: CoursesTeachersRequestModel) {
        if (!courseService.existCourseById(model.courseId))
            throw NotFoundException("Курс не найден")
        if (!teacherService.existTeacherById(model.teacherId))
            throw NotFoundException("Преподаватель не найден не найден")
        coursesTeachersRepository.save(model.toEntity())
    }

    fun removeTeacherFromCourse(model: CoursesTeachersRequestModel) {
        val entity = model.toEntity()
        if (coursesTeachersRepository.existsById(entity.coursesTeachersId)) {
            coursesTeachersRepository.delete(entity)
        }
    }
}

private fun CoursesTeachersRequestModel.toEntity() = CoursesTeachersEntity(CoursesTeachersId(courseId, teacherId))
