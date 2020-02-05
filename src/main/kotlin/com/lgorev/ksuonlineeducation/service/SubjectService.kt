package com.lgorev.ksuonlineeducation.service

import com.lgorev.ksuonlineeducation.domain.subject.SubjectRequestModel
import com.lgorev.ksuonlineeducation.domain.subject.SubjectRequestPageModel
import com.lgorev.ksuonlineeducation.domain.subject.SubjectResponseModel
import com.lgorev.ksuonlineeducation.repository.subject.SubjectEntity
import com.lgorev.ksuonlineeducation.repository.subject.SubjectRepository
import javassist.NotFoundException
import org.springframework.data.domain.Page
import org.springframework.data.domain.PageRequest
import org.springframework.data.repository.findByIdOrNull
import org.springframework.stereotype.Service
import java.util.*

@Service
class SubjectService(private val subjectRepository: SubjectRepository) {

    fun addSubject(model: SubjectRequestModel) =
            subjectRepository.save(model.toEntity()).toModel()

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

    fun getSubjectPage(model: SubjectRequestPageModel): Page<SubjectEntity> {
        val pageable = PageRequest.of(model.pageNum, model.pageSize, model.sortType, model.sortField)

        return if (model.nameFilter != null)
            subjectRepository.findAllByNameContainingIgnoreCase(pageable, model.nameFilter)
        else
            subjectRepository.findAll(pageable)
    }

    fun deleteSubject(id: UUID) = subjectRepository.deleteById(id)
}

private fun SubjectRequestModel.toEntity() = SubjectEntity(id, name, description, type)

private fun SubjectEntity.toModel() = SubjectResponseModel(id, name, description, type)