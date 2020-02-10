package com.lgorev.ksuonlineeducation.domain.trainingdirection

import com.fasterxml.jackson.annotation.JsonInclude
import java.util.*

@JsonInclude(JsonInclude.Include.NON_NULL)
data class TrainingDirectionResponseModel(
        val id: UUID,
        val name: String,
        val description: String,
        val facultyId: UUID
)