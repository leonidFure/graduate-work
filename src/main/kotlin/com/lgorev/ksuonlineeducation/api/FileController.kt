package com.lgorev.ksuonlineeducation.api

import com.lgorev.ksuonlineeducation.service.FileStoreService
import com.lgorev.ksuonlineeducation.util.getUserId
import javassist.NotFoundException
import org.springframework.core.io.ByteArrayResource
import org.springframework.http.HttpHeaders
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.security.access.prepost.PreAuthorize
import org.springframework.util.MimeTypeUtils
import org.springframework.web.bind.annotation.*
import org.springframework.web.multipart.MultipartFile
import java.security.Principal
import java.util.*

@RestController
@RequestMapping("api/files")
class FileController(private val fileStoreService: FileStoreService) {
    @PostMapping("avatar")
    @PreAuthorize("hasAuthority('STUDENT')")
    fun addAvatar(@RequestParam image: MultipartFile, principal: Principal) {
        val userId = getUserId(principal)
        if (userId != null) fileStoreService.addAvatar(userId, image)
    }

    @GetMapping("avatar")
    @PreAuthorize("hasAuthority('STUDENT')")
    fun getAvatar(principal: Principal): ResponseEntity<*> {
        val userId = getUserId(principal)
        if (userId != null) {
            val multipartFile = fileStoreService.getAvatar(userId)
            return getFile(multipartFile)
        }
        throw NotFoundException("Пользователь не найлен не найден")
    }

    @GetMapping("avatar/open")
    @PreAuthorize("permitAll()")
    fun getAvatar(@RequestParam id: UUID): ResponseEntity<*> {
        val multipartFile = fileStoreService.getAvatar(id)
        return getFile(multipartFile)
    }

    @PostMapping("course")
    @PreAuthorize("hasAuthority('ADMIN') or hasAuthority('TEACHER')")
    fun addCourseImage(@RequestParam image: MultipartFile, @RequestParam courseId: UUID, principal: Principal) {
        fileStoreService.addCourseImage(courseId, image)
    }

    @GetMapping("course/open")
    @PreAuthorize("permitAll()")
    fun getCourseImage(@RequestParam id: UUID): ResponseEntity<*> {
        val multipartFile = fileStoreService.getCourseImage(id)
        return getFile(multipartFile)
    }

    private fun getFile(multipartFile: MultipartFile): ResponseEntity<ByteArrayResource> {
        val headers = HttpHeaders()
        val resource = ByteArrayResource(multipartFile.bytes)
        val filename = multipartFile.originalFilename
        headers.add(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=${filename}")
        headers.add(HttpHeaders.CONTENT_TYPE, MimeTypeUtils.IMAGE_JPEG_VALUE)
        return ResponseEntity(resource, headers, HttpStatus.OK)
    }
}