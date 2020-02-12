package com.lgorev.ksuonlineeducation.domain.lesson

import org.springframework.data.domain.Sort
import java.time.LocalDate
import java.util.*

data class LessonRequestPageModel(
        val pageNum: Int = 0,
        val pageSize: Int = 10,
        val sortType: Sort.Direction = Sort.Direction.ASC,
        val sortField: String = "date",
        val timetableId: UUID? = null,
        val fromDate: LocalDate? = null,
        val toDate: LocalDate? = null,
        val statusFilter: LessonStatus? = null
)