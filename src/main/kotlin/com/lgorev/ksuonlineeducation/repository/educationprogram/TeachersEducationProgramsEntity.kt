package com.lgorev.ksuonlineeducation.repository.educationprogram

import java.io.Serializable
import java.util.*
import javax.persistence.*

@Entity
@Table(name = "teachers_education_programs")
data class TeachersEducationProgramsEntity(
        @EmbeddedId
        val teachersEducationProgramsId: TeachersEducationProgramsId
)

@Embeddable
data class TeachersEducationProgramsId(
        @Column(name = "teacher_id")
        val teacherId: UUID,
        @Column(name = "education_program_id")
        val educationProgramId: UUID
) : Serializable