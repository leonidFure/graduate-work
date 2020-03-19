package com.lgorev.ksuonlineeducation.api

import com.lgorev.ksuonlineeducation.domain.educationprogram.EducationProgramRequestModel
import com.lgorev.ksuonlineeducation.domain.educationprogram.EducationProgramRequestPageModel
import com.lgorev.ksuonlineeducation.service.EducationProgramService
import org.springframework.http.ResponseEntity.*
import org.springframework.security.access.prepost.PreAuthorize
import org.springframework.web.bind.annotation.*
import java.util.*

@RestController
@RequestMapping("api/education-programs")
class EducationProgramController(private val educationProgramService: EducationProgramService) {

    @GetMapping
    @PreAuthorize("isAuthenticated()")
    fun getById(@RequestParam id: UUID) =
            ok(educationProgramService.getEducationProgramById(id))

    @PostMapping("page")
    @PreAuthorize("isAuthenticated()")
    fun getPage(@RequestBody model: EducationProgramRequestPageModel) =
            ok(educationProgramService.getPage(model))

    @PostMapping
    @PreAuthorize("hasAuthority('ADMIN') or hasAuthority('TEACHER')")
    fun add(@RequestBody model: EducationProgramRequestModel) =
            ok(educationProgramService.addEducationProgram(model))

    @PutMapping
    @PreAuthorize("hasAuthority('ADMIN') or hasAuthority('TEACHER')")
    fun update(@RequestBody model: EducationProgramRequestModel) =
            ok(educationProgramService.updateEducationProgram(model))

    @DeleteMapping
    @PreAuthorize("hasAuthority('ADMIN') or hasAuthority('TEACHER')")
    fun delete(@RequestParam id: UUID) =
            ok(educationProgramService.deleteEducationProgram(id))

}