package com.lgorev.ksuonlineeducation.service

import com.lgorev.ksuonlineeducation.domain.faculty.TeachersFacultiesModel
import com.lgorev.ksuonlineeducation.exception.BadRequestException
import com.lgorev.ksuonlineeducation.exception.NotFoundException
import com.lgorev.ksuonlineeducation.repository.faculty.TeachersFacultiesEntity
import com.lgorev.ksuonlineeducation.repository.faculty.TeachersFacultiesId
import com.lgorev.ksuonlineeducation.repository.faculty.TeachersFacultiesRepository
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Isolation
import org.springframework.transaction.annotation.Transactional

@Service
@Transactional(isolation = Isolation.READ_UNCOMMITTED)
class TeachersFacultiesService (private val teachersFacultiesRepository: TeachersFacultiesRepository) {
    @Autowired
    private lateinit var facultyService: FacultyService
    @Autowired
    private lateinit var teacherService: TeacherService

    @Throws(NotFoundException::class)
    fun addTeacherToFaculty(model: TeachersFacultiesModel) {
        if(!facultyService.existFacultyById(model.facultyId))
            throw NotFoundException("Факультет не найден")
        if (!teacherService.existTeacherById(model.teacherId))
            throw NotFoundException("Преподватель не найден")
        teachersFacultiesRepository.saveAndFlush(model.toEntity()).toModel()
    }

    @Throws(BadRequestException::class)
    fun removeTeacherFromFaculty(model: TeachersFacultiesModel) {
        val faculty = facultyService.getFacultyById(model.facultyId)
        if(faculty.managerId == model.teacherId)
            throw BadRequestException("Декан не может быть убран из института")
        val entity = model.toEntity()
        if (teachersFacultiesRepository.existsById(entity.teachersFacultiesId))
            teachersFacultiesRepository.delete(entity)
    }
}

private fun TeachersFacultiesEntity.toModel() =
        TeachersFacultiesModel(teachersFacultiesId.teacherId, teachersFacultiesId.facultyId)

private fun TeachersFacultiesModel.toEntity() =
        TeachersFacultiesEntity(TeachersFacultiesId(teacherId, facultyId))

