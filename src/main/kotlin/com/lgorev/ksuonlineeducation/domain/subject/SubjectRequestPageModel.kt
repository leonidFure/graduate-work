package com.lgorev.ksuonlineeducation.domain.subject

import org.springframework.data.domain.Sort.Direction

data class SubjectRequestPageModel(
        val pageNum: Int = 1,
        val pageSize: Int = 10,
        val sortType: Direction = Direction.ASC,
        val sortField: String = "name",
        val nameFilter: String? = null,
        val typeFilter: SubjectType? = null
)