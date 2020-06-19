package com.lgorev.ksuonlineeducation.config

import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.databind.SerializationFeature
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule
import com.lgorev.ksuonlineeducation.domain.common.GeneralResponse
import org.springframework.core.annotation.AnnotationUtils
import org.springframework.http.HttpHeaders
import org.springframework.http.HttpStatus
import org.springframework.http.MediaType
import org.springframework.http.server.ServletServerHttpResponse
import org.springframework.web.bind.annotation.ControllerAdvice
import org.springframework.web.bind.annotation.ExceptionHandler
import org.springframework.web.bind.annotation.ResponseStatus
import java.time.LocalDateTime
import javax.servlet.http.HttpServletRequest
import javax.servlet.http.HttpServletResponse

@ControllerAdvice
class ExceptionHandler {
    @ExceptionHandler(RuntimeException::class)
    fun handleException(req: HttpServletRequest, resp: HttpServletResponse, e: Exception) {
        val annotation = AnnotationUtils.findAnnotation(e::class.java, ResponseStatus::class.java)
        if (annotation != null) {
            val mapper = ObjectMapper()
            mapper.registerModule(JavaTimeModule())
            mapper.disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS)

            val generalResponse = GeneralResponse(LocalDateTime.now(), HttpStatus.BAD_REQUEST, e.message
                    ?: "Ошибка сервера")

            val httpResponse = ServletServerHttpResponse(resp)
            httpResponse.setStatusCode(annotation.code)
            httpResponse.servletResponse.setHeader(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
            httpResponse.body.write(mapper.writeValueAsBytes(generalResponse))
        }
    }
}