package com.lgorev.ksuonlineeducation.api

import com.lgorev.ksuonlineeducation.domain.common.GeneralResponse
import org.springframework.http.HttpStatus
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RestController
import java.time.LocalDateTime
import javax.annotation.security.PermitAll

@RestController
class Oauth2Controller {

    @PermitAll
    @GetMapping("oauth2/redirect")
    fun lol() = GeneralResponse(LocalDateTime.now(), HttpStatus.BAD_REQUEST, "asdsa")

    @PermitAll
    @GetMapping("/error")
    fun error() = GeneralResponse(LocalDateTime.now(), HttpStatus.BAD_REQUEST, "test")

}