package com.lgorev.ksuonlineeducation.domain.user

import com.fasterxml.jackson.annotation.JsonFormat
import com.fasterxml.jackson.databind.annotation.JsonDeserialize
import com.fasterxml.jackson.databind.annotation.JsonSerialize
import com.fasterxml.jackson.datatype.jsr310.deser.LocalDateDeserializer
import com.fasterxml.jackson.datatype.jsr310.ser.LocalDateSerializer
import java.time.LocalDate
import java.util.*

data class UserRequestModel(
        var id: UUID? = null,
        val firstName: String,
        val lastName: String,
        val patronymic: String? = null,
        val email: String,
        @JsonSerialize(using = LocalDateSerializer::class)
        @JsonDeserialize(using = LocalDateDeserializer::class)
        @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd")
        val startWorkDate: LocalDate? = null,
        val info: String? = null,
        val password: String?,
        val role: Role,
        val facultiesIds: List<UUID>? = null

)