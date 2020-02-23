package com.lgorev.ksuonlineeducation.domain.lesson

import com.fasterxml.jackson.annotation.JsonFormat
import com.fasterxml.jackson.databind.annotation.JsonDeserialize
import com.fasterxml.jackson.databind.annotation.JsonSerialize
import com.fasterxml.jackson.datatype.jsr310.deser.LocalDateDeserializer
import com.fasterxml.jackson.datatype.jsr310.ser.LocalDateSerializer
import org.springframework.data.domain.Sort
import org.springframework.data.domain.Sort.Direction
import java.time.LocalDate
import java.util.*

data class LessonRequestPageModel(
        val pageNum: Int = 0,
        val pageSize: Int = 10,
        val sortType: Direction = Direction.ASC,
        val courseId: UUID? = null,
        val timetableIds: MutableSet<UUID> = mutableSetOf(),
        val themeIds: MutableSet<UUID> = mutableSetOf(),
        @JsonSerialize(using = LocalDateSerializer::class)
        @JsonDeserialize(using = LocalDateDeserializer::class)
        @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd")
        val fromDate: LocalDate? = null,
        @JsonSerialize(using = LocalDateSerializer::class)
        @JsonDeserialize(using = LocalDateDeserializer::class)
        @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd")
        val toDate: LocalDate? = null,
        val statusFilter: LessonStatus? = null
)