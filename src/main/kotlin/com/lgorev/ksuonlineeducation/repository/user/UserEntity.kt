package com.lgorev.ksuonlineeducation.repository.user

import com.lgorev.ksuonlineeducation.domain.user.Role
import java.time.LocalDate
import java.util.*
import javax.persistence.*

@Entity
@Table(name = "usr", schema = "public")
data class UserEntity(
        @Id
        @Column(name = "id", nullable = false)
        val id: UUID = UUID.randomUUID(),
        @Column(name = "first_name", nullable = false)
        var firstName: String = "",
        @Column(name = "last_name", nullable = false)
        var lastName: String = "",
        @Column(name = "patronymic")
        var patronymic: String? = null,
        @Column(name = "email", unique = true, nullable = false)
        var email: String = "",
        @Column(name = "password", nullable = false)
        var password: String = "",
        @Column(name = "is_active", nullable = false)
        var isActive: Boolean = true,
        @Column(name = "registration_date", nullable = false)
        val registrationDate: LocalDate = LocalDate.now(),
        @Enumerated(EnumType.STRING)
        @Column(name = "role")
        val role: Role,
        @Column(name = "start_work_date")
        val startWorkDate: LocalDate?,
        @Column(name = "info")
        val info: String?,
        @Column(name = "image_id")
        var imageId: UUID?
)
