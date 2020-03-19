package com.lgorev.ksuonlineeducation.domain.common

import com.fasterxml.jackson.annotation.JsonInclude
import com.lgorev.ksuonlineeducation.domain.user.Role
import java.time.LocalDateTime

@JsonInclude(JsonInclude.Include.NON_NULL)
data class TokenResponseModel (
        val accessToken: String?,
        val refreshToken: String?,
        val roles: MutableList<Role>,
        val accessTokenExpirationDateTime: LocalDateTime,
        val refreshTokenExpirationDateTime: LocalDateTime
)