package com.lgorev.ksuonlineeducation.domain.common

import com.fasterxml.jackson.annotation.JsonInclude
import com.fasterxml.jackson.databind.annotation.JsonDeserialize
import com.fasterxml.jackson.databind.annotation.JsonSerialize
import com.fasterxml.jackson.datatype.jsr310.deser.LocalDateTimeDeserializer
import com.fasterxml.jackson.datatype.jsr310.ser.LocalDateTimeSerializer
import org.springframework.format.annotation.DateTimeFormat
import org.springframework.http.HttpStatus
import java.time.LocalDateTime


@JsonInclude(JsonInclude.Include.NON_NULL)
data class GeneralResponse(
        @JsonSerialize(using = LocalDateTimeSerializer::class)
        @JsonDeserialize(using = LocalDateTimeDeserializer::class)
        @DateTimeFormat
        val timestamp: LocalDateTime,
        val status: Int,
        val error: String,
        val message: String
) {
    constructor(timestamp: LocalDateTime, status: HttpStatus, message: String): this(timestamp, status.value(), status.reasonPhrase, message)
}