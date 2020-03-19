package com.lgorev.ksuonlineeducation.domain.trainingdirection

import java.util.*

data class TrainingDirectionRequestModel(
        val id: UUID = UUID.randomUUID(),
        val name: String,
        val code: String,
        val description: String,
        val facultyId: UUID,
        val subjectIds: MutableSet<UUID> = mutableSetOf()
)