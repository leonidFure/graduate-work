package com.lgorev.ksuonlineeducation.service

import com.lgorev.ksuonlineeducation.domain.common.PageResponseModel
import com.lgorev.ksuonlineeducation.domain.common.map
import com.lgorev.ksuonlineeducation.domain.educationprogram.EducationProgramRequestModel
import com.lgorev.ksuonlineeducation.domain.educationprogram.EducationProgramRequestPageModel
import com.lgorev.ksuonlineeducation.domain.educationprogram.EducationProgramResponseModel
import com.lgorev.ksuonlineeducation.exception.BadRequestException
import com.lgorev.ksuonlineeducation.exception.UniqueConstraintException
import com.lgorev.ksuonlineeducation.repository.educationprogram.EducationProgramEntity
import com.lgorev.ksuonlineeducation.repository.educationprogram.EducationProgramRepository
import javassist.NotFoundException
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.data.domain.Page
import org.springframework.data.repository.findByIdOrNull
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import java.util.*

@Service
@Transactional
class EducationProgramService(private val educationProgramRepository: EducationProgramRepository) {

    @Autowired
    private lateinit var subjectService: SubjectService

    @Autowired
    private lateinit var teachersEducationProgramService: TeachersEducationProgramsService

    @Throws(NotFoundException::class)
    fun getEducationProgramById(id: UUID): EducationProgramResponseModel {
        educationProgramRepository.findByIdOrNull(id)?.let { return it.toModel() }
        throw NotFoundException("Программа обучения не найдена")
    }

    fun getEducationProgramsByIds(ids: MutableSet<UUID>) = educationProgramRepository.findAllByIdIn(ids).map { it.toModel() }

    fun existEducationProgramById(id: UUID) = educationProgramRepository.existsById(id)

    @Throws(BadRequestException::class)
    fun getPage(model: EducationProgramRequestPageModel): PageResponseModel<EducationProgramResponseModel> {
        if (model.creationDateFrom != null && model.creationDateTo != null && model.creationDateFrom.isBefore(model.creationDateTo))
            throw BadRequestException("Промежуток задан некоректно")
        if(model.teacherId != null) {
            val teachersEducationPrograms = teachersEducationProgramService.getTeachersEducationProgramsByTeacherId(model.teacherId)
            model.ids = teachersEducationPrograms.map { it.educationProgramId }
        }
        return educationProgramRepository.findPage(model).map { it.toModel() }
    }

    @Throws(NotFoundException::class, UniqueConstraintException::class)
    fun addEducationProgram(model: EducationProgramRequestModel): EducationProgramResponseModel {
        if (educationProgramRepository.existsByName(model.name))
            throw UniqueConstraintException("Программа обучения ${model.name} уже существует")

        if (!subjectService.existSubjectById(model.subjectId))
            throw NotFoundException("Предмет не найден")

        return educationProgramRepository.save(model.toEntity()).toModel()
    }

    @Throws(NotFoundException::class, UniqueConstraintException::class)
    fun updateEducationProgram(model: EducationProgramRequestModel): EducationProgramResponseModel {
        if (educationProgramRepository.existsByName(model.name))
            throw UniqueConstraintException("Программа обучения ${model.name} уже существует")

        if (!subjectService.existSubjectById(model.subjectId))
            throw NotFoundException("Предмет не найден")

        educationProgramRepository.findByIdOrNull(model.id)?.let { program ->
            program.subjectId = model.subjectId
            program.name = model.name
            program.description = model.description
            program.creationDate = model.creationDate
            program.status = model.status
            program.isActual = model.isActual
            return program.toModel()
        }
        throw NotFoundException("Программа обучения не найдена")
    }

    fun deleteEducationProgram(id: UUID) {
        if (educationProgramRepository.existsById(id))
            educationProgramRepository.deleteById(id)
    }

    fun getListByName(name: String) = educationProgramRepository.findAllByNameContainingIgnoreCase(name)
}

private fun EducationProgramEntity.toModel() =
        EducationProgramResponseModel(id, subjectId, name, description, creationDate, status, isActual)

private fun EducationProgramRequestModel.toEntity() =
        EducationProgramEntity(id, subjectId, name, description, creationDate, status, isActual)