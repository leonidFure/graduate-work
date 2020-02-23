package com.lgorev.ksuonlineeducation.repository.faculty

import com.lgorev.ksuonlineeducation.repository.user.UserEntity
import java.util.*
import javax.persistence.*

@Entity
@Table(name = "faculty")
data class FacultyEntity(
        @Id
        @Column(name = "id")
        val id: UUID = UUID.randomUUID(),
        @Column(name = "name", unique = true)
        var name: String = "",
        @Column(name = "description")
        var description: String,
        @Column(name = "manager_id")
        var managerId: UUID

)