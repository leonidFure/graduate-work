package com.lgorev.ksuonlineeducation.domain.departments

import java.util.*

data class DepartmentRequestModel(
        val id: UUID = UUID.randomUUID(),
        val name: String,
        val description: String,
        val facultyId: UUID,
        val managerId: UUID
)