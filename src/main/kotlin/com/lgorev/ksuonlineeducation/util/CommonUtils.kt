package com.lgorev.ksuonlineeducation.util

import com.lgorev.ksuonlineeducation.domain.user.Role
import com.lgorev.ksuonlineeducation.security.TokenCredentialContainer
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken
import java.security.Principal
import java.util.*

fun getUserId(principal: Principal): UUID? {
    val token = principal as UsernamePasswordAuthenticationToken
    val container = token.credentials as TokenCredentialContainer
    return container.id
}

fun getRole(principal: Principal): Role {
    val token = principal as UsernamePasswordAuthenticationToken
    return Role.valueOf(token.authorities.first().authority)
}