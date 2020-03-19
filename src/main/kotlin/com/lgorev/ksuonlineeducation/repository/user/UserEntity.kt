package com.lgorev.ksuonlineeducation.repository.user

import com.lgorev.ksuonlineeducation.repository.faculty.FacultyEntity
import com.lgorev.ksuonlineeducation.repository.teacher.TeacherEntity
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
        @OneToMany(
                cascade = [CascadeType.ALL],
                orphanRemoval = true,
                fetch = FetchType.EAGER
        )
        @JoinColumn(name = "user_id", referencedColumnName = "id")
        var roles: MutableSet<UserRoleEntity> = mutableSetOf(),
        @Column(name = "photo_exists")
        var photoExists: Boolean

) {
    @OneToOne(cascade = [CascadeType.ALL], fetch = FetchType.LAZY)
    @JoinColumn(name = "id", referencedColumnName = "user_id")
    var teacher: TeacherEntity? = null
}
