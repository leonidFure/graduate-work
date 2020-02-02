package com.lgorev.ksuonlineeducation.exception

import org.springframework.http.HttpStatus
import org.springframework.web.bind.annotation.ResponseStatus

@ResponseStatus(HttpStatus.BAD_REQUEST)
class UniqueConstraintException(message: String?) : RuntimeException(message)