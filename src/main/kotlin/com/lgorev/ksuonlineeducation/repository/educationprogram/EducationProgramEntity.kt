package com.lgorev.ksuonlineeducation.repository.educationprogram

import com.lgorev.ksuonlineeducation.domain.educationprogram.EducationProgramStatus
import java.time.LocalDate
import java.util.*
import javax.persistence.*

@Entity
@Table(name = "education_program")
data class EducationProgramEntity(
        @Id
        @Column(name = "id")
        val id: UUID = UUID.randomUUID(),
        @Column(name = "direction_id")
        var directionID: UUID,
        @Column(name = "subject_id")
        var subjectId: UUID,
        @Column(name = "name")
        var name: String,
        @Column(name = "description")
        var description: String,
        @Column(name = "creation_date")
        var creationDate: LocalDate = LocalDate.now(),
        @Column(name = "status")
        @Enumerated(value = EnumType.STRING)
        var status: EducationProgramStatus,
        @Column(name = "is_actual")
        var isActual: Boolean = true
)

