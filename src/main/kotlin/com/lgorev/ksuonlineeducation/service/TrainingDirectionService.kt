package com.lgorev.ksuonlineeducation.service

import com.lgorev.ksuonlineeducation.domain.common.PageResponseModel
import com.lgorev.ksuonlineeducation.domain.common.map
import com.lgorev.ksuonlineeducation.domain.trainingdirection.TrainingDirectionPageRequestModel
import com.lgorev.ksuonlineeducation.domain.trainingdirection.TrainingDirectionRequestModel
import com.lgorev.ksuonlineeducation.domain.trainingdirection.TrainingDirectionResponseModel
import com.lgorev.ksuonlineeducation.exception.BadRequestException
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
class TrainingDirectionService(private val trainingDirectionRepository: TrainingDirectionRepository) {

    @Autowired
    private lateinit var facultyService: FacultyService

    @Autowired
    private lateinit var subjectService: SubjectService

    @Autowired
    private lateinit var subjectForEntranceService: SubjectForEntranceService

    @Throws(UniqueConstraintException::class)
    fun addTrainingDirection(model: TrainingDirectionRequestModel): TrainingDirectionResponseModel {
        if (trainingDirectionRepository.existsByName(model.name))
            throw UniqueConstraintException(message = "Направление ${model.name} уже существует")

        if (!facultyService.existFacultyById(model.facultyId))
            throw BadRequestException(message = "Факультет не найден")

        val direction = trainingDirectionRepository.save(model.toEntity()).toModel()
        if (model.subjectIds.isNotEmpty()) {
            if (!subjectService.existsSubjectsByIds(model.subjectIds)) {
                throw BadRequestException("Предмет не найден")
            } else {
                val subjectForEntrance = model.subjectIds.map { getSubjectsForEntranceEntity(direction.id, it) }.toMutableSet()
                subjectForEntranceService.saveAll(subjectForEntrance)
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
            if (!subjectService.existsSubjectsByIds(model.subjectIds)) {
                throw BadRequestException("Предмет не найден")
            } else {
                subjectForEntranceService.deleteAllByDirectionId(model.id)
                val subjectForEntrance = model.subjectIds.map { getSubjectsForEntranceEntity(model.id, it) }.toMutableSet()
                subjectForEntranceService.saveAll(subjectForEntrance)
            }
            if (facultyService.existFacultyById(model.facultyId)) {
                dir.name = model.name
                dir.description = model.description
                dir.facultyId = model.facultyId

                return dir.toModel()
            } else throw BadRequestException(message = "Факультет не найден")
        }


        throw BadRequestException(message = "Направление не найдено")
    }

    fun getTrainingDirectionById(id: UUID): TrainingDirectionResponseModel {
        trainingDirectionRepository.findByIdOrNull(id)?.let { return it.toModel() }
        throw BadRequestException(message = "Направление не неайдено")
    }

    fun getTrainingDirectionPage(model: TrainingDirectionPageRequestModel): PageResponseModel<TrainingDirectionResponseModel> {
        val page = if (model.subjectIds.isNotEmpty()) {
            val subjectForEntranceIds = subjectForEntranceService.getSubjectForEntranceByDirectionIds(model.subjectIds)
            val ids = subjectForEntranceIds.map { it.subjectsForEntranceId.trainingDirectionId }.toMutableSet()
            trainingDirectionRepository.findPage(model, ids).map { it.toModel() }
        } else trainingDirectionRepository.findPage(model, null).map { it.toModel() }
        val ids = page.content.map { it.id }.toMutableSet()
        val subjectsForEntrance = subjectForEntranceService.getSubjectForEntranceByDirectionIds(ids)
        val subjectIds = subjectsForEntrance.map { it.subjectsForEntranceId.subjectId }.toMutableSet()
        val subjects = subjectService.getSubjectListByIds(subjectIds)
        page.content.forEach { dir ->
            run {
                val currentSubjectIds = subjectsForEntrance
                        .filter { it.subjectsForEntranceId.trainingDirectionId == dir.id }
                        .map { it.subjectsForEntranceId.subjectId }
                val currentSubjects = subjects
                        .filter { subject -> subject.id in currentSubjectIds }
                        .toMutableSet()
                dir.subjects = currentSubjects
            }
        }
        return page
    }

    fun deleteTrainingDirection(id: UUID) {
        if (trainingDirectionRepository.existsById(id))
            trainingDirectionRepository.deleteById(id)
    }

    private fun getSubjectsForEntranceEntity(trainingDirectionID: UUID, subjectId: UUID) =
            SubjectsForEntranceEntity(SubjectsForEntranceId(trainingDirectionID, subjectId))
}

private fun TrainingDirectionRequestModel.toEntity() = TrainingDirectionEntity(id, name, code, description, facultyId)

private fun TrainingDirectionEntity.toModel() = TrainingDirectionResponseModel(id, name, code, description, facultyId)