package com.lgorev.ksuonlineeducation.domain.timetable

import com.fasterxml.jackson.annotation.JsonFormat
import com.fasterxml.jackson.databind.annotation.JsonDeserialize
import com.fasterxml.jackson.databind.annotation.JsonSerialize
import com.fasterxml.jackson.datatype.jsr310.deser.LocalTimeDeserializer
import com.fasterxml.jackson.datatype.jsr310.ser.LocalTimeSerializer
import java.time.DayOfWeek
import java.time.LocalTime
import java.util.*

data class TimetableResponseModel(
        val id: UUID,
        val courseId: UUID,
        val dayOfWeek: DayOfWeek,
        @JsonSerialize(using = LocalTimeSerializer::class)
        @JsonDeserialize(using = LocalTimeDeserializer::class)
        @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "hh:mm:ss")
        val startTime: LocalTime,
        @JsonSerialize(using = LocalTimeSerializer::class)
        @JsonDeserialize(using = LocalTimeDeserializer::class)
        @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "hh:mm:ss")
        val endTime: LocalTime,
        val type: TimetableType,
        val isActual: Boolean
)