package com.lgorev.ksuonlineeducation.domain.faculty

import java.util.*

data class FacultyRequestModel (
        val id: UUID = UUID.randomUUID(),
        val name: String,
        val description: String,
        val abbr: String,
        val managerId: UUID = UUID(0,0)
)