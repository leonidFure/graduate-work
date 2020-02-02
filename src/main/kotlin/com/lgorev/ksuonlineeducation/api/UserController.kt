package com.lgorev.ksuonlineeducation.api

import com.lgorev.ksuonlineeducation.domain.user.PasswordModel
import com.lgorev.ksuonlineeducation.domain.user.UserRequestModel
import com.lgorev.ksuonlineeducation.service.UserService
import com.lgorev.ksuonlineeducation.util.getUserId
import org.springframework.data.domain.PageRequest
import org.springframework.data.domain.Sort.Direction
import org.springframework.data.domain.Sort.Direction.*
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity.ok
import org.springframework.security.access.prepost.PreAuthorize
import org.springframework.web.bind.annotation.*
import java.security.Principal
import java.util.*

@RestController
@RequestMapping("api/users")
class UserController(private val userService: UserService) {

    @GetMapping(params = ["id"])
    @PreAuthorize("isAuthenticated()")
    fun getUserById(@RequestParam id: UUID) = ok(userService.getUserById(id))

    @GetMapping("page", params = ["page", "size", "sort"])
    @PreAuthorize("isAuthenticated()")
    fun getUserPage(@RequestParam page: Int = 0, @RequestParam size: Int = 10, @RequestParam sort: Direction = ASC) =
            ok(userService.loadPage(PageRequest.of(page, size, sort, "id")))

    @PutMapping
    @PreAuthorize("isAuthenticated()")
    fun updateUser(@RequestBody model: UserRequestModel) = ok(userService.updateUser(model))

    @PatchMapping("active")
    @PreAuthorize("hasAuthority('ADMIN')")
    fun setUserIsActive(@RequestParam id: UUID) = ok(userService.setUserNotActive(id))

    @PatchMapping("password")
    @PreAuthorize("hasAuthority('TEACHER') or hasAuthority('STUDENT')")
    @ResponseStatus(HttpStatus.OK)
    fun updatePassword(@RequestBody model: PasswordModel, principal: Principal) {
        val userId = getUserId(principal)
        if (model.id != null) model.id = userId
        userService.updatePassword(model)
    }
}