package com.lgorev.ksuonlineeducation.api

import com.lgorev.ksuonlineeducation.domain.timetable.TimetableRequestModel
import com.lgorev.ksuonlineeducation.service.TimetableService
import org.springframework.web.bind.annotation.*
import java.util.*

@RestController
@RequestMapping("api/timetables")
class TimetableController(private val timetableService: TimetableService) {

    @GetMapping
    fun getById(@RequestParam id: UUID) = timetableService.getTimetableById(id)

    @GetMapping("course")
    fun getByCourseId(@RequestParam id: UUID) = timetableService.getTimetablesByCourseId(id)

    @PostMapping
    fun add(@RequestBody model: TimetableRequestModel) = timetableService.addTimetable(model)

    @PutMapping
    fun update(@RequestBody model: TimetableRequestModel) = timetableService.updateTimetable(model)

    @DeleteMapping
    fun delete(@RequestParam id: UUID) = timetableService.deleteTimetable(id)
}