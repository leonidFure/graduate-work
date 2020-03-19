package com.lgorev.ksuonlineeducation.service

import com.lgorev.ksuonlineeducation.domain.file.FileType
import com.lgorev.ksuonlineeducation.exception.BadRequestException
import com.lgorev.ksuonlineeducation.exception.NotFoundException
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.beans.factory.annotation.Value
import org.springframework.core.io.ResourceLoader
import org.springframework.mock.web.MockMultipartFile
import org.springframework.stereotype.Service
import org.springframework.util.MimeTypeUtils
import org.springframework.web.multipart.MultipartFile
import java.nio.file.Files
import java.nio.file.Paths
import java.util.*

@Service
class FileStoreService(private val resourceLoader: ResourceLoader) {

    @Autowired
    private lateinit var userService: UserService

    @Value("\${file.path.avatar}")
    private lateinit var pathAvatar: String

    @Value("\${file.path.course}")
    private lateinit var pathCourse: String

    private val validImageTypes = listOf(MimeTypeUtils.IMAGE_JPEG_VALUE, MimeTypeUtils.IMAGE_PNG_VALUE)


    @Throws(BadRequestException::class)
    fun addAvatar(userId: UUID, image: MultipartFile) {
        if (!isValidFileFormat(image))
            throw BadRequestException("Неподходящий формат фотографии")
        val fileTypeStr = FileType.AVATAR.toString().toLowerCase()
        val file = Paths.get(pathAvatar, "${userId}_$fileTypeStr.jpg")
        Files.write(file, image.bytes)
        userService.setUserPhotoExists(userId)
    }

    @Throws(NotFoundException::class)
    fun getAvatar(userId: UUID): MockMultipartFile {
        val fileTypeStr = FileType.AVATAR.toString().toLowerCase()
        val filename = "${userId}_$fileTypeStr.jpg"
        val file = Paths.get(pathAvatar, filename)
        try {
            val bytes = Files.readAllBytes(file)
            return MockMultipartFile("${userId}_$fileTypeStr.jpg", bytes)
        } catch (e: Exception) {
            throw NotFoundException("Файл не найден")
        }
    }

    @Throws(BadRequestException::class)
    fun addCourseImage(courseId: UUID, image: MultipartFile) {
        if (!isValidFileFormat(image))
            throw BadRequestException("Неподходящий формат фотографии")
        val fileTypeStr = FileType.COURSE_IMAGE.toString().toLowerCase()
        val file = Paths.get(pathCourse, "${courseId}_$fileTypeStr.jpg")
        Files.write(file, image.bytes)
    }

    @Throws(NotFoundException::class)
    fun getCourseImage(courseId: UUID): MockMultipartFile {
        val fileTypeStr = FileType.COURSE_IMAGE.toString().toLowerCase()
        val filename = "${courseId}_$fileTypeStr.jpg"
        val file = Paths.get(pathCourse, filename)
        try {
            val bytes = Files.readAllBytes(file)
            return MockMultipartFile("${courseId}_$fileTypeStr.jpg", bytes)
        } catch (e: Exception) {
            throw NotFoundException("Файл не найден")
        }
    }

    private fun isValidFileFormat(file: MultipartFile) = validImageTypes.contains(file.contentType)
}