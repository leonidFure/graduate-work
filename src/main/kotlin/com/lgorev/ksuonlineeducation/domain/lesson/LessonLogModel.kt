package com.lgorev.ksuonlineeducation.domain.lesson

import com.fasterxml.jackson.databind.annotation.JsonDeserialize
import com.fasterxml.jackson.databind.annotation.JsonSerialize
import com.fasterxml.jackson.datatype.jsr310.deser.LocalDateDeserializer
import com.fasterxml.jackson.datatype.jsr310.ser.LocalDateSerializer
import org.springframework.format.annotation.DateTimeFormat
import java.time.LocalDateTime
import java.util.*

data class LessonLogModel(
        val lessonId: UUID,
        @JsonSerialize(using = LocalDateSerializer::class)
        @JsonDeserialize(using = LocalDateDeserializer::class)
        @DateTimeFormat
        val datetime: LocalDateTime,
        val oldStatus: LessonStatus?,
        val newStatus: LessonStatus?
)