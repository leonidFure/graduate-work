package com.lgorev.ksuonlineeducation.repository.department

import com.lgorev.ksuonlineeducation.repository.user.UserEntity
import java.util.*
import javax.persistence.*

@Entity
@Table(name = "department")
data class DepartmentEntity(
        @Id
        @Column(name = "id")
        val id: UUID = UUID.randomUUID(),
        @Column(name = "name", unique = true)
        var name: String = "",
        @Column(name = "description")
        var description: String,
        @Column(name = "faculty_id")
        var facultyId: UUID,
        @Column(name = "manager_id")
        var managerId: UUID

) {
        @OneToOne(fetch = FetchType.LAZY)
        @JoinColumn(name = "manager_id", insertable = false, updatable = false)
        lateinit var manager: UserEntity
}