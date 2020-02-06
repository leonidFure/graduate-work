package com.lgorev.ksuonlineeducation.domain.educationprogram

import org.springframework.data.domain.Sort
import org.springframework.data.domain.Sort.Direction
import java.time.LocalDate

data class EducationProgramRequestPageModel(
        val pageNum: Int = 0,
        val pageSize: Int = 10,
        val sortType: Direction = Direction.ASC,
        val sortField: String = "name",
        val nameFilter: String? = null,
        val statusFilter: EducationProgramStatus? = null,
        val actualFilter: Boolean? = null,
        val creationDateFrom: LocalDate? = null,
        val creationDateTo: LocalDate? = null,
        val directionNameFilter: String? = null,
        val subjectNameFilter: String? = null
)