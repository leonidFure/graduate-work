package com.lgorev.ksuonlineeducation.repository.lesson

import java.time.LocalDate
import java.util.*
import javax.persistence.Column
import javax.persistence.Entity
import javax.persistence.Id
import javax.persistence.Table

@Entity
@Table(name = "lesson")
data class LessonEntity(
        @Id
        @Column(name = "id")
        val id: UUID = UUID.randomUUID(),
        @Column(name = "course_id")
        var courseId: UUID,
        @Column(name = "date")
        var date: LocalDate,
        @Column(name = "status")
        var status: LessonStatus
)

enum class LessonStatus { CREATED, IN_PROGRESS, DONE }
