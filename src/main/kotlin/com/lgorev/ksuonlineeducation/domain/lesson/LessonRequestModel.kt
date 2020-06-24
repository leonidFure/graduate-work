package com.lgorev.ksuonlineeducation.domain.lesson

import com.fasterxml.jackson.annotation.JsonFormat
import com.fasterxml.jackson.databind.annotation.JsonDeserialize
import com.fasterxml.jackson.databind.annotation.JsonSerialize
import com.fasterxml.jackson.datatype.jsr310.deser.LocalDateDeserializer
import com.fasterxml.jackson.datatype.jsr310.ser.LocalDateSerializer
import java.time.LocalDate
import java.util.*

data class LessonRequestModel (
        val id: UUID = UUID.randomUUID(),
        val courseId: UUID,
        val timetableId: UUID,
        @JsonSerialize(using = LocalDateSerializer::class)
        @JsonDeserialize(using = LocalDateDeserializer::class)
        @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd")
        val date: LocalDate,
        val status: LessonStatus = LessonStatus.LESSON_CREATED,
        val videoUri: String?
)

enum class LessonStatus { LESSON_CREATED, LESSON_IN_PROGRESS, LESSON_DONE }
