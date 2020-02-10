package com.lgorev.ksuonlineeducation.api

import com.lgorev.ksuonlineeducation.domain.educationprogram.EducationProgramRequestModel
import com.lgorev.ksuonlineeducation.domain.educationprogram.EducationProgramRequestPageModel
import com.lgorev.ksuonlineeducation.service.EducationProgramService
import org.springframework.http.ResponseEntity
import org.springframework.http.ResponseEntity.*
import org.springframework.web.bind.annotation.*
import java.util.*

@RestController
@RequestMapping("api/education-programs")
class EducationProgramController(private val educationProgramService: EducationProgramService) {

    @GetMapping
    fun getById(@RequestParam id: UUID) = ok(educationProgramService.getEducationProgramById(id))

            @PostMapping("page")
            fun getPage(@RequestBody model: EducationProgramRequestPageModel) = ok(educationProgramService.getPage(model))

            @PostMapping
            fun add(@RequestBody model: EducationProgramRequestModel) = ok(educationProgramService.addEducationProgram(model))

            @PutMapping
            fun update(@RequestBody model: EducationProgramRequestModel) = ok(educationProgramService.updateEducationProgram(model))

            @DeleteMapping
            fun delete(@RequestParam id: UUID) = ok(educationProgramService.deleteEducationProgram(id))

}