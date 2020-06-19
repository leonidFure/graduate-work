package com.lgorev.ksuonlineeducation.domain.educationprogram

import com.fasterxml.jackson.annotation.JsonFormat
import com.fasterxml.jackson.databind.annotation.JsonDeserialize
import com.fasterxml.jackson.databind.annotation.JsonSerialize
import com.fasterxml.jackson.datatype.jsr310.deser.LocalDateDeserializer
import com.fasterxml.jackson.datatype.jsr310.ser.LocalDateSerializer
import java.time.LocalDate
import java.util.*

data class EducationProgramRequestModel(
        val id: UUID = UUID.randomUUID(),
        val subjectId: UUID,
        val name: String,
        val description: String?,
        @JsonSerialize(using = LocalDateSerializer::class)
        @JsonDeserialize(using = LocalDateDeserializer::class)
        @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd")
        val creationDate: LocalDate = LocalDate.now(),
        val status: EducationProgramStatus = EducationProgramStatus.EP_CREATED,
        val isActual: Boolean = true
)

        enum class EducationProgramStatus { EP_DRAFT, EP_AWAITING_CONFIRMATION, EP_CREATED }
