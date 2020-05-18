package com.lgorev.ksuonlineeducation.api

import com.lgorev.ksuonlineeducation.domain.subject.SubjectListRequestModel
import com.lgorev.ksuonlineeducation.domain.subject.SubjectRequestModel
import com.lgorev.ksuonlineeducation.domain.subject.SubjectRequestPageModel
import com.lgorev.ksuonlineeducation.service.SubjectService
import org.springframework.http.ResponseEntity.*
import org.springframework.security.access.prepost.PreAuthorize
import org.springframework.web.bind.annotation.*
import java.util.*

@RestController
@RequestMapping("api/subjects")
class SubjectController(private val subjectService: SubjectService) {

    @GetMapping
    @PreAuthorize("isAuthenticated()")
    fun getById(@RequestParam id: UUID) = ok(subjectService.getSubjectById(id))

    @PostMapping("page")
    @PreAuthorize("isAuthenticated()")
    fun getPage(@RequestBody model: SubjectRequestPageModel) = ok(subjectService.getSubjectPage(model))

    @PostMapping("list")
    @PreAuthorize("isAuthenticated()")
    fun getList(@RequestBody model: SubjectListRequestModel) = ok(subjectService.getSubjectListByIds(model))

    @PostMapping
    @PreAuthorize("hasAuthority('ADMIN') or hasAuthority('TEACHER')")
    fun add(@RequestBody model: SubjectRequestModel) = ok(subjectService.addSubject(model))

    @PutMapping
    @PreAuthorize("hasAuthority('ADMIN') or hasAuthority('TEACHER')")
    fun update(@RequestBody model: SubjectRequestModel) = ok(subjectService.updateSubject(model))

    @DeleteMapping
    @PreAuthorize("hasAuthority('ADMIN') or hasAuthority('TEACHER')")
    fun delete(@RequestParam id: UUID) = ok(subjectService.deleteSubject(id))
}