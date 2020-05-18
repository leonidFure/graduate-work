package com.lgorev.ksuonlineeducation.api

import com.lgorev.ksuonlineeducation.domain.faculty.FacultyPageRequestModel
import com.lgorev.ksuonlineeducation.domain.faculty.FacultyRequestModel
import com.lgorev.ksuonlineeducation.domain.faculty.TeachersFacultiesModel
import com.lgorev.ksuonlineeducation.service.FacultyService
import com.lgorev.ksuonlineeducation.service.TeachersFacultiesService
import org.springframework.http.ResponseEntity.ok
import org.springframework.security.access.prepost.PreAuthorize
import org.springframework.web.bind.annotation.*
import java.util.*

@RestController
@RequestMapping("api/faculties")
class FacultyController(private val facultyService: FacultyService,
                        private val teachersFacultiesService: TeachersFacultiesService) {

    @GetMapping
    @PreAuthorize("isAuthenticated()")
    fun getById(@RequestParam id: UUID) = ok(facultyService.getFacultyById(id))

    @GetMapping("manager")
    @PreAuthorize("isAuthenticated()")
    fun getByManagerId(@RequestParam id: UUID) = ok(facultyService.getFacultyByManagerId(id))

    @GetMapping("teacher")
    @PreAuthorize("isAuthenticated()")
    fun getByTeacherId(@RequestParam id: UUID) = ok(facultyService.getFacultiesByTeacherId(id))

    /*Нужен ли тут пэйджинг*/
    @PostMapping("page")
    @PreAuthorize("isAuthenticated()")
    fun getPage(@RequestBody model: FacultyPageRequestModel) = ok(facultyService.getFacultyPage(model))

    @PostMapping
    @PreAuthorize("hasAuthority('ADMIN') or hasAuthority('TEACHER')")
    fun add(@RequestBody model: FacultyRequestModel) = ok(facultyService.addFaculty(model))

    @PutMapping
    @PreAuthorize("hasAuthority('ADMIN') or hasAuthority('TEACHER')")
    fun update(@RequestBody model: FacultyRequestModel) = ok(facultyService.updateFaculty(model))

    @DeleteMapping
    @PreAuthorize("hasAuthority('ADMIN') or hasAuthority('TEACHER')")
    fun delete(@RequestParam id: UUID) = ok(facultyService.deleteFaculty(id))

    @PostMapping("teacher/add")
    @PreAuthorize("hasAuthority('ADMIN') or hasAuthority('TEACHER')")
    fun addTeacherToFaculty(@RequestBody model: TeachersFacultiesModel) = ok(teachersFacultiesService.addTeacherToFaculty(model))

    @PostMapping("teacher/remove")
    @PreAuthorize("hasAuthority('ADMIN') or hasAuthority('TEACHER')")
    fun removeTeacherFromFaculty(@RequestBody model: TeachersFacultiesModel) = ok(teachersFacultiesService.removeTeacherFromFaculty(model))
}