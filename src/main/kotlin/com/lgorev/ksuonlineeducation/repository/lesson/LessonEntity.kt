package com.lgorev.ksuonlineeducation.repository.lesson

import com.lgorev.ksuonlineeducation.domain.lesson.LessonStatus
import java.time.LocalDate
import java.util.*
import javax.persistence.*

@Entity
@Table(name = "lesson")
data class LessonEntity(
        @Id
        @Column(name = "id")
        val id: UUID = UUID.randomUUID(),
        @Column(name = "timetable_id")
        var timetableId: UUID,
        @Column(name = "date")
        var date: LocalDate,
        @Column(name = "status")
        @Enumerated(value = EnumType.STRING)
        var status: LessonStatus
)

