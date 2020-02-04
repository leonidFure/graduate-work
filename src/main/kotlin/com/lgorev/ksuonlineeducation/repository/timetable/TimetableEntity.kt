package com.lgorev.ksuonlineeducation.repository.timetable

import java.time.DayOfWeek
import java.time.LocalTime
import java.util.*
import javax.persistence.*

@Entity
@Table(name = "timetable")
data class TimetableEntity(
        @Id
        @Column(name = "id")
        val id: UUID = UUID.randomUUID(),
        @Column(name = "course_id")
        var courseId: UUID,
        @Column(name = "day_of_week")
        @Enumerated(value = EnumType.STRING)
        var dayOfWeek: DayOfWeek,
        @Column(name = "start_time")
        var startTime: LocalTime,
        @Column(name = "end_time")
        var endTime: LocalTime,
        @Column(name = "type")
        var type: TimetableType,
        @Column(name = "is_actual")
        var isActual: Boolean = true
)

enum class TimetableType { EVEN, ODD, EVERY_WEEK }
