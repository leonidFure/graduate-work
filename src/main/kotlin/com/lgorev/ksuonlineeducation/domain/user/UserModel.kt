package com.lgorev.ksuonlineeducation.domain.user

import org.springframework.security.core.authority.SimpleGrantedAuthority
import org.springframework.security.core.userdetails.UserDetails
import java.util.*

class UserModel(
        val id: UUID = UUID.randomUUID(),
        var firstName: String = "",
        var lastName: String = "",
        var patronymic: String? = null,
        private var email: String = "",
        private var password: String = "",
        var role: Role
) : UserDetails {


    override fun getAuthorities() = mutableListOf(role).map { SimpleGrantedAuthority(it.toString()) }

    override fun isEnabled() = true

    override fun getUsername() = email

    override fun isCredentialsNonExpired() = true

    override fun getPassword() = password

    override fun isAccountNonExpired() = true

    override fun isAccountNonLocked() = true

}

enum class Role { ADMIN, TEACHER, STUDENT }

