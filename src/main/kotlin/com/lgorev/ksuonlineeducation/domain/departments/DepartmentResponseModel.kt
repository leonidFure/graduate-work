package com.lgorev.ksuonlineeducation.domain.departments

import com.fasterxml.jackson.annotation.JsonInclude
import com.lgorev.ksuonlineeducation.domain.user.UserResponseModel
import java.util.*

@JsonInclude(JsonInclude.Include.NON_NULL)
data class DepartmentResponseModel(
        val id: UUID = UUID.randomUUID(),
        val name: String,
        val description: String,
        val facultyId: UUID,
        val manager: UserResponseModel?
)