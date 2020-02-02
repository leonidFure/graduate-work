package com.lgorev.ksuonlineeducation.domain.group

import com.fasterxml.jackson.annotation.JsonInclude
import java.util.*

@JsonInclude(JsonInclude.Include.NON_NULL)
data class GroupResponseModel(
        val id: UUID,
        val name: String,
        val trainingDirectionId: UUID
)