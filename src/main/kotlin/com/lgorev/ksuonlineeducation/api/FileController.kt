package com.lgorev.ksuonlineeducation.api

import com.lgorev.ksuonlineeducation.domain.file.FileRequestPageModel
import com.lgorev.ksuonlineeducation.service.FileService
import com.lgorev.ksuonlineeducation.util.getUserId
import org.springframework.core.io.ByteArrayResource
import org.springframework.http.HttpHeaders
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.http.ResponseEntity.ok
import org.springframework.security.access.prepost.PreAuthorize
import org.springframework.util.MimeTypeUtils
import org.springframework.web.bind.annotation.*
import org.springframework.web.multipart.MultipartFile
import java.security.Principal
import java.util.*

@RestController
@RequestMapping("api/files")
class FileController(private val fileService: FileService) {

    @PostMapping("users")
    @PreAuthorize("isAuthenticated()")
    fun addAvatar(@RequestParam image: MultipartFile, principal: Principal) {
        val userId = getUserId(principal)
        if (userId != null) fileService.addUserImage(userId, image)
    }

    @GetMapping("users")
    @PreAuthorize("permitAll()")
    fun getAvatar(@RequestParam id: UUID): ResponseEntity<*> {
        val multipartFile = fileService.getUserImage(id)
        return getFile(multipartFile)
    }

    @PostMapping("courses")
    @PreAuthorize("hasAuthority('ADMIN') or hasAuthority('TEACHER')")
    fun addCourseImage(@RequestParam image: MultipartFile, @RequestParam courseId: UUID) {
        fileService.addCourseImage(courseId, image)
    }

    @GetMapping("courses")
    @PreAuthorize("permitAll()")
    fun getCourseImage(@RequestParam id: UUID): ResponseEntity<*> {
        val multipartFile = fileService.getCourseImage(id)
        return getFile(multipartFile)
    }

    @PostMapping("subjects")
    @PreAuthorize("hasAuthority('ADMIN') or hasAuthority('TEACHER')")
    fun addSubjectImage(@RequestParam image: MultipartFile, @RequestParam subjectId: UUID) {
        fileService.addSubjectImage(subjectId, image)
    }

    @GetMapping("subjects")
    @PreAuthorize("permitAll()")
    fun getSubjectImage(@RequestParam id: UUID): ResponseEntity<*> {
        val multipartFile = fileService.getSubjectImage(id)
        return getFile(multipartFile)
    }

    @PostMapping("lessons")
    @PreAuthorize("hasAuthority('ADMIN') or hasAuthority('TEACHER')")
    fun addLessonFile(@RequestParam file: MultipartFile, @RequestParam lessonId: UUID) {
        fileService.addLessonFile(lessonId, file)
    }

    @GetMapping("lessons")
    @PreAuthorize("permitAll()")
    fun getLessonFile(@RequestParam lessonId: UUID, @RequestParam fileId: UUID): ResponseEntity<*> {
        val multipartFile = fileService.getLessonFile(lessonId, fileId)
        return getFile(multipartFile)
    }

    @GetMapping("lessons/page")
    @PreAuthorize("isAuthenticated()")
    fun getPage(model: FileRequestPageModel) = ok(fileService.getPage(model))

    @DeleteMapping("lessons")
    @PreAuthorize("hasAuthority('ADMIN') or hasAuthority('TEACHER')")
    fun deleteLessonFile(@RequestParam lessonId: UUID, @RequestParam fileId: UUID) {
        fileService.deleteLessonFile(lessonId, fileId)
    }

    private fun getFile(multipartFile: MultipartFile): ResponseEntity<ByteArrayResource> {
        val headers = HttpHeaders()
        val resource = ByteArrayResource(multipartFile.bytes)
        val filename = multipartFile.originalFilename
        headers.add(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=${filename}")
        headers.add(HttpHeaders.CONTENT_TYPE, multipartFile.contentType)
        return ResponseEntity(resource, headers, HttpStatus.OK)
    }
}