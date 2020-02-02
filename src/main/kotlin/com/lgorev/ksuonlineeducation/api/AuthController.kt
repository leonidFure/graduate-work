package com.lgorev.ksuonlineeducation.api

import com.lgorev.ksuonlineeducation.domain.user.UserLoginModel
import com.lgorev.ksuonlineeducation.domain.user.UserRequestModel
import com.lgorev.ksuonlineeducation.service.AuthService
import org.springframework.http.ResponseEntity.*
import org.springframework.web.bind.annotation.*
import javax.annotation.security.PermitAll

@RestController
@RequestMapping("api/auth")
class AuthController(private val authService: AuthService) {

    @PermitAll
    @PostMapping("login")
    fun login(@RequestBody modelUser: UserLoginModel) = ok(authService.login(modelUser))

    @PermitAll
    @PostMapping("register")
    fun register(@RequestBody model: UserRequestModel) = ok(authService.register(model))

}