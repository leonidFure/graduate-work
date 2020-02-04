package com.lgorev.ksuonlineeducation.repository.lesson

import java.io.Serializable
import java.time.LocalDateTime
import java.util.*
import javax.persistence.*

@Entity
@Table(name = "lesson_log")
data class LessonLogEntity(
        @Id
        @Column(name = "id")
        val id: UUID = UUID.randomUUID(),
        @Column(name = "lesson_id")
        val lessonId: UUID,
        @Column(name = "datetime")
        val datetime: LocalDateTime,
        @Column(name = "old_status")
        val oldStatus: LessonStatus,
        @Column(name = "new_status")
        val newStatus: LessonStatus
)
