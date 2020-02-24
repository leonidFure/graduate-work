package com.lgorev.ksuonlineeducation.api

import com.lgorev.ksuonlineeducation.domain.common.TokenResponseModel
import com.lgorev.ksuonlineeducation.domain.user.TeacherRequestModel
import com.lgorev.ksuonlineeducation.domain.user.UserLoginModel
import com.lgorev.ksuonlineeducation.domain.user.UserRequestModel
import com.lgorev.ksuonlineeducation.security.TokenCredentialContainer
import com.lgorev.ksuonlineeducation.service.AuthService
import org.springframework.http.ResponseEntity.ok
import org.springframework.security.access.prepost.PreAuthorize
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController
import java.security.Principal
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

    @PermitAll
    @PostMapping("register/teacher")
    fun registerTeacher(@RequestBody model: TeacherRequestModel) = ok(authService.register(model))

    @PreAuthorize("hasAuthority('REFRESH_TOKEN')")
    @PostMapping("refresh")
    fun refresh(principal: Principal): TokenResponseModel {
        val user = principal as UsernamePasswordAuthenticationToken
        val tcc = user.credentials as TokenCredentialContainer
        return authService.refresh(principal.name, tcc.sessionId)
    }

    @PreAuthorize("isAuthenticated()")
    @PostMapping("logout")
    fun logout(principal: Principal) {
        val user = principal as UsernamePasswordAuthenticationToken
        val tcc = user.credentials as TokenCredentialContainer
        authService.logout(tcc.sessionId)
    }

    @PermitAll
    @PostMapping("login/google")
    fun loginWithGoogle(@RequestBody modelUser: UserLoginModel) {

    }
//    TODO("Добавить Oauth 2.0, функционал выхода из системы, рефреша токена, авторизации с помощью ВК и Google")
}