package com.lgorev.ksuonlineeducation.domain.course

import com.fasterxml.jackson.annotation.JsonFormat
import com.fasterxml.jackson.databind.annotation.JsonDeserialize
import com.fasterxml.jackson.databind.annotation.JsonSerialize
import com.fasterxml.jackson.datatype.jsr310.deser.LocalDateDeserializer
import com.fasterxml.jackson.datatype.jsr310.ser.LocalDateSerializer
import org.springframework.data.domain.Sort.Direction
import org.springframework.data.domain.Sort.Direction.*
import java.time.LocalDate
import java.util.*

data class CourseRequestPageModel(
        val pageNum: Int = 0,
        val pageSize: Int = 10,
        val sortType: Direction = ASC,
        val statusFilter: CourseStatus? = null,
        val actualFilter: Boolean? = null,
        @JsonSerialize(using = LocalDateSerializer::class)
        @JsonDeserialize(using = LocalDateDeserializer::class)
        @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd")
        val dateFrom: LocalDate? = null,
        @JsonSerialize(using = LocalDateSerializer::class)
        @JsonDeserialize(using = LocalDateDeserializer::class)
        @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd")
        val dateTo: LocalDate? = null,
        @JsonSerialize(using = LocalDateSerializer::class)
        @JsonDeserialize(using = LocalDateDeserializer::class)
        @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd")
        val creationDateFrom: LocalDate? = null,
        @JsonSerialize(using = LocalDateSerializer::class)
        @JsonDeserialize(using = LocalDateDeserializer::class)
        @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd")
        val creationDateTo: LocalDate? = null,
        val educationProgramId: UUID? = null,
        var ids: MutableSet<UUID>? = null,
        val nameFilter: String? = null,
        var educationProgramIds: MutableSet<UUID>? = null,
        val isSelf: Boolean = false,
        var userId: UUID? = null,
        var subscriberId: UUID? = null,
        val sortField: String = "startDate"
)