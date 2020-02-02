package com.lgorev.ksuonlineeducation.service

import com.lgorev.ksuonlineeducation.domain.trainingdirection.TrainingDirectionRequestModel
import com.lgorev.ksuonlineeducation.domain.trainingdirection.TrainingDirectionResponseModel
import com.lgorev.ksuonlineeducation.exception.NotFoundException
import com.lgorev.ksuonlineeducation.exception.UniqueConstraintException
import com.lgorev.ksuonlineeducation.repository.department.DepartmentRepository
import com.lgorev.ksuonlineeducation.repository.trainingdirection.TrainingDirectionEntity
import com.lgorev.ksuonlineeducation.repository.trainingdirection.TrainingDirectionRepository
import org.springframework.data.domain.Pageable
import org.springframework.data.repository.findByIdOrNull
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import java.util.*

@Service
@Transactional
class TrainingDirectionService(private val trainingDirectionRepository: TrainingDirectionRepository,
                               private val departmentRepository: DepartmentRepository) {

    @Throws(UniqueConstraintException::class)
    fun addTrainingDirection(model: TrainingDirectionRequestModel): TrainingDirectionResponseModel {
        trainingDirectionRepository.findByName(model.name)?.let {
            throw UniqueConstraintException("Направление ${model.name} уже существует")
        }
        return trainingDirectionRepository.save(model.toEntity()).toModel()
    }

    @Throws(NotFoundException::class, UniqueConstraintException::class)
    fun updateTrainingDirection(model: TrainingDirectionRequestModel): TrainingDirectionResponseModel {
        trainingDirectionRepository.findByName(model.name)?.let { dir ->
            if (dir.id != model.id) throw UniqueConstraintException(message = "Направление ${model.name} уже существует")
        }
        trainingDirectionRepository.findByIdOrNull(model.id)?.let { dir ->
            if (departmentRepository.existsById(model.departmentId)) {
                dir.name = model.name
                dir.description = model.description
                dir.departmentId = model.departmentId
            } else throw NotFoundException(message = "Кафедра не найдена")
        }
        throw NotFoundException(message = "Направление не найдено")
    }

    fun getTrainingDirectionById(id: UUID): TrainingDirectionResponseModel {
        trainingDirectionRepository.findByIdOrNull(id)?.let {
            return it.toModel()
        }
        throw NotFoundException(message = "Направление не неайдено")
    }

    fun getTrainingDirectionPage(pageable: Pageable) = trainingDirectionRepository.findAll(pageable).map { it.toModel() }

    fun deleteTrainingDirection(id: UUID) = trainingDirectionRepository.deleteById(id)
}

private fun TrainingDirectionRequestModel.toEntity() = TrainingDirectionEntity(id, name, description, departmentId)

private fun TrainingDirectionEntity.toModel() = TrainingDirectionResponseModel(id, name, description, departmentId)