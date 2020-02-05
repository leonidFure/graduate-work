package com.lgorev.ksuonlineeducation.domain.subject

import java.util.*

data class SubjectResponseModel(
        val id: UUID = UUID.randomUUID(),
        val name: String,
        val description: String,
        val type: SubjectType
)