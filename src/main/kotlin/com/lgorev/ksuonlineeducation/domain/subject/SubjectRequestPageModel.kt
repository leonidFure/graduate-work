package com.lgorev.ksuonlineeducation.domain.subject

import org.springframework.data.domain.Sort.Direction
import java.util.*

data class SubjectRequestPageModel(
        val pageNum: Int = 0,
        val pageSize: Int = 10,
        val sortType: Direction = Direction.ASC,
        val nameFilter: String? = null,
        val typeFilter: SubjectType? = null,
        val trainingDirectionIds: MutableSet<UUID> = mutableSetOf()
)