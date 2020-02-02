package com.lgorev.ksuonlineeducation.api

import com.lgorev.ksuonlineeducation.domain.group.GroupRequestModel
import com.lgorev.ksuonlineeducation.service.GroupService
import org.springframework.data.domain.PageRequest
import org.springframework.data.domain.Sort
import org.springframework.http.ResponseEntity.ok
import org.springframework.security.access.prepost.PreAuthorize
import org.springframework.web.bind.annotation.*
import java.util.*

@RestController
@RequestMapping("api/groups")
class GroupController(private val groupService: GroupService) {
    @GetMapping
    @PreAuthorize("hasAuthority('ADMIN') or hasAuthority('TEACHER') or hasAuthority('STUDENT')")
    fun getById(@RequestParam id: UUID) = ok(groupService.getGroupById(id))

    @GetMapping("page")
    @PreAuthorize("hasAuthority('ADMIN') or hasAuthority('TEACHER') or hasAuthority('STUDENT')")
    fun getPage(@RequestParam page: Int = 0,
                @RequestParam size: Int = 10,
                @RequestParam sort: Sort.Direction = Sort.Direction.ASC) =
            ok(groupService.getGroupPage(PageRequest.of(page, size, sort, "id")))

    @PostMapping
    @PreAuthorize("hasAuthority('ADMIN') or hasAuthority('TEACHER')")
    fun add(@RequestBody model: GroupRequestModel) = ok(groupService.addGroup(model))

    @PutMapping
    @PreAuthorize("hasAuthority('ADMIN') or hasAuthority('TEACHER')")
    fun update(@RequestBody model: GroupRequestModel) = ok(groupService.updateGroup(model))

    @DeleteMapping
    @PreAuthorize("hasAuthority('ADMIN') or hasAuthority('TEACHER')")
    fun delete(@RequestParam id: UUID) = ok(groupService.deleteGroup(id))

    @PostMapping("user")
    @PreAuthorize("hasAuthority('ADMIN') or hasAuthority('TEACHER')")
    fun addUserToGroup(@RequestParam userId: UUID, @RequestParam groupId: UUID) =
            ok(groupService.addStudentToGroup(userId, groupId))

    @DeleteMapping("user")
    @PreAuthorize("hasAuthority('ADMIN') or hasAuthority('TEACHER')")
    fun removeUserToGroup(@RequestParam userId: UUID, @RequestParam groupId: UUID) =
            ok(groupService.removeStudentFromGroup(userId, groupId))
}