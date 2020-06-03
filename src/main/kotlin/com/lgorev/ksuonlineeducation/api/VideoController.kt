package com.lgorev.ksuonlineeducation.api

import com.lgorev.ksuonlineeducation.domain.video.VideoPageRequestModel
import com.lgorev.ksuonlineeducation.service.VideoRequestModel
import com.lgorev.ksuonlineeducation.service.VideoService
import org.springframework.http.ResponseEntity.ok
import org.springframework.security.access.prepost.PreAuthorize
import org.springframework.web.bind.annotation.*
import org.springframework.web.multipart.MultipartFile
import java.util.*

@RestController
@RequestMapping("api/videos")
class VideoController(private val videoService: VideoService) {

    @GetMapping
    @PreAuthorize("isAuthenticated()")
    fun getVideoByLessonId(@RequestParam id: UUID) = ok(videoService.getVideoByLessonId(id))

    @PostMapping("page")
    @PreAuthorize("isAuthenticated()")
    fun getVideoPage(@RequestBody model: VideoPageRequestModel) = ok(videoService.getVideoPage(model))

    @PatchMapping
    @PreAuthorize("isAuthenticated()")
    fun updateVideo(@RequestParam id: UUID, @RequestBody model: VideoRequestModel) = ok(videoService.updateVideoInfo(id, model))

    @DeleteMapping
    @PreAuthorize("isAuthenticated()")
    fun deleteVideo(@RequestParam id: UUID) = ok(videoService.deleteVideo(id))

    @PostMapping("{lessonId}")
    @PreAuthorize("isAuthenticated()")
    fun uploadVideo(@RequestBody file: MultipartFile, @PathVariable lessonId: UUID) = ok(videoService.uploadVideo(file, lessonId))
}