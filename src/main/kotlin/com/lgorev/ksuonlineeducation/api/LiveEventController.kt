package com.lgorev.ksuonlineeducation.api

import com.lgorev.ksuonlineeducation.service.LiveEventService
import org.springframework.security.access.prepost.PreAuthorize
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("api/live-event")
class LiveEventController (private val liveEventService: LiveEventService) {

    @GetMapping
    fun getLiveEvent(@RequestParam id: String) = liveEventService.getLiveEvent(id)

    @PostMapping("{id}/start")
    @PreAuthorize("hasAuthority('ADMIN') or hasAuthority('TEACHER')")
    fun startLiveEvent(@PathVariable id: String) {liveEventService.startLiveEvent(id)}

    @PostMapping("{id}/stop")
    @PreAuthorize("hasAuthority('ADMIN') or hasAuthority('TEACHER')")
    fun stopLiveEvent(@PathVariable id: String) {liveEventService.stopLiveEvent(id)}
}