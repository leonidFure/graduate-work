package com.lgorev.ksuonlineeducation.domain.educationprogram

import com.lgorev.ksuonlineeducation.domain.subject.SubjectResponseModel
import com.lgorev.ksuonlineeducation.domain.trainingdirection.TrainingDirectionResponseModel
import java.time.LocalDate
import java.util.*

data class EducationProgramResponseModel(
        val id: UUID = UUID.randomUUID(),
        val direction: TrainingDirectionResponseModel?,
        val subject: SubjectResponseModel?,
        val name: String,
        val description: String,
        val creationDate: LocalDate = LocalDate.now(),
        val status: EducationProgramStatus,
        val isActual: Boolean = true
)