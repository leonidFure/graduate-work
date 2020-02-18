package com.lgorev.ksuonlineeducation.service

import com.lgorev.ksuonlineeducation.domain.subject.SubjectRequestModel
import com.lgorev.ksuonlineeducation.domain.subject.SubjectRequestPageModel
import com.lgorev.ksuonlineeducation.domain.subject.SubjectResponseModel
import com.lgorev.ksuonlineeducation.exception.UniqueConstraintException
import com.lgorev.ksuonlineeducation.repository.subject.SubjectEntity
import com.lgorev.ksuonlineeducation.repository.subject.SubjectRepository
import com.lgorev.ksuonlineeducation.repository.trainingdirection.SubjectForEntranceRepository
import javassist.NotFoundException
import org.springframework.data.domain.Page
import org.springframework.data.repository.findByIdOrNull
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import java.util.*

@Service
@Transactional
class SubjectService(private val subjectRepository: SubjectRepository,
                     private val subjectForEntranceRepository: SubjectForEntranceRepository) {

    @Throws(UniqueConstraintException::class)
    fun addSubject(model: SubjectRequestModel): SubjectResponseModel {
        if (subjectRepository.existsByName(model.name))
            throw UniqueConstraintException("Предмет \"${model.name}\" уже существует")
        return subjectRepository.save(model.toEntity()).toModel()
    }

    @Throws(NotFoundException::class, UniqueConstraintException::class)
    fun updateSubject(model: SubjectRequestModel): SubjectResponseModel {
        subjectRepository.findByName(model.name)?.let { subject ->
            if (subject.id != model.id)
                throw UniqueConstraintException("Предмет \"${model.name}\" уже существует")
        }
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

    fun getSubjectPage(model: SubjectRequestPageModel): Page<SubjectResponseModel> {
        val subjectIds = subjectForEntranceRepository.findByDirectionIds(model.trainingDirectionIds)
        return subjectRepository.findPage(model, subjectIds).map { it.toModel() }
    }

    fun deleteSubject(id: UUID) {
        if (subjectRepository.existsById(id))
            subjectRepository.deleteById(id)
    }
}

private fun SubjectRequestModel.toEntity() = SubjectEntity(id, name, description, type)

private fun SubjectEntity.toModel() = SubjectResponseModel(id, name, description, type)