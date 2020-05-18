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

    @Autowired
    private lateinit var courseService: CourseService

    @Autowired
    private lateinit var subjectService: SubjectService

    @Value("\${file.path.avatar}")
    private lateinit var pathAvatar: String

    @Value("\${file.path.course}")
    private lateinit var pathCourse: String
    @Value("\${file.path.subject}")
    private lateinit var pathSubject: String

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

    fun getCourseImage(courseId: UUID): MockMultipartFile {
        val courseFileTypeStr = FileType.COURSE_IMAGE.toString().toLowerCase()
        val subjectFileTypeStr = FileType.SUBJECT_IMAGE.toString().toLowerCase()
        val courseFileName = "${courseId}_$courseFileTypeStr.jpg"
        val defaultFileName = "default.png"
        val courseFile = Paths.get(pathCourse, courseFileName)
        val courseFileExists = Files.exists(courseFile)

        if (courseFileExists) {
            val bytes = Files.readAllBytes(courseFile)
            return MockMultipartFile("${courseId}_$courseFileTypeStr.jpg", bytes)
        } else {
            val subjectId = courseService.getSubjectId(courseId)
            if (subjectId != null) {
                val subjectFileName = "${subjectId}_$subjectFileTypeStr.jpg"
                val subjectFile = Paths.get(pathSubject, subjectFileName)
                val subjectFileExists = Files.exists(subjectFile)
                if (subjectFileExists) {
                    val bytes = Files.readAllBytes(subjectFile)
                    return MockMultipartFile("${courseId}_$courseFileTypeStr.jpg", bytes)
                }
            }
        }
        val defaultFile = Paths.get(pathCourse, defaultFileName)
        val bytes = Files.readAllBytes(defaultFile)
        return MockMultipartFile("${courseId}_$courseFileTypeStr.jpg", bytes)
    }


    @Throws(BadRequestException::class)
    fun addSubjectImage(subjectId: UUID, image: MultipartFile) {
        if (!isValidFileFormat(image))
            throw BadRequestException("Неподходящий формат фотографии")
        val fileTypeStr = FileType.SUBJECT_IMAGE.toString().toLowerCase()
        val file = Paths.get(pathSubject, "${subjectId}_$fileTypeStr.jpg")
        Files.write(file, image.bytes)
    }

    private fun isValidFileFormat(file: MultipartFile) = validImageTypes.contains(file.contentType)
}