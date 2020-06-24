package com.lgorev.ksuonlineeducation.domain.educationprogram

import com.fasterxml.jackson.annotation.JsonFormat
import com.fasterxml.jackson.databind.annotation.JsonDeserialize
import com.fasterxml.jackson.databind.annotation.JsonSerialize
import com.fasterxml.jackson.datatype.jsr310.deser.LocalDateDeserializer
import com.fasterxml.jackson.datatype.jsr310.deser.LocalDateTimeDeserializer
import com.fasterxml.jackson.datatype.jsr310.ser.LocalDateSerializer
import com.fasterxml.jackson.datatype.jsr310.ser.LocalDateTimeSerializer
import com.lgorev.ksuonlineeducation.domain.subject.SubjectResponseModel
import org.springframework.format.annotation.DateTimeFormat
import java.time.LocalDate
import java.time.LocalDateTime
import java.util.*

data class EducationProgramResponseModel(
        val id: UUID = UUID.randomUUID(),
        val subjectId: UUID,
        val name: String,
        val description: String,
        @JsonSerialize(using = LocalDateTimeSerializer::class)
        @JsonDeserialize(using = LocalDateTimeDeserializer::class)
        @DateTimeFormat
        val creationDatetime: LocalDateTime = LocalDateTime.now(),
        val status: EducationProgramStatus,
        val isActual: Boolean = true,
        var subject: SubjectResponseModel? = null
)