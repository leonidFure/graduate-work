package com.lgorev.ksuonlineeducation.service

import com.lgorev.ksuonlineeducation.domain.departments.DepartmentResponseModel
import com.lgorev.ksuonlineeducation.domain.departments.DepartmentRequestModel
import com.lgorev.ksuonlineeducation.exception.NotFoundException
import com.lgorev.ksuonlineeducation.exception.UniqueConstraintException
import com.lgorev.ksuonlineeducation.repository.department.DepartmentEntity
import com.lgorev.ksuonlineeducation.repository.department.DepartmentRepository
import com.lgorev.ksuonlineeducation.repository.faculty.FacultyRepository
import com.lgorev.ksuonlineeducation.repository.teacher.TeacherRepository
import org.springframework.data.domain.Pageable
import org.springframework.data.repository.findByIdOrNull
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import java.util.*

@Service
@Transactional
class DepartmentService(private val departmentRepository: DepartmentRepository,
                        private val teacherRepository: TeacherRepository,
                        private val facultyRepository: FacultyRepository) {

    @Throws(UniqueConstraintException::class)
    fun addDepartment(model: DepartmentRequestModel): DepartmentResponseModel {
        departmentRepository.findByName(model.name)?.let {
            throw UniqueConstraintException("Кафедра ${model.name} уже существует")
        }
        return departmentRepository.save(model.toEntity()).toModel()
    }

    @Throws(UniqueConstraintException::class, NotFoundException::class)
    fun updateDepartment(model: DepartmentRequestModel): DepartmentResponseModel {
        departmentRepository.findByName(model.name)?.let { department ->
            if (department.id != model.id)
                throw UniqueConstraintException(message = "Кафедра ${model.name} уже существует")
        }
        departmentRepository.findByIdOrNull(model.id)?.let { department ->
            if (!teacherRepository.existsById(model.managerId))
                throw NotFoundException(message = "Преподаватель не найден")
            if (!facultyRepository.existsById(model.facultyId))
                throw NotFoundException(message = "Факультет не найден")

            department.name = model.name
            department.description = model.description
            department.managerId = model.managerId
            department.facultyId = model.facultyId
            return department.toModel()
        }
        throw NotFoundException(message = "Кафедра не найдена")
    }

    fun getDepartmentById(id: UUID): DepartmentResponseModel {
        departmentRepository.findByIdOrNull(id)?.let {
            return it.toModel()
        }
        throw NotFoundException("Кафедра не найдена")
    }

    fun getDepartmentPage(pageable: Pageable) = departmentRepository.findAll(pageable).map { it.toModel() }

    fun deleteDepartment(id: UUID) = departmentRepository.deleteById(id)
}

fun DepartmentEntity.toModel() = DepartmentResponseModel(id, name, description, facultyId, manager.toModel())


private fun DepartmentRequestModel.toEntity() = DepartmentEntity(id, name, description, facultyId, managerId)

