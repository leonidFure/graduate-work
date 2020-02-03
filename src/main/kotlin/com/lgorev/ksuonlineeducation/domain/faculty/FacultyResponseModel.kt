package com.lgorev.ksuonlineeducation.domain.faculty

import com.fasterxml.jackson.annotation.JsonInclude
import com.lgorev.ksuonlineeducation.domain.user.UserResponseModel
import java.util.*

@JsonInclude(JsonInclude.Include.NON_NULL)
data class FacultyResponseModel(
        val id: UUID,
        val name: String,
        val description: String,
        val manager: UserResponseModel?
)