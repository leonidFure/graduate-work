package com.lgorev.ksuonlineeducation.api

import com.lgorev.ksuonlineeducation.domain.timetable.TimetableRequestModel
import com.lgorev.ksuonlineeducation.domain.timetable.TimetablesRequestModel
import com.lgorev.ksuonlineeducation.service.TimetableService
import org.springframework.http.ResponseEntity.ok
import org.springframework.security.access.prepost.PreAuthorize
import org.springframework.web.bind.annotation.*
import java.util.*

@RestController
@RequestMapping("api/timetables")
class TimetableController(private val timetableService: TimetableService) {

    @GetMapping
    @PreAuthorize("isAuthenticated()")
    fun getById(@RequestParam id: UUID) = ok(timetableService.getTimetableById(id))

    @GetMapping("course")
    @PreAuthorize("isAuthenticated()")
    fun getByCourseId(@RequestParam id: UUID) = ok(timetableService.getTimetablesByCourseId(id))

    @PostMapping("timetable")
    @PreAuthorize("hasAuthority('ADMIN') or hasAuthority('TEACHER')")
    fun add(@RequestBody model: TimetableRequestModel) = ok(timetableService.addTimetable(model))

    @PostMapping
    @PreAuthorize("hasAuthority('ADMIN') or hasAuthority('TEACHER')")
    fun addTimetables(@RequestBody model: TimetablesRequestModel) = ok(timetableService.addTimetables(model))

    @PutMapping
    @PreAuthorize("hasAuthority('ADMIN') or hasAuthority('TEACHER')")
    fun update(@RequestBody model: TimetableRequestModel) = ok(timetableService.updateTimetable(model))

    @DeleteMapping
    @PreAuthorize("hasAuthority('ADMIN') or hasAuthority('TEACHER')")
    fun delete(@RequestParam id: UUID) = ok(timetableService.deleteTimetable(id))
}