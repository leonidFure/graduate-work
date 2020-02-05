package com.lgorev.ksuonlineeducation.domain.educationprogram

import java.time.LocalDate
import java.util.*

data class EducationProgramResponseModel(
        val id: UUID = UUID.randomUUID(),
        val directionID: UUID,
        val subjectId: UUID,
        val name: String,
        val description: String,
        val creationDate: LocalDate = LocalDate.now(),
        val status: EducationProgramStatus,
        val isActual: Boolean = true
)