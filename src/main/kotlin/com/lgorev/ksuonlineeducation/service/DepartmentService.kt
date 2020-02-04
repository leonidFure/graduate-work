package com.lgorev.ksuonlineeducation.service

import com.lgorev.ksuonlineeducation.domain.departments.DepartmentPageRequestModel
import com.lgorev.ksuonlineeducation.domain.departments.DepartmentResponseModel
import com.lgorev.ksuonlineeducation.domain.departments.DepartmentRequestModel
import com.lgorev.ksuonlineeducation.exception.NotFoundException
import com.lgorev.ksuonlineeducation.exception.UniqueConstraintException
import com.lgorev.ksuonlineeducation.repository.department.DepartmentEntity
import com.lgorev.ksuonlineeducation.repository.department.DepartmentRepository
import com.lgorev.ksuonlineeducation.repository.faculty.FacultyRepository
import com.lgorev.ksuonlineeducation.repository.teacher.TeacherRepository
import org.springframework.data.domain.Page
import org.springframework.data.domain.PageRequest
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
        if (departmentRepository.existsByName(model.name))
            throw UniqueConstraintException(message = "Кафедра ${model.name} уже существует")

        if (!teacherRepository.existsById(model.managerId))
            throw NotFoundException(message = "Преподаватель не найден")

        if (departmentRepository.existsByManagerId(model.managerId))
            throw UniqueConstraintException(message = "За данным преподавателем уже закремлена кафедра")

        if (!facultyRepository.existsById(model.facultyId))
            throw NotFoundException(message = "Факультет не найден")

        return departmentRepository.save(model.toEntity()).toModel()
    }

    @Throws(UniqueConstraintException::class, NotFoundException::class)
    fun updateDepartment(model: DepartmentRequestModel): DepartmentResponseModel {
        departmentRepository.findByName(model.name)?.let { department ->
            if (department.id != model.id)
                throw UniqueConstraintException(message = "Кафедра ${model.name} уже существует")
        }

        if (!teacherRepository.existsById(model.managerId))
            throw NotFoundException(message = "Преподаватель не найден")

        departmentRepository.findByManagerId(model.managerId)?.let { dep ->
            if (dep.id != model.id)
                throw UniqueConstraintException(message = "За данным преподавателем уже закреплена кафедра")
        }

        if (!facultyRepository.existsById(model.facultyId))
            throw NotFoundException(message = "Факультет не найден")

        departmentRepository.findByIdOrNull(model.id)?.let { department ->
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

    fun getDepartmentPage(model: DepartmentPageRequestModel): Page<DepartmentResponseModel> {
        val pageable = PageRequest.of(model.pageNum, model.pageSize, model.sortType, model.sortField)
        return if(model.nameFilter != null)
            departmentRepository.findAllByNameContainingIgnoreCase(pageable, model.nameFilter).map { it.toModel() }
        else
            departmentRepository.findAll(pageable).map { it.toModel() }
    }

    fun deleteDepartment(id: UUID) = departmentRepository.deleteById(id)
}

private fun DepartmentEntity.toModel() = DepartmentResponseModel(id, name, description, facultyId, manager?.toModel())


private fun DepartmentRequestModel.toEntity() = DepartmentEntity(id, name, description, facultyId, managerId)

