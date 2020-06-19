package com.lgorev.ksuonlineeducation.domain.user

import com.fasterxml.jackson.annotation.JsonInclude
import com.lgorev.ksuonlineeducation.domain.faculty.FacultyResponseModel
import java.time.LocalDate
import java.util.*

@JsonInclude(JsonInclude.Include.NON_NULL)
data class UserResponseModel(
        val id: UUID,
        val firstName: String,
        val lastName: String,
        val patronymic: String?,
        val email: String,
        val role: Role,
        val photoUrl: String,
        val startWorkDate: LocalDate?,
        val info: String?,
        val imageId: UUID?,
        var faculties: MutableSet<FacultyResponseModel>? = null,
        val registrationDate: LocalDate
)