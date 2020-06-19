package com.lgorev.ksuonlineeducation.domain.trainingdirection

import com.fasterxml.jackson.annotation.JsonInclude
import com.lgorev.ksuonlineeducation.domain.subject.SubjectResponseModel
import java.util.*

@JsonInclude(JsonInclude.Include.NON_NULL)
data class TrainingDirectionResponseModel(
        val id: UUID,
        val name: String,
        val code: String,
        val description: String,
        val facultyId: UUID,
        var subjects: MutableSet<SubjectResponseModel>? = null
)