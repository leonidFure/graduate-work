package com.lgorev.ksuonlineeducation.api

import com.lgorev.ksuonlineeducation.domain.trainingdirection.TrainingDirectionRequestModel
import com.lgorev.ksuonlineeducation.service.TrainingDirectionService
import org.springframework.data.domain.PageRequest
import org.springframework.data.domain.Sort
import org.springframework.http.ResponseEntity.*
import org.springframework.security.access.prepost.PreAuthorize
import org.springframework.web.bind.annotation.*
import java.util.*

@RestController
@RequestMapping("api/training-directions")
class TrainingDirectionController (private val trainingDirectionService: TrainingDirectionService) {

    @GetMapping
    @PreAuthorize("hasAuthority('ADMIN') or hasAuthority('TEACHER')")
    fun getById(@RequestParam id: UUID) = ok(trainingDirectionService.getTrainingDirectionById(id))

    @GetMapping("page")
    @PreAuthorize("hasAuthority('ADMIN') or hasAuthority('TEACHER')")
    fun getPage(@RequestParam page: Int = 0,
                @RequestParam size: Int = 10,
                @RequestParam sort: Sort.Direction = Sort.Direction.ASC) =
            ok(trainingDirectionService.getTrainingDirectionPage(PageRequest.of(page, size, sort, "id")))

    @PostMapping
    @PreAuthorize("hasAuthority('ADMIN') or hasAuthority('TEACHER')")
    fun add(@RequestBody model: TrainingDirectionRequestModel) = ok(trainingDirectionService.addTrainingDirection(model))

    @PutMapping
    @PreAuthorize("hasAuthority('ADMIN') or hasAuthority('TEACHER')")
    fun update(@RequestBody model: TrainingDirectionRequestModel) = ok(trainingDirectionService.updateTrainingDirection(model))

    @DeleteMapping
    @PreAuthorize("hasAuthority('ADMIN') or hasAuthority('TEACHER')")
    fun delete(@RequestParam id: UUID) = ok(trainingDirectionService.deleteTrainingDirection(id))
}