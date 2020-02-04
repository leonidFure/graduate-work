package com.lgorev.ksuonlineeducation.domain.departments

import org.springframework.data.domain.Sort.Direction

data class DepartmentPageRequestModel(
        val pageNum: Int = 0,
        val pageSize: Int = 10,
        val sortType: Direction = Direction.ASC,
        val sortField: String = "name",
        val nameFilter: String? = null
)