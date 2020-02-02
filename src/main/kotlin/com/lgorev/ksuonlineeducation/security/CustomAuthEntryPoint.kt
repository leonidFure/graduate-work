package com.lgorev.ksuonlineeducation.security

import com.fasterxml.jackson.databind.JsonNode
import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.databind.SerializationFeature
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule
import com.lgorev.ksuonlineeducation.domain.common.GeneralResponse
import org.springframework.http.HttpHeaders
import org.springframework.http.HttpStatus
import org.springframework.http.MediaType
import org.springframework.http.server.ServletServerHttpResponse
import org.springframework.security.core.AuthenticationException
import org.springframework.security.web.AuthenticationEntryPoint
import org.springframework.web.bind.annotation.ControllerAdvice
import org.springframework.web.bind.annotation.ExceptionHandler
import java.time.LocalDateTime
import javax.servlet.http.HttpServletRequest
import javax.servlet.http.HttpServletResponse

@ControllerAdvice
class CustomAuthEntryPoint : AuthenticationEntryPoint {
    override fun commence(request: HttpServletRequest?,
                          response: HttpServletResponse,
                          authException: AuthenticationException?) {
        val mapper = ObjectMapper()
        mapper.registerModule(JavaTimeModule())
        mapper.disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS)

        val generalResponse = GeneralResponse(LocalDateTime.now(), HttpStatus.UNAUTHORIZED, "Вы не авторизовались")

        val httpResponse = ServletServerHttpResponse(response)
        httpResponse.setStatusCode(HttpStatus.UNAUTHORIZED)
        httpResponse.servletResponse.setHeader(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
        httpResponse.body.write(mapper.valueToTree<JsonNode>(generalResponse).binaryValue())
    }

    @ExceptionHandler(value = [org.springframework.security.access.AccessDeniedException::class])
    fun commence(request: HttpServletRequest?,
                 response: HttpServletResponse,
                 authException: org.springframework.security.access.AccessDeniedException) {
        val mapper = ObjectMapper()
        mapper.registerModule(JavaTimeModule())
        mapper.disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS)

        val generalResponse = GeneralResponse(LocalDateTime.now(), HttpStatus.UNAUTHORIZED, "Вы не авторизовались")

        val httpResponse = ServletServerHttpResponse(response)
        httpResponse.setStatusCode(HttpStatus.UNAUTHORIZED)
        httpResponse.servletResponse.setHeader(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
        httpResponse.body.write(mapper.writeValueAsBytes(generalResponse))
    }
}