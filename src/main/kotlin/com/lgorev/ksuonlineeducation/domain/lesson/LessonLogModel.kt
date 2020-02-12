package com.lgorev.ksuonlineeducation.domain.lesson

import java.time.LocalDateTime
import java.util.*

data class LessonLogModel(
        val lessonId: UUID,
        val datetime: LocalDateTime,
        val oldStatus: LessonStatus?,
        val newStatus: LessonStatus?
)