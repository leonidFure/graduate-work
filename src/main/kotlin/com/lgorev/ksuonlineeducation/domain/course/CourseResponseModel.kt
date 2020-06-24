package com.lgorev.ksuonlineeducation.domain.course

import com.fasterxml.jackson.annotation.JsonFormat
import com.fasterxml.jackson.annotation.JsonInclude
import com.fasterxml.jackson.databind.annotation.JsonDeserialize
import com.fasterxml.jackson.databind.annotation.JsonSerialize
import com.fasterxml.jackson.datatype.jsr310.deser.LocalDateDeserializer
import com.fasterxml.jackson.datatype.jsr310.deser.LocalDateTimeDeserializer
import com.fasterxml.jackson.datatype.jsr310.ser.LocalDateSerializer
import com.fasterxml.jackson.datatype.jsr310.ser.LocalDateTimeSerializer
import com.lgorev.ksuonlineeducation.domain.educationprogram.EducationProgramResponseModel
import org.springframework.format.annotation.DateTimeFormat
import java.time.LocalDate
import java.time.LocalDateTime
import java.util.*

@JsonInclude(JsonInclude.Include.NON_NULL)
data class CourseResponseModel(
        val id: UUID,
        val educationProgramId: UUID,
        var educationProgram: EducationProgramResponseModel? = null,
        val status: CourseStatus,
        @JsonSerialize(using = LocalDateSerializer::class)
        @JsonDeserialize(using = LocalDateDeserializer::class)
        @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd")
        val startDate: LocalDate,
        @JsonSerialize(using = LocalDateSerializer::class)
        @JsonDeserialize(using = LocalDateDeserializer::class)
        @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd")
        val endDate: LocalDate,
        @JsonSerialize(using = LocalDateTimeSerializer::class)
        @JsonDeserialize(using = LocalDateTimeDeserializer::class)
        @DateTimeFormat
        val creationDatetime: LocalDateTime,
        val isActual: Boolean,
        val imageUrl: String,
        var rating: Double? = 0.0,
        var ratingCount: Long? = 0,
        var hasSubscription: Boolean = false,
        val creatorId: UUID?,
        val imageId: UUID?,
        val wowzaLiveEventId: String? = null,
        var subCount: Int? = null
)

