package com.lgorev.ksuonlineeducation.domain.user

import java.util.*

data class UserRequestModel(
        var id: UUID? = null,
        val firstName: String,
        val lastName: String,
        val patronymic: String?,
        val email: String,
        val password: String
)