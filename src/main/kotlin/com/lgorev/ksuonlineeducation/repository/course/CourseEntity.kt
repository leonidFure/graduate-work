package com.lgorev.ksuonlineeducation.repository.course

import com.lgorev.ksuonlineeducation.domain.course.CourseStatus
import java.time.LocalDate
import java.util.*
import javax.persistence.*

@Entity
@Table(name = "course")
data class CourseEntity(
        @Id
        @Column(name = "id")
        val id: UUID = UUID.randomUUID(),
        @Column(name = "education_program_id")
        var educationProgramId: UUID,
        @Column(name = "status")
        @Enumerated(value = EnumType.STRING)
        var status: CourseStatus,
        @Column(name = "start_date")
        var startDate: LocalDate,
        @Column(name = "end_date")
        var endDate: LocalDate,
        @Column(name = "creation_date")
        var creationDate: LocalDate = LocalDate.now(),
        @Column(name = "is_actual")
        var isActual: Boolean = true,
        @Column(name = "creator_id")
        val creatorId: UUID?,
        @Column(name = "image_id")
        var imageId: UUID?
) {
    companion object Fields {
        const val ID = "id"
        const val EDUCATION_PROGRAM_ID = "educationProgramId"
        const val STATUS = "status"
        const val START_DATE = "startDate"
        const val END_DATE = "endDate"
        const val CREATION_DATE = "creationDate"
        const val IS_ACTUAL = "isActual"
    }
}

