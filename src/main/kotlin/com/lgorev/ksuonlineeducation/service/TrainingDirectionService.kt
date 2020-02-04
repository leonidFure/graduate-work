package com.lgorev.ksuonlineeducation.service

import com.lgorev.ksuonlineeducation.domain.trainingdirection.TrainingDirectionPageRequestModel
import com.lgorev.ksuonlineeducation.domain.trainingdirection.TrainingDirectionRequestModel
import com.lgorev.ksuonlineeducation.domain.trainingdirection.TrainingDirectionResponseModel
import com.lgorev.ksuonlineeducation.exception.NotFoundException
import com.lgorev.ksuonlineeducation.exception.UniqueConstraintException
import com.lgorev.ksuonlineeducation.repository.department.DepartmentRepository
import com.lgorev.ksuonlineeducation.repository.trainingdirection.TrainingDirectionEntity
import com.lgorev.ksuonlineeducation.repository.trainingdirection.TrainingDirectionRepository
import org.springframework.data.domain.Page
import org.springframework.data.domain.PageRequest
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
        if(trainingDirectionRepository.existsByName(model.name))
            throw UniqueConstraintException(message = "Направление ${model.name} уже существует")

        if (!departmentRepository.existsById(model.departmentId))
            throw NotFoundException(message = "Кафедра не найдена")

        return trainingDirectionRepository.save(model.toEntity()).toModel()
    }

    @Throws(NotFoundException::class, UniqueConstraintException::class)
    fun updateTrainingDirection(model: TrainingDirectionRequestModel): TrainingDirectionResponseModel {
        trainingDirectionRepository.findByName(model.name)?.let { dir ->
            if (dir.id != model.id)
                throw UniqueConstraintException(message = "Направление ${model.name} уже существует")
        }
        trainingDirectionRepository.findByIdOrNull(model.id)?.let { dir ->
            if (departmentRepository.existsById(model.departmentId)) {
                dir.name = model.name
                dir.description = model.description
                dir.departmentId = model.departmentId
                return dir.toModel()
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

    fun getTrainingDirectionPage(model: TrainingDirectionPageRequestModel): Page<TrainingDirectionResponseModel> {
        val pageable = PageRequest.of(model.pageNum, model.pageSize, model.sortType, model.sortField)
        return if (model.nameFilter != null)
            trainingDirectionRepository.findAllByNameContainingIgnoreCase(pageable, model.nameFilter).map { it.toModel() }
        else
            trainingDirectionRepository.findAll(pageable).map { it.toModel() }
    }

    fun deleteTrainingDirection(id: UUID) = trainingDirectionRepository.deleteById(id)
}

private fun TrainingDirectionRequestModel.toEntity() = TrainingDirectionEntity(id, name, description, departmentId)

private fun TrainingDirectionEntity.toModel() = TrainingDirectionResponseModel(id, name, description, departmentId)