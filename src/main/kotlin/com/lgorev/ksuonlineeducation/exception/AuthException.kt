package com.lgorev.ksuonlineeducation.exception

import org.springframework.http.HttpStatus
import org.springframework.web.bind.annotation.ResponseStatus

@ResponseStatus(HttpStatus.FORBIDDEN)
class AuthException(message: String?) : RuntimeException(message)