package com.lgorev.ksuonlineeducation.service

import com.lgorev.ksuonlineeducation.domain.trainingdirection.TrainingDirectionPageRequestModel
import com.lgorev.ksuonlineeducation.domain.trainingdirection.TrainingDirectionRequestModel
import com.lgorev.ksuonlineeducation.domain.trainingdirection.TrainingDirectionResponseModel
import com.lgorev.ksuonlineeducation.exception.NotFoundException
import com.lgorev.ksuonlineeducation.exception.UniqueConstraintException
import com.lgorev.ksuonlineeducation.repository.trainingdirection.*
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.data.domain.Page
import org.springframework.data.repository.findByIdOrNull
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import java.util.*

@Service
@Transactional
class TrainingDirectionService(private val trainingDirectionRepository: TrainingDirectionRepository,
                               private val subjectForEntranceRepository: SubjectForEntranceRepository) {

    @Autowired
    private lateinit var facultyService: FacultyService
    @Autowired
    private lateinit var subjectService: SubjectService

    @Throws(UniqueConstraintException::class)
    fun addTrainingDirection(model: TrainingDirectionRequestModel): TrainingDirectionResponseModel {
        if (trainingDirectionRepository.existsByName(model.name))
            throw UniqueConstraintException(message = "Направление ${model.name} уже существует")

        if (!facultyService.existFacultyById(model.facultyId))
            throw NotFoundException(message = "Факультет не найден")

        val direction = trainingDirectionRepository.save(model.toEntity()).toModel()
        if (model.subjectIds.isNotEmpty()) {
            if (!subjectService.existsSubjectsByIds(model.subjectIds)) {
                throw NotFoundException("Предмет не найден")
            } else {
                val subjectForEntrance = model.subjectIds.map { SubjectsForEntranceEntity(SubjectsForEntranceId(direction.id, it)) }
                subjectForEntranceRepository.saveAll(subjectForEntrance)
            }
        }
        return direction
    }

    @Throws(NotFoundException::class, UniqueConstraintException::class)
    fun updateTrainingDirection(model: TrainingDirectionRequestModel): TrainingDirectionResponseModel {
        trainingDirectionRepository.findByName(model.name)?.let { dir ->
            if (dir.id != model.id)
                throw UniqueConstraintException(message = "Направление ${model.name} уже существует")
        }
        trainingDirectionRepository.findByIdOrNull(model.id)?.let { dir ->
            if (!facultyService.existFacultyById(model.facultyId)) {
                dir.name = model.name
                dir.description = model.description
                dir.facultyId = model.facultyId
                return dir.toModel()
            } else throw NotFoundException(message = "Факультет не найден")
        }
        throw NotFoundException(message = "Направление не найдено")
    }

    fun getTrainingDirectionById(id: UUID): TrainingDirectionResponseModel {
        trainingDirectionRepository.findByIdOrNull(id)?.let { return it.toModel() }
        throw NotFoundException(message = "Направление не неайдено")
    }

    fun getTrainingDirectionPage(model: TrainingDirectionPageRequestModel): Page<TrainingDirectionResponseModel> {
        val ids = subjectForEntranceRepository.findByDirectionIds(model.subjectIds)
        return trainingDirectionRepository.findPage(model, ids).map { it.toModel() }
    }

    fun deleteTrainingDirection(id: UUID) {
        if (trainingDirectionRepository.existsById(id))
            trainingDirectionRepository.deleteById(id)
    }
}

private fun TrainingDirectionRequestModel.toEntity() = TrainingDirectionEntity(id, name, code, description, facultyId)

private fun TrainingDirectionEntity.toModel() = TrainingDirectionResponseModel(id, name, code, description, facultyId)