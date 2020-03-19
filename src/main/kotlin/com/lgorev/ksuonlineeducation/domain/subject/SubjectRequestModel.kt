package com.lgorev.ksuonlineeducation.domain.subject

import java.util.*

data class SubjectRequestModel(
        val id: UUID = UUID.randomUUID(),
        val name: String,
        val description: String = "",
        val type: SubjectType = SubjectType.EXAM
)

enum class SubjectType { EXAM, OLYMPIAD }