package com.lgorev.ksuonlineeducation.repository.course

import java.time.LocalDate
import java.util.*
import javax.persistence.Column
import javax.persistence.Entity
import javax.persistence.Id
import javax.persistence.Table

@Entity
@Table(name = "course")
data class CourseEntity(
        @Id
        @Column(name = "id")
        val id: UUID = UUID.randomUUID(),
        @Column(name = "education_program_id")
        var educationProgramId: UUID,
        @Column(name = "status")
        var status: CourseStatus,
        @Column(name = "start_date")
        var startDate: LocalDate,
        @Column(name = "end_date")
        var endDate: LocalDate,
        @Column(name = "creation_date")
        var creationDate: LocalDate = LocalDate.now(),
        @Column(name = "is_actual")
        var isActual: Boolean = true
)

enum class CourseStatus { AWAIT_STUDENTS, IN_PROGRESS, DONE }