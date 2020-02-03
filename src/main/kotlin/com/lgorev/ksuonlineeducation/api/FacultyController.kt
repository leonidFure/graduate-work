package com.lgorev.ksuonlineeducation.api

import com.lgorev.ksuonlineeducation.domain.faculty.FacultyRequestModel
import com.lgorev.ksuonlineeducation.service.FacultyService
import org.springframework.data.domain.PageRequest
import org.springframework.data.domain.Sort
import org.springframework.http.ResponseEntity.ok
import org.springframework.security.access.prepost.PreAuthorize
import org.springframework.web.bind.annotation.*
import java.util.*

@RestController
@RequestMapping("api/faculties")
class FacultyController(private val facultyService: FacultyService) {

    @GetMapping
    @PreAuthorize("isAuthenticated()")
    fun getById(@RequestParam id: UUID) = ok(facultyService.getFacultyById(id))

    @GetMapping("page")
    @PreAuthorize("isAuthenticated()")
    fun getPage(@RequestParam page: Int = 0,
                @RequestParam size: Int = 10,
                @RequestParam sort: Sort.Direction = Sort.Direction.ASC) =
            ok(facultyService.getFacultyPage(PageRequest.of(page, size, sort, "id")))

    @PostMapping
    @PreAuthorize("hasAuthority('ADMIN') or hasAuthority('TEACHER')")
    fun add(@RequestBody model: FacultyRequestModel) = ok(facultyService.addFaculty(model))

    @PutMapping
    @PreAuthorize("hasAuthority('ADMIN') or hasAuthority('TEACHER')")
    fun update(@RequestBody model: FacultyRequestModel) = ok(facultyService.updateFaculty(model))

    @DeleteMapping
    @PreAuthorize("hasAuthority('ADMIN') or hasAuthority('TEACHER')")
    fun delete(@RequestParam id: UUID) = ok(facultyService.deleteFaculty(id))
}