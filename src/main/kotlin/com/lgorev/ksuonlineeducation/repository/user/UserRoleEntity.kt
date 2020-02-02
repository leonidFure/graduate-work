package com.lgorev.ksuonlineeducation.repository.user

import com.lgorev.ksuonlineeducation.domain.user.Role
import java.io.Serializable
import java.util.*
import javax.persistence.*

@Entity
@Table(name = "user_roles", schema = "public")
data class UserRoleEntity (
        @EmbeddedId
        val userRoleId: UserRoleId
)
@Embeddable
data class UserRoleId(
        @Column(name = "user_id")
        val userId: UUID,
        @Column(name = "role")
        @Enumerated(value = EnumType.STRING)
        val role: Role
): Serializable
