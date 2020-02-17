package com.lgorev.ksuonlineeducation.domain.trainingdirection

import org.springframework.data.domain.Sort
import org.springframework.data.domain.Sort.Direction
import java.util.*

data class  TrainingDirectionPageRequestModel(
        val pageNum: Int = 0,
        val pageSize: Int = 10,
        val sortType: Direction = Direction.ASC,
        val sortField: String = "name",
        val nameFilter: String? = null,
        val facultyId: UUID? = null,
        val subjectIds: MutableSet<UUID> = mutableSetOf()
)