package com.lgorev.ksuonlineeducation.domain.group

import java.util.*

data class GroupRequestModel(
        val id: UUID = UUID.randomUUID(),
        val name: String,
        val trainingDirectionId: UUID
)