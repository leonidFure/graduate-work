package com.lgorev.ksuonlineeducation.repository.group

import java.io.Serializable
import java.util.*
import javax.persistence.*

@Entity
@Table(name = "users_group")
data class UserGroupEntity(
        @EmbeddedId
        val userGroupId: UserGroupId
)

@Embeddable
data class UserGroupId(
        @Column(name = "user_id")
        val userId: UUID,
        @Column(name = "group_id")
        val groupId: UUID
): Serializable