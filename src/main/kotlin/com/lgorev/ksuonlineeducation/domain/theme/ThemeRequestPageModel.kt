package com.lgorev.ksuonlineeducation.domain.theme

import org.springframework.data.domain.Sort.*
import java.util.*

data class ThemeRequestPageModel(
        val pageNum: Int = 0,
        val pageSize: Int = 10,
        val sortType: Direction = Direction.ASC,
        val sortField: String = "name",
        val nameFilter: String? = null,
        val parentThemeId: UUID? = null,
        val educationProgramId: UUID? = null
)