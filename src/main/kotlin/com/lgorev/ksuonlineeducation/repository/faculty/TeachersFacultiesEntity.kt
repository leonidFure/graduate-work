package com.lgorev.ksuonlineeducation.repository.faculty

import java.io.Serializable
import java.util.*
import javax.persistence.*

@Entity
@Table(name = "teachers_faculties")
data class TeachersFacultiesEntity(
        @EmbeddedId
        val teachersFacultiesId: TeachersFacultiesId
)

@Embeddable
data class TeachersFacultiesId(
        @Column(name = "teacher_id")
        val teacherId: UUID,
        @Column(name = "faculty_id")
        val facultyId: UUID
) : Serializable