package com.lgorev.ksuonlineeducation.service

import com.lgorev.ksuonlineeducation.domain.common.PageResponseModel
import com.lgorev.ksuonlineeducation.domain.common.map
import com.lgorev.ksuonlineeducation.domain.subject.SubjectListRequestModel
import com.lgorev.ksuonlineeducation.domain.subject.SubjectRequestModel
import com.lgorev.ksuonlineeducation.domain.subject.SubjectRequestPageModel
import com.lgorev.ksuonlineeducation.domain.subject.SubjectResponseModel
import com.lgorev.ksuonlineeducation.exception.UniqueConstraintException
import com.lgorev.ksuonlineeducation.repository.subject.SubjectEntity
import com.lgorev.ksuonlineeducation.repository.subject.SubjectRepository
import javassist.NotFoundException
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.data.domain.Page
import org.springframework.data.repository.findByIdOrNull
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import java.util.*

@Service
@Transactional
class SubjectService(private val subjectRepository: SubjectRepository) {

    @Autowired
    private lateinit var subjectForEntranceService: SubjectForEntranceService


    @Throws(UniqueConstraintException::class)
    fun addSubject(model: SubjectRequestModel): SubjectResponseModel {
        return subjectRepository.save(model.toEntity()).toModel()
    }

    @Throws(NotFoundException::class, UniqueConstraintException::class)
    fun updateSubject(model: SubjectRequestModel): SubjectResponseModel {
        subjectRepository.findByIdOrNull(model.id)?.let { subject ->
            subject.name = model.name
            subject.description = model.description
            subject.type = model.type
            return subject.toModel()
        }
        throw NotFoundException("Предмет не найден")
    }

    @Throws(NotFoundException::class)
    fun getSubjectById(id: UUID): SubjectResponseModel {
        subjectRepository.findByIdOrNull(id)?.let { subject -> return subject.toModel() }
        throw NotFoundException("Предмет не найден")
    }

    fun existSubjectById(id: UUID) = subjectRepository.existsById(id)

    fun existsSubjectsByIds(ids: MutableSet<UUID>) = subjectRepository.existsByIdIn(ids)

    fun getSubjectPage(model: SubjectRequestPageModel): PageResponseModel<SubjectResponseModel> {
        return when {
            model.trainingDirectionIds.isNotEmpty() -> {
                val subjectForEntranceIds = subjectForEntranceService.getSubjectForEntranceByDirectionIds(model.trainingDirectionIds)
                val ids = subjectForEntranceIds.map { it.subjectsForEntranceId.subjectId }.toMutableSet()
                subjectRepository.findPage(model, ids).map { it.toModel() }
            }
            model.trainingDirectionId != null -> {
                val subjectForEntranceIds = subjectForEntranceService.getSubjectForEntranceByDirectionIds(mutableSetOf(model.trainingDirectionId))
                val ids = subjectForEntranceIds.map { it.subjectsForEntranceId.subjectId }.toMutableSet()
                subjectRepository.findPage(model, ids).map { it.toModel() }
            }
            else -> subjectRepository.findPage(model, null).map { it.toModel() }
        }
    }

    fun deleteSubject(id: UUID) {
        if (subjectRepository.existsById(id))
            subjectRepository.deleteById(id)
    }

    fun getSubjectListByIds(model: SubjectListRequestModel) = subjectRepository.findByIdIn(model.ids).map { it.toModel() }

    fun getSubjectListByIds(ids: MutableSet<UUID>) = subjectRepository.findByIdIn(ids).map { it.toModel() }

    fun getSubjectList() = subjectRepository.findAll().map { it.toModel() }

    fun setImageId(subjectId: UUID, imageId: UUID) {
        subjectRepository.findByIdOrNull(subjectId)?.let { it.imageId = imageId }
    }
}

private fun SubjectRequestModel.toEntity() = SubjectEntity(id, name, description, type, null)

private fun SubjectEntity.toModel() = SubjectResponseModel(id, name, description, type, imageId)