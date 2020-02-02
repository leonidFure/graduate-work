package com.lgorev.ksuonlineeducation.repository.teacher

import java.time.LocalDate
import java.util.*
import javax.persistence.*

@Entity
@Table(name = "teacher")
data class TeacherEntity(
        @Id
        @Column(name = "user_id")
        val userId: UUID,
        @Column(name = "start_work_date")
        var startWorkDate: LocalDate,
        @Column(name = "info")
        var info: String
)