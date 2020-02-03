package com.lgorev.ksuonlineeducation.domain.user

import java.util.*

data class UserRequestModel(
        var id: UUID? = null,
        val firstName: String,
        val lastName: String,
        val patronymic: String?,
        val email: String,
        val gender: Gender,
        val password: String,
        val roles: MutableSet<Role>
)