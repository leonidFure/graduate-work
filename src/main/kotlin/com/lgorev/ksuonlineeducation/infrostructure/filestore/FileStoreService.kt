package com.lgorev.ksuonlineeducation.infrostructure.filestore

import com.lgorev.ksuonlineeducation.domain.file.FileModel
import com.lgorev.ksuonlineeducation.domain.file.FileType
import com.lgorev.ksuonlineeducation.domain.file.FileType.*
import com.lgorev.ksuonlineeducation.exception.BadRequestException
import com.lgorev.ksuonlineeducation.exception.NotFoundException
import com.lgorev.ksuonlineeducation.repository.file.FileEntity
import com.lgorev.ksuonlineeducation.util.*
import org.springframework.beans.factory.annotation.Value
import org.springframework.core.io.ResourceLoader
import org.springframework.http.MediaType.*
import org.springframework.mock.web.MockMultipartFile
import org.springframework.stereotype.Service
import org.springframework.web.multipart.MultipartFile
import java.nio.file.Files
import java.nio.file.Path
import java.nio.file.Paths
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import java.util.*

@Service
class FileStoreService(private val resourceLoader: ResourceLoader) {
    @Value("\${file.path.avatar}")
    private lateinit var pathAvatar: String

    @Value("\${file.path.course}")
    private lateinit var pathCourse: String

    @Value("\${file.path.subject}")
    private lateinit var pathSubject: String

    @Value("\${file.path.lessons}")
    private lateinit var pathLessons: String

    private val validImageTypes = listOf(IMAGE_JPEG_VALUE, IMAGE_PNG_VALUE)
    private val validFileTypes = listOf(
            // излбражения
            IMAGE_JPEG_VALUE, IMAGE_PNG_VALUE, IMAGE_GIF_VALUE, TEXT_HTML_VALUE, TEXT_PLAIN_VALUE, APPLICATION_PDF_VALUE,
            // текстовые документыдокументы
            DOC, DOT, DOCX, DOTX, DOCM, DOTM,
            // excel документы
            XLS, XLT, XLA, XLSX, XLTX, XLSM, XLTM, XLAM, XLSB,
            // презентации
            PPT, POT, PPS, PPA,
            PPTX, POTX, PPSX, PPAM, PPTM, POTM, PPSM,
            // Microsoft Access
            MDB
    )

    @Throws(BadRequestException::class)
    fun saveFile(file: MultipartFile, id: UUID, fileType: FileType): FileEntity {
        if (!isValidFile(file)) throw BadRequestException("Невалидынй файл")
        val originalFilename = file.originalFilename ?: throw BadRequestException("Невалидынй файл")
        val formatter = DateTimeFormatter.ofPattern("dd-MM-yyyy.hh_mm_ss")
        val nowStr = LocalDateTime.now().format(formatter)
        val fileName = "${nowStr}_${originalFilename}"
        if (!Files.exists(Path.of("$pathLessons/$id")))
            Files.createDirectory(Path.of("$pathLessons/$id"))
        val newFile = Paths.get("$pathLessons/$id", fileName)
        Files.write(newFile, file.bytes)
        return FileEntity(name = fileName, type = fileType, uploadingDateTime = LocalDateTime.now(), contentType = file.contentType)
    }

    @Throws(BadRequestException::class)
    fun saveImage(file: MultipartFile, id: UUID, fileType: FileType): FileEntity {
        if (!isValidImage(file)) throw BadRequestException("Невалидынй файл")
        val originalFilename = file.originalFilename ?: throw BadRequestException("Невалидынй файл")

        val extension = originalFilename.substring(originalFilename.lastIndexOf('.'))
        val fileName = fileType.toString().toLowerCase()
        val fullFileName = "$fileName$extension"
        val newFile = when (fileType) {
            AVATAR -> {
                val path = Path.of("$pathLessons/$id")
                if(!Files.exists(path)) Files.createDirectory(path)
                Paths.get("$pathLessons/$id", fullFileName)
            }
            COURSE_IMAGE -> {
                val path = Path.of("$pathCourse/$id")
                if(!Files.exists(path)) Files.createDirectory(path)
                Paths.get("$pathCourse/$id", fullFileName)
            }
            SUBJECT_IMAGE -> {
                val path = Path.of("$pathSubject/$id")
                if(!Files.exists(path)) Files.createDirectory(path)
                Paths.get("$pathSubject/$id", fullFileName)
            }
            else -> throw BadRequestException("Невалидынй файл")
        }
        Files.write(newFile, file.bytes)
        return FileEntity(name = fullFileName, type = fileType, uploadingDateTime = LocalDateTime.now(), contentType = file.contentType)
    }

    @Throws(NotFoundException::class)
    fun getFile(id: UUID, model: FileModel): MockMultipartFile {
        val file = when (model.type) {
            AVATAR -> Paths.get("$pathLessons/$id", model.name)
            COURSE_IMAGE -> Paths.get("$pathCourse/$id", model.name)
            SUBJECT_IMAGE -> Paths.get("$pathSubject/$id", model.name)
            LESSON_FILE -> Paths.get("$pathLessons/$id", model.name)
        }
        try {
            val bytes = Files.readAllBytes(file)
            return MockMultipartFile(model.name, model.name, model.contentType, bytes)
        } catch (e: Exception) {
            throw NotFoundException("Файл не найден")
        }
    }

    private fun isValidFile(file: MultipartFile) = if (isValidFileSize(file)) isValidFileType(file) else false

    private fun isValidImage(file: MultipartFile) = if (isValidFileSize(file)) isValidImageType(file) else false

    private fun isValidImageType(file: MultipartFile) = validImageTypes.contains(file.contentType)

    private fun isValidFileType(file: MultipartFile) = validFileTypes.contains(file.contentType)

    private fun isValidFileSize(file: MultipartFile) = file.size < MAX_VALID_SIZE

    fun deleteLessonFile(lessonId: UUID, file: FileModel) {
        val path = Path.of("$pathLessons/$lessonId/${file.name}")
        if (Files.exists(path)) Files.delete(path)
    }

    private companion object {
        private const val MAX_VALID_SIZE = 100 * 1048576L
    }
}