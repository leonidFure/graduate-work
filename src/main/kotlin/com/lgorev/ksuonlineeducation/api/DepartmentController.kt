package com.lgorev.ksuonlineeducation.api

import com.lgorev.ksuonlineeducation.domain.departments.DepartmentRequestModel
import com.lgorev.ksuonlineeducation.service.DepartmentService
import org.springframework.data.domain.PageRequest
import org.springframework.data.domain.Sort
import org.springframework.http.ResponseEntity.*
import org.springframework.security.access.prepost.PreAuthorize
import org.springframework.web.bind.annotation.*
import java.util.*

@RestController
@RequestMapping("api/departments")
class DepartmentController(private val departmentService: DepartmentService) {


    @GetMapping
    @PreAuthorize("hasAuthority('ADMIN') or hasAuthority('TEACHER')")
    fun getById(@RequestParam id: UUID) = ok(departmentService.getDepartmentById(id))

    @GetMapping("page")
    @PreAuthorize("hasAuthority('ADMIN') or hasAuthority('TEACHER')")
    fun getPage(@RequestParam page: Int = 0,
                @RequestParam size: Int = 10,
                @RequestParam sort: Sort.Direction = Sort.Direction.ASC) =
            ok(departmentService.getDepartmentPage(PageRequest.of(page, size, sort, "id")))

    @PostMapping
    @PreAuthorize("hasAuthority('ADMIN') or hasAuthority('TEACHER')")
    fun add(@RequestBody model: DepartmentRequestModel) = ok(departmentService.addDepartment(model))

    @PutMapping
    @PreAuthorize("hasAuthority('ADMIN') or hasAuthority('TEACHER')")
    fun update(@RequestBody model: DepartmentRequestModel) = ok(departmentService.updateDepartment(model))

    @DeleteMapping
    @PreAuthorize("hasAuthority('ADMIN') or hasAuthority('TEACHER')")
    fun delete(@RequestParam id: UUID) = ok(departmentService.deleteDepartment(id))
}