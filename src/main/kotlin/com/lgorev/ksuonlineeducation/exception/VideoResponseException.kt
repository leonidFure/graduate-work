package com.lgorev.ksuonlineeducation.exception

import org.springframework.http.HttpStatus
import org.springframework.web.bind.annotation.ResponseStatus

@ResponseStatus(HttpStatus.BAD_REQUEST)
class VideoResponseException(message: String? = "Ошибка со стронним сервисом видеотрансляций") : RuntimeException(message)
