package com.lgorev.ksuonlineeducation.domain.educationprogram

import com.fasterxml.jackson.databind.annotation.JsonDeserialize
import com.fasterxml.jackson.databind.annotation.JsonSerialize
import com.fasterxml.jackson.datatype.jsr310.deser.LocalDateTimeDeserializer
import com.fasterxml.jackson.datatype.jsr310.ser.LocalDateTimeSerializer
import org.springframework.data.domain.Sort.Direction
import org.springframework.format.annotation.DateTimeFormat
import java.time.LocalDateTime
import java.util.*

data class EducationProgramRequestPageModel(
        val pageNum: Int = 0,
        val pageSize: Int = 10,
        val sortType: Direction = Direction.ASC,
        val nameFilter: String? = null,
        val statusFilter: EducationProgramStatus? = null,
        val actualFilter: Boolean? = null,
        @JsonSerialize(using = LocalDateTimeSerializer::class)
        @JsonDeserialize(using = LocalDateTimeDeserializer::class)
        @DateTimeFormat
        val creationDateFrom: LocalDateTime? = null,
        @JsonSerialize(using = LocalDateTimeSerializer::class)
        @JsonDeserialize(using = LocalDateTimeDeserializer::class)
        @DateTimeFormat
        val creationDateTo: LocalDateTime? = null,
        val subjectIds: MutableSet<UUID> = mutableSetOf(),
        val teacherId: UUID? = null,
        var ids: List<UUID>? =null
)