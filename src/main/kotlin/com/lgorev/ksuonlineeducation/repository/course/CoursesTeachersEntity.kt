package com.lgorev.ksuonlineeducation.repository.course

import java.io.Serializable
import java.util.*
import javax.persistence.*

@Entity
@Table(name = "courses_teachers")
data class CoursesTeachersEntity(
        @EmbeddedId
        val coursesTeachersId: CoursesTeachersId
)

@Embeddable
data class CoursesTeachersId(
        @Column(name = "course_id")
        val courseId: UUID,
        @Column(name = "teacher_id")
        val teacherId: UUID
) : Serializable