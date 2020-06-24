package com.lgorev.ksuonlineeducation.domain.theme

import java.util.*

data class ThemeRequestModel(
        val id: UUID = UUID.randomUUID(),
        var parentThemeId: UUID? = null,
        val number: Short = 1,
        var educationProgramId: UUID,
        var name: String,
        var description: String
)