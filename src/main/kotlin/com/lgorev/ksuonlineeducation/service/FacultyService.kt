package com.lgorev.ksuonlineeducation.service

import com.lgorev.ksuonlineeducation.domain.faculty.FacultyResponseModel
import com.lgorev.ksuonlineeducation.domain.faculty.FacultyRequestModel
import com.lgorev.ksuonlineeducation.exception.NotFoundException
import com.lgorev.ksuonlineeducation.exception.UniqueConstraintException
import com.lgorev.ksuonlineeducation.repository.faculty.FacultyEntity
import com.lgorev.ksuonlineeducation.repository.faculty.FacultyRepository
import com.lgorev.ksuonlineeducation.repository.teacher.TeacherRepository
import org.springframework.data.domain.Pageable
import org.springframework.data.repository.findByIdOrNull
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import java.util.*

@Service
@Transactional
class FacultyService(private val facultyRepository: FacultyRepository,
                     private val teacherRepository: TeacherRepository) {

    @Throws(UniqueConstraintException::class)
    fun addFaculty(model: FacultyRequestModel): FacultyResponseModel {
        facultyRepository.findByName(model.name)?.let {
            throw UniqueConstraintException(message = "Факультет ${model.name} уже существует")
        }
        return facultyRepository.save(model.toEntity()).toModel()
    }

    @Throws(UniqueConstraintException::class, NotFoundException::class)
    fun updateFaculty(model: FacultyRequestModel): FacultyResponseModel {
        facultyRepository.findByName(model.name)?.let {faculty ->
            if (faculty.id != model.id)
                throw UniqueConstraintException(message = "Факультет ${model.name} уже существует")
        }
        facultyRepository.findByIdOrNull(model.id)?.let { faculty ->
            if(teacherRepository.existsById(model.managerId)) {
                faculty.name = model.name
                faculty.description = model.description
                faculty.managerId = model.managerId
                return faculty.toModel()
            } else throw NotFoundException(message = "Преподаватель не найден")
        }
        throw NotFoundException(message = "Факультет не найден")
    }

    @Throws(NotFoundException::class)
    fun getFacultyById(id: UUID): FacultyResponseModel {
        facultyRepository.findByIdOrNull(id)?.let {
            return it.toModel()
        }
        throw NotFoundException("Факультет не найден")
    }

    fun getFacultyPage(pageable: Pageable) = facultyRepository.findAll(pageable).map { it.toModel() }

    fun deleteFaculty(id: UUID) = facultyRepository.deleteById(id)

}

private fun FacultyEntity.toModel() = FacultyResponseModel(id, name, description, manager.toModel())

private fun FacultyRequestModel.toEntity() = FacultyEntity(id, name, description, managerId)

