package com.lgorev.ksuonlineeducation.domain.theme

import java.util.*

data class ThemeResponseModel(
        val id: UUID,
        var parentThemeId: UUID?,
        var number: Short,
        var educationProgramId: UUID,
        var name: String,
        var description: String
)
