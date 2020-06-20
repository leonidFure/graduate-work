package com.lgorev.ksuonlineeducation.api

import com.lgorev.ksuonlineeducation.domain.user.*
import com.lgorev.ksuonlineeducation.exception.AuthException
import com.lgorev.ksuonlineeducation.exception.BadRequestException
import com.lgorev.ksuonlineeducation.exception.NotFoundException
import com.lgorev.ksuonlineeducation.service.UserService
import com.lgorev.ksuonlineeducation.util.getRole
import com.lgorev.ksuonlineeducation.util.getUserId
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.http.ResponseEntity.*
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

    @GetMapping("self")
    @PreAuthorize("isAuthenticated()")
    fun getUserSelf(principal: Principal): ResponseEntity<UserResponseModel> {
        val userId = getUserId(principal)
        return if (userId != null)
            ok(userService.getUserById(userId))
        else
            throw NotFoundException("Пользователь не найден")
    }

    @PutMapping
    @PreAuthorize("isAuthenticated()")
    fun updateUser(@RequestBody model: UserRequestModel, principal: Principal): UserResponseModel {
        val userId = getUserId(principal)
        if (getRole(principal) != Role.ADMIN && model.id != null && userId != model.id)
            throw AuthException("Вы не можете поменять данные другого пользователя")

        if (model.id == null) model.id = userId
        return userService.updateUser(model)
    }

    @PostMapping
    @PreAuthorize("hasAnyAuthority('ADMIN')")
    fun saveUser(@RequestBody model: UserRequestModel) = userService.saveUser(model)

    @PatchMapping("lock")
    @PreAuthorize("hasAuthority('ADMIN')")
    fun setUserIsActive(@RequestParam id: UUID, principal: Principal): ResponseEntity<*> {
        val userId = getUserId(principal)
        return if (userId != id) ok(userService.setUserNotActive(id))
        else throw BadRequestException("Вы не можете заблокировать себя")
    }

    @PatchMapping("password")
    @PreAuthorize("hasAuthority('ADMIN') or hasAuthority('TEACHER') or hasAuthority('STUDENT')")
    @ResponseStatus(HttpStatus.OK)
    fun updatePassword(@RequestBody model: PasswordModel, principal: Principal) {
        val userId = getUserId(principal)

        if (getRole(principal) != Role.ADMIN && model.id != null && userId != model.id)
            throw AuthException("Вы не можете поменять пароль другому пользователю")

        if (model.id != null) model.id = userId
        userService.updatePassword(model)
    }

    @PostMapping("page")
    @PreAuthorize("isAuthenticated()")
    fun getPage(@RequestBody model: UserPageRequestModel, principal: Principal) = userService.getPage(model, principal)

    @GetMapping("list")
    @PreAuthorize("isAuthenticated()")
    fun getAllUsers() = ok(userService.getAllUsers())

    @GetMapping("list/{course_id}/teachers")
    @PreAuthorize("isAuthenticated()")
    fun getAllCourseTeachers(@PathVariable("course_id") id: UUID) = ok(userService.getCourseTeachers(id))

    @GetMapping("list/{course_id}/not/teachers")
    @PreAuthorize("isAuthenticated()")
    fun getNotCourseTeachers(@PathVariable("course_id") id: UUID) = ok(userService.getNotCourseTeachers(id))

    @GetMapping("list/{course_id}/subs")
    @PreAuthorize("isAuthenticated()")
    fun getAllCourseSubs(@PathVariable("course_id") id: UUID) = ok(userService.getCourseSubs(id))
}