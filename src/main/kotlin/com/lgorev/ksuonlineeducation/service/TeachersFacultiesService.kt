package com.lgorev.ksuonlineeducation.service

import com.lgorev.ksuonlineeducation.domain.faculty.TeachersFacultiesListModel
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
import java.util.*

@Service
@Transactional(isolation = Isolation.READ_UNCOMMITTED)
class TeachersFacultiesService(private val teachersFacultiesRepository: TeachersFacultiesRepository) {
    @Autowired
    private lateinit var facultyService: FacultyService

    @Autowired
    private lateinit var userService: UserService

    @Throws(NotFoundException::class)
    fun addTeacherToFaculty(model: TeachersFacultiesModel) {
        if (!facultyService.existFacultyById(model.facultyId))
            throw BadRequestException("Факультет не найден")
        if (!userService.existsTeacherById(model.teacherId))
            throw BadRequestException("Преподаватель не найден")
        teachersFacultiesRepository.saveAndFlush(model.toEntity()).toModel()
    }

    @Throws(BadRequestException::class)
    fun removeTeacherFromFaculty(model: TeachersFacultiesModel) {
        val faculty = facultyService.getFacultyById(model.facultyId)
        if (faculty.managerId == model.teacherId)
            throw BadRequestException("Декан не может быть убран из института")
        val entity = model.toEntity()
        if (teachersFacultiesRepository.existsById(entity.teachersFacultiesId))
            teachersFacultiesRepository.delete(entity)
    }

    fun saveAll(entities: List<TeachersFacultiesEntity>) = teachersFacultiesRepository.saveAll(entities)

    fun editTeacherFromFaculty(model: TeachersFacultiesListModel, id: UUID) {
        teachersFacultiesRepository.deleteAllByTeacherId(id)
        val list = model.list.map { TeachersFacultiesEntity(TeachersFacultiesId(it.teacherId, it.facultyId)) }
        teachersFacultiesRepository.saveAll(list)
    }

    fun getTeachersFacultiesByTeacherId(id: UUID) =
            teachersFacultiesRepository.findByTeacherId(id).map { it.toModel() }.toMutableSet()
    fun getTeachersFacultiesByTeacherIds(ids: MutableSet<UUID>) =
            teachersFacultiesRepository.findByTeacherIds(ids).map { it.toModel() }.toMutableSet()
    fun getTeachersFacultiesByFacultyIds(ids: MutableSet<UUID>) =
            teachersFacultiesRepository.findByFacultyIds(ids).map { it.toModel() }.toMutableSet()
}

private fun TeachersFacultiesEntity.toModel() =
        TeachersFacultiesModel(teachersFacultiesId.teacherId, teachersFacultiesId.facultyId)

private fun TeachersFacultiesModel.toEntity() =
        TeachersFacultiesEntity(TeachersFacultiesId(teacherId, facultyId))

