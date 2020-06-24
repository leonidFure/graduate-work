package com.lgorev.ksuonlineeducation.domain.educationprogram

import com.fasterxml.jackson.databind.annotation.JsonDeserialize
import com.fasterxml.jackson.databind.annotation.JsonSerialize
import com.fasterxml.jackson.datatype.jsr310.deser.LocalDateTimeDeserializer
import com.fasterxml.jackson.datatype.jsr310.ser.LocalDateTimeSerializer
import org.springframework.format.annotation.DateTimeFormat
import java.time.LocalDateTime
import java.util.*

data class EducationProgramRequestModel(
        val id: UUID = UUID.randomUUID(),
        val subjectId: UUID,
        val name: String,
        val description: String?,
        @JsonSerialize(using = LocalDateTimeSerializer::class)
        @JsonDeserialize(using = LocalDateTimeDeserializer::class)
        @DateTimeFormat
        val creationDateTime: LocalDateTime = LocalDateTime.now(),
        val status: EducationProgramStatus = EducationProgramStatus.EP_CREATED,
        val isActual: Boolean = true
)

enum class EducationProgramStatus { EP_DRAFT, EP_AWAITING_CONFIRMATION, EP_CREATED }
