package com.lgorev.ksuonlineeducation.domain.user

import com.fasterxml.jackson.annotation.JsonInclude
import com.lgorev.ksuonlineeducation.domain.teacher.TeacherModel
import java.util.*

@JsonInclude(JsonInclude.Include.NON_NULL)
data class UserResponseModel(
        val id: UUID,
        val firstName: String,
        val lastName: String,
        val patronymic: String?,
        val email: String,
        val roles: MutableSet<Role>,
        val teacher: TeacherModel? = null,
        val photoUrl: String
)