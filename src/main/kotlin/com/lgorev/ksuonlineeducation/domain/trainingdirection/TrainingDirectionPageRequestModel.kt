package com.lgorev.ksuonlineeducation.domain.trainingdirection

import org.springframework.data.domain.Sort

data class TrainingDirectionPageRequestModel(
        val pageNum: Int = 0,
        val pageSize: Int = 10,
        val sortType: Sort.Direction = Sort.Direction.ASC,
        val sortField: String = "name",
        val nameFilter: String? = null
)