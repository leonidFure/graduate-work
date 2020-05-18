package com.lgorev.ksuonlineeducation.service

import com.lgorev.ksuonlineeducation.domain.educationprogram.TeachersEducationsProgramsModel
import com.lgorev.ksuonlineeducation.repository.educationprogram.TeachersEducationProgramsEntity
import com.lgorev.ksuonlineeducation.repository.educationprogram.TeachersEducationProgramsRepository
import org.springframework.stereotype.Service
import java.util.*

@Service
class TeachersEducationProgramsService(private val teachersEducationProgramsRepository: TeachersEducationProgramsRepository) {

    fun getTeachersEducationProgramsByTeacherId(id: UUID) = teachersEducationProgramsRepository.findAllByTeacherId(id).map { it.toModel() }
}

private fun TeachersEducationProgramsEntity.toModel() =
        TeachersEducationsProgramsModel(
                teachersEducationProgramsId.educationProgramId,
                teachersEducationProgramsId.teacherId
        )