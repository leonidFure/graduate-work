package com.lgorev.ksuonlineeducation.domain.course

import com.fasterxml.jackson.annotation.JsonFormat
import com.fasterxml.jackson.databind.annotation.JsonDeserialize
import com.fasterxml.jackson.databind.annotation.JsonSerialize
import com.fasterxml.jackson.datatype.jsr310.deser.LocalDateDeserializer
import com.fasterxml.jackson.datatype.jsr310.ser.LocalDateSerializer
import com.lgorev.ksuonlineeducation.domain.timetable.TimetableRequestModel
import java.time.LocalDate
import java.util.*

data class CourseRequestModel(
        val id: UUID = UUID.randomUUID(),
        val educationProgramId: UUID,
        val status: CourseStatus = CourseStatus.COURSE_AWAIT_STUDENTS,
        @JsonSerialize(using = LocalDateSerializer::class)
        @JsonDeserialize(using = LocalDateDeserializer::class)
        @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd")
        val startDate: LocalDate,
        @JsonSerialize(using = LocalDateSerializer::class)
        @JsonDeserialize(using = LocalDateDeserializer::class)
        @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd")
        val endDate: LocalDate,
        @JsonSerialize(using = LocalDateSerializer::class)
        @JsonDeserialize(using = LocalDateDeserializer::class)
        @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd")
        val creationDate: LocalDate = LocalDate.now(),
        val isActual: Boolean = true
)

enum class CourseStatus { COURSE_AWAIT_STUDENTS, COURSE_IN_PROGRESS, COURSE_DONE }