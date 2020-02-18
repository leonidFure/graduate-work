package com.lgorev.ksuonlineeducation.service

import com.lgorev.ksuonlineeducation.domain.faculty.FacultyPageRequestModel
import com.lgorev.ksuonlineeducation.domain.faculty.FacultyResponseModel
import com.lgorev.ksuonlineeducation.domain.faculty.FacultyRequestModel
import com.lgorev.ksuonlineeducation.domain.faculty.TeachersFacultiesModel
import com.lgorev.ksuonlineeducation.exception.NotFoundException
import com.lgorev.ksuonlineeducation.exception.UniqueConstraintException
import com.lgorev.ksuonlineeducation.repository.faculty.*
import com.lgorev.ksuonlineeducation.repository.teacher.TeacherRepository
import org.springframework.data.domain.Page
import org.springframework.data.domain.PageRequest
import org.springframework.data.repository.findByIdOrNull
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import java.util.*

@Service
@Transactional
class FacultyService(private val facultyRepository: FacultyRepository,
                     private val teacherRepository: TeacherRepository,
                     private val teachersFacultiesRepository: TeachersFacultiesRepository) {

    @Throws(UniqueConstraintException::class, NotFoundException::class)
    fun addFaculty(model: FacultyRequestModel): FacultyResponseModel {
        if (facultyRepository.existsByName(model.name))
            throw UniqueConstraintException(message = "Факультет ${model.name} уже существует")

        if (!teacherRepository.existsById(model.managerId))
            throw NotFoundException("Преподаватель не найден")

        if (facultyRepository.existsByManagerId(model.managerId))
            throw UniqueConstraintException(message = "За данным преподавателем уже закреплен факультет")

        return facultyRepository.save(model.toEntity()).toModel()
    }

    @Throws(UniqueConstraintException::class, NotFoundException::class)
    fun updateFaculty(model: FacultyRequestModel): FacultyResponseModel {
        facultyRepository.findByName(model.name)?.let { faculty ->
            if (faculty.id != model.id)
                throw UniqueConstraintException(message = "Факультет ${model.name} уже существует")
        }

        if (!teacherRepository.existsById(model.managerId))
            throw NotFoundException(message = "Преподаватель не найден")

        facultyRepository.findByManagerId(model.managerId)?.let { faculty ->
            if (faculty.id != model.id)
                throw UniqueConstraintException(message = "За данным преподавателем уже закреплен факультет")
        }

        facultyRepository.findByIdOrNull(model.id)?.let { faculty ->
            faculty.name = model.name
            faculty.description = model.description
            faculty.managerId = model.managerId
            return faculty.toModel()
        }
        throw NotFoundException(message = "Факультет не найден")
    }

    @Throws(NotFoundException::class)
    fun getFacultyById(id: UUID): FacultyResponseModel {
        facultyRepository.findByIdOrNull(id)?.let { return it.toModel() }
        throw NotFoundException("Факультет не найден")
    }

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

    fun deleteFaculty(id: UUID) = facultyRepository.deleteById(id)

    @Throws(NotFoundException::class)
    fun addTeacherToFaculty(model: TeachersFacultiesModel) {
        if (!facultyRepository.existsById(model.facultyId))
            throw NotFoundException("Факультет не найден")
        if (!teacherRepository.existsById(model.teacherId))
            throw NotFoundException("Преподватель не найден")
        teachersFacultiesRepository.save(model.toEntity()).toModel()
    }

    fun removeTeacherFromFaculty(model: TeachersFacultiesModel) =
            teachersFacultiesRepository.delete(model.toEntity())
}

private fun FacultyEntity.toModel() =
        FacultyResponseModel(id, name, description, manager?.toModel())

private fun FacultyRequestModel.toEntity() =
        FacultyEntity(id, name, description, managerId)

private fun TeachersFacultiesEntity.toModel() =
        TeachersFacultiesModel(teachersFacultiesId.teacherId, teachersFacultiesId.facultyId)

private fun TeachersFacultiesModel.toEntity() =
        TeachersFacultiesEntity(TeachersFacultiesId(teacherId, facultyId))

