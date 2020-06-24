package com.lgorev.ksuonlineeducation.api

import com.lgorev.ksuonlineeducation.domain.trainingdirection.TrainingDirectionPageRequestModel
import com.lgorev.ksuonlineeducation.domain.trainingdirection.TrainingDirectionRequestModel
import com.lgorev.ksuonlineeducation.service.TrainingDirectionService
import org.springframework.http.ResponseEntity.*
import org.springframework.security.access.prepost.PreAuthorize
import org.springframework.web.bind.annotation.*
import java.util.*

@RestController
@RequestMapping("api/training-directions")
class TrainingDirectionController (private val trainingDirectionService: TrainingDirectionService) {

    @GetMapping
    @PreAuthorize("isAuthenticated()")
    fun getById(@RequestParam id: UUID) =
            ok(trainingDirectionService.getTrainingDirectionById(id))

    @PostMapping("page")
    @PreAuthorize("isAuthenticated()")
    fun getPage(@RequestBody model: TrainingDirectionPageRequestModel) =
            ok(trainingDirectionService.getTrainingDirectionPage(model))

    @PostMapping
    @PreAuthorize("hasAuthority('ADMIN') or hasAuthority('TEACHER')")
    fun add(@RequestBody model: TrainingDirectionRequestModel) = trainingDirectionService.addTrainingDirection(model)

    @PutMapping
    @PreAuthorize("hasAuthority('ADMIN') or hasAuthority('TEACHER')")
    fun update(@RequestBody model: TrainingDirectionRequestModel) = trainingDirectionService.updateTrainingDirection(model)

    @DeleteMapping
    @PreAuthorize("hasAuthority('ADMIN') or hasAuthority('TEACHER')")
    fun delete(@RequestParam id: UUID) =
            ok(trainingDirectionService.deleteTrainingDirection(id))
}