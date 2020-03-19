package com.lgorev.ksuonlineeducation.service

import com.lgorev.ksuonlineeducation.domain.faculty.FacultyPageRequestModel
import com.lgorev.ksuonlineeducation.domain.faculty.FacultyResponseModel
import com.lgorev.ksuonlineeducation.domain.faculty.FacultyRequestModel
import com.lgorev.ksuonlineeducation.domain.faculty.TeachersFacultiesModel
import com.lgorev.ksuonlineeducation.exception.NotFoundException
import com.lgorev.ksuonlineeducation.exception.UniqueConstraintException
import com.lgorev.ksuonlineeducation.repository.faculty.*
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.data.domain.Page
import org.springframework.data.domain.PageRequest
import org.springframework.data.repository.findByIdOrNull
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import java.util.*

@Service
@Transactional
class FacultyService(private val facultyRepository: FacultyRepository) {

    @Autowired
    private lateinit var teachersFacultiesService: TeachersFacultiesService
    @Autowired
    private lateinit var teacherService: TeacherService

    @Throws(UniqueConstraintException::class, NotFoundException::class)
    fun addFaculty(model: FacultyRequestModel): FacultyResponseModel {
        if (facultyRepository.existsByName(model.name))
            throw UniqueConstraintException(message = "Факультет ${model.name} уже существует")

        if (!teacherService.existTeacherById(model.managerId))
            throw NotFoundException(message = "Преподаватель не найден")

        if (facultyRepository.existsByManagerId(model.managerId))
            throw UniqueConstraintException(message = "За данным преподавателем уже закреплен факультет")

        val faculty = facultyRepository.saveAndFlush(model.toEntity()).toModel()
        val teachersFacultiesModel = TeachersFacultiesModel(model.managerId, faculty.id)
        teachersFacultiesService.addTeacherToFaculty(teachersFacultiesModel)
        return faculty
    }

    @Throws(UniqueConstraintException::class, NotFoundException::class)
    fun updateFaculty(model: FacultyRequestModel): FacultyResponseModel {
        facultyRepository.findByName(model.name)?.let { faculty ->
            if (faculty.id != model.id)
                throw UniqueConstraintException(message = "Факультет ${model.name} уже существует")
        }

        if (!teacherService.existTeacherById(model.managerId))
            throw NotFoundException(message = "Преподаватель не найден")

        facultyRepository.findByManagerId(model.managerId)?.let { faculty ->
            if (faculty.id != model.id)
                throw UniqueConstraintException(message = "За данным преподавателем уже закреплен факультет")
        }

        val faculty = facultyRepository.findByIdOrNull(model.id)
        if (faculty != null) {
            faculty.name = model.name
            faculty.description = model.description
            faculty.managerId = model.managerId
            teachersFacultiesService.addTeacherToFaculty(TeachersFacultiesModel(model.managerId, faculty.id))
            return faculty.toModel()
        } else throw NotFoundException(message = "Факультет не найден")
    }

    @Throws(NotFoundException::class)
    fun getFacultyById(id: UUID): FacultyResponseModel {
        facultyRepository.findByIdOrNull(id)?.let { return it.toModel() }
        throw NotFoundException("Факультет не найден")
    }

    fun existFacultyById(id: UUID) = facultyRepository.existsById(id)

    @Throws(NotFoundException::class)
    fun getFacultyByManagerId(id: UUID): FacultyResponseModel {
        facultyRepository.findByManagerId(id)?.let { return it.toModel() }
        throw NotFoundException("Факультет не найден")
    }

    fun getFacultyPage(model: FacultyPageRequestModel): Page<FacultyResponseModel> {
        val pageable = PageRequest.of(model.pageNum, model.pageSize, model.sortType, "name")
        return if (model.nameFilter != null)
            facultyRepository
                    .findAllByNameContainingIgnoreCase(pageable, model.nameFilter)
                    .map { it.toModel() }
        else
            facultyRepository.findAll(pageable).map { it.toModel() }
    }

    fun deleteFaculty(id: UUID) {
        if (facultyRepository.existsById(id))
            facultyRepository.deleteById(id)
    }
}

private fun FacultyEntity.toModel() =
        FacultyResponseModel(id, name, description, managerId)

private fun FacultyRequestModel.toEntity() =
        FacultyEntity(id, name, description, managerId)

