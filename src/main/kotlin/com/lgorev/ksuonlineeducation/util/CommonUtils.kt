package com.lgorev.ksuonlineeducation.util

import com.lgorev.ksuonlineeducation.domain.user.Role
import com.lgorev.ksuonlineeducation.domain.user.Role.STUDENT
import com.lgorev.ksuonlineeducation.domain.user.Role.TEACHER
import com.lgorev.ksuonlineeducation.security.TokenCredentialContainer
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken
import java.security.Principal
import java.util.*

fun getUserId(principal: Principal): UUID? {
    val token = principal as UsernamePasswordAuthenticationToken
    val role = Role.valueOf(token.authorities.first().authority)
    if(role in mutableSetOf(STUDENT, TEACHER)) {
        val container = token.credentials as TokenCredentialContainer
        return container.id
    }
    return null
}