package com.lgorev.ksuonlineeducation.service

import com.lgorev.ksuonlineeducation.domain.educationprogram.EducationProgramRequestModel
import com.lgorev.ksuonlineeducation.domain.educationprogram.EducationProgramRequestPageModel
import com.lgorev.ksuonlineeducation.domain.educationprogram.EducationProgramResponseModel
import com.lgorev.ksuonlineeducation.exception.BadRequestException
import com.lgorev.ksuonlineeducation.exception.UniqueConstraintException
import com.lgorev.ksuonlineeducation.repository.educationprogram.EducationProgramEntity
import com.lgorev.ksuonlineeducation.repository.educationprogram.EducationProgramRepository
import com.lgorev.ksuonlineeducation.repository.subject.SubjectRepository
import com.lgorev.ksuonlineeducation.repository.trainingdirection.TrainingDirectionRepository
import javassist.NotFoundException
import org.springframework.data.domain.Page
import org.springframework.data.repository.findByIdOrNull
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import java.util.*

@Service
@Transactional
class EducationProgramService(private val educationProgramRepository: EducationProgramRepository,
                              private val trainingDirectionRepository: TrainingDirectionRepository,
                              private val subjectRepository: SubjectRepository) {

    @Throws(NotFoundException::class)
    fun getEducationProgramById(id: UUID): EducationProgramResponseModel {
        educationProgramRepository.findByIdOrNull(id)?.let { return it.toModel() }
        throw NotFoundException("Программа обучения не найдена")
    }

    @Throws(BadRequestException::class)
    fun getPage(model: EducationProgramRequestPageModel): Page<EducationProgramResponseModel> {
        if (model.creationDateFrom != null && model.creationDateTo != null && model.creationDateFrom.isBefore(model.creationDateTo))
            throw BadRequestException("Промежуток задан некоректно")
        return educationProgramRepository.findPage(model).map { it.toModel() }
    }

    @Throws(NotFoundException::class, UniqueConstraintException::class)
    fun addEducationProgram(model: EducationProgramRequestModel): EducationProgramResponseModel {
        if (educationProgramRepository.existsByName(model.name))
            throw UniqueConstraintException("Программа обучения ${model.name} уже существует")

        if (!subjectRepository.existsById(model.subjectId))
            throw NotFoundException("Предмет не найден")

        return educationProgramRepository.save(model.toEntity()).toModel()
    }

    @Throws(NotFoundException::class, UniqueConstraintException::class)
    fun updateEducationProgram(model: EducationProgramRequestModel): EducationProgramResponseModel {
        if (educationProgramRepository.existsByName(model.name))
            throw UniqueConstraintException("Программа обучения ${model.name} уже существует")

        if (!subjectRepository.existsById(model.subjectId))
            throw NotFoundException("Предмет не найден")

        educationProgramRepository.findByIdOrNull(model.id)?.let { program ->
            program.code = model.code
            program.subjectId = model.subjectId
            program.name = model.name
            program.description = model.description
            program.creationDate = model.creationDate
            program.isActual = model.isActual
            return program.toModel()
        }
        throw NotFoundException("Программа обучения не найдена")
    }

    fun deleteEducationProgram(id: UUID) = educationProgramRepository.deleteById(id)
}

private fun EducationProgramEntity.toModel() =
        EducationProgramResponseModel(id, code, subjectId, name, description, creationDate, status, isActual)

private fun EducationProgramRequestModel.toEntity() =
        EducationProgramEntity(id, subjectId, code, name, description, creationDate, status)