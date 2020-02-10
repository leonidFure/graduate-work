package com.lgorev.ksuonlineeducation.domain.educationprogram

import com.fasterxml.jackson.annotation.JsonFormat
import com.fasterxml.jackson.databind.annotation.JsonDeserialize
import com.fasterxml.jackson.databind.annotation.JsonSerialize
import com.fasterxml.jackson.datatype.jsr310.deser.LocalDateDeserializer
import com.fasterxml.jackson.datatype.jsr310.ser.LocalDateSerializer
import java.time.LocalDate
import java.util.*

data class EducationProgramResponseModel(
        val id: UUID = UUID.randomUUID(),
        val directionId: UUID,
        val subjectId: UUID,
        val name: String,
        val description: String,
        @JsonSerialize(using = LocalDateSerializer::class)
        @JsonDeserialize(using = LocalDateDeserializer::class)
        @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd")
        val creationDate: LocalDate = LocalDate.now(),
        val status: EducationProgramStatus,
        val isActual: Boolean = true
)