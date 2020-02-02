package com.lgorev.ksuonlineeducation.domain.common

import com.fasterxml.jackson.annotation.JsonInclude
import com.lgorev.ksuonlineeducation.domain.user.Role
import java.time.LocalDateTime

@JsonInclude(JsonInclude.Include.NON_NULL)
data class TokenResponseModel (
        val token: String?,
        val roles: MutableList<Role>,
        val expirationDateTime: LocalDateTime
)