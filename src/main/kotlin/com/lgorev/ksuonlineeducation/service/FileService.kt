package com.lgorev.ksuonlineeducation.service

import com.lgorev.ksuonlineeducation.domain.common.PageResponseModel
import com.lgorev.ksuonlineeducation.domain.common.forEach
import com.lgorev.ksuonlineeducation.domain.common.map
import com.lgorev.ksuonlineeducation.domain.file.FileModel
import com.lgorev.ksuonlineeducation.domain.file.FileRequestPageModel
import com.lgorev.ksuonlineeducation.domain.file.FileType.*
import com.lgorev.ksuonlineeducation.exception.BadRequestException
import com.lgorev.ksuonlineeducation.exception.NotFoundException
import com.lgorev.ksuonlineeducation.infrostructure.filestore.FileStoreService
import com.lgorev.ksuonlineeducation.repository.file.FileEntity
import com.lgorev.ksuonlineeducation.repository.file.FileRepository
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.beans.factory.annotation.Value
import org.springframework.data.repository.findByIdOrNull
import org.springframework.mock.web.MockMultipartFile
import org.springframework.stereotype.Service
import org.springframework.web.multipart.MultipartFile
import java.util.*

@Service
class FileService(private val fileRepository: FileRepository) {

    @Autowired
    private lateinit var fileStoreService: FileStoreService

    @Autowired
    private lateinit var userService: UserService

    @Autowired
    private lateinit var courseService: CourseService

    @Autowired
    private lateinit var subjectService: SubjectService

    @Autowired
    private lateinit var lessonsFilesService: LessonsFilesService

    @Value("\${file.max-count}")
    private lateinit var maxCount: String

    @Throws(BadRequestException::class)
    fun addUserImage(userId: UUID, image: MultipartFile) {
        val fileEntity = fileStoreService.saveImage(image, userId, AVATAR)
        fileRepository.save(fileEntity)
        userService.setImageId(userId, fileEntity.id)
    }

    @Throws(NotFoundException::class)
    fun getUserImage(userId: UUID): MockMultipartFile {
        val user = userService.getUserById(userId)
        if (user.imageId == null)
            throw NotFoundException("У пользователя нет изображения")
        fileRepository.findByIdOrNull(user.imageId)?.let { file ->
            return fileStoreService.getFile(userId, file.toModel())
        }
        throw NotFoundException("У пользователя нет изображения")
    }

    @Throws(BadRequestException::class)
    fun addCourseImage(courseId: UUID, image: MultipartFile) {
        val fileEntity = fileStoreService.saveImage(image, courseId, COURSE_IMAGE)
        fileRepository.save(fileEntity)
        courseService.setImageId(courseId, fileEntity.id)
    }

    @Throws(NotFoundException::class)
    fun getCourseImage(courseId: UUID): MockMultipartFile {
        val course = courseService.getCourseById(courseId, null)
        if (course.imageId == null)
            throw NotFoundException("У курса нет изображения")
        fileRepository.findByIdOrNull(course.imageId)?.let { file ->
            return fileStoreService.getFile(courseId, file.toModel())
        }
        throw NotFoundException("У курса нет изображения")
    }

    @Throws(BadRequestException::class)
    fun addSubjectImage(subjectId: UUID, image: MultipartFile) {
        val fileEntity = fileStoreService.saveImage(image, subjectId, SUBJECT_IMAGE)
        fileRepository.save(fileEntity)
        subjectService.setImageId(subjectId, fileEntity.id)
    }

    @Throws(NotFoundException::class)
    fun getSubjectImage(subjectId: UUID): MockMultipartFile {
        val subject = subjectService.getSubjectById(subjectId)
        if (subject.imageId == null)
            throw NotFoundException("У предмета нет изображения")
        fileRepository.findByIdOrNull(subject.imageId)?.let { file ->
            return fileStoreService.getFile(subjectId, file.toModel())
        }
        throw NotFoundException("У предмета нет изображения")
    }

    @Throws(NotFoundException::class)
    fun getLessonFile(lessonId: UUID, fileId: UUID): MockMultipartFile {
        val lessonsFilesModel = lessonsFilesService.getLessonsFilesById(lessonId, fileId)
        fileRepository.findByIdOrNull(lessonsFilesModel.fileId)?.let { file ->
            return fileStoreService.getFile(lessonId, file.toModel())
        }
        throw NotFoundException("У занятия нет изображения")
    }

    @Throws(BadRequestException::class)
    fun addLessonFile(lessonId: UUID, file: MultipartFile) {
        val count = fileRepository.count()
        val validCount = maxCount.toLong()
        if(count >= validCount) throw BadRequestException("Максимальное количество файлов")
        val fileEntity = fileStoreService.saveFile(file, lessonId, LESSON_FILE)
        fileRepository.save(fileEntity)
        lessonsFilesService.addFileToLesson(lessonId, fileEntity.id)
    }

    fun existsFileById(id: UUID) = fileRepository.existsById(id)

    fun getPage(model: FileRequestPageModel): PageResponseModel<FileModel> {
        if (model.lessonId != null) {
            val lessonsFiles = lessonsFilesService.getLessonsFilesByLessonId(model.lessonId)
            model.ids = lessonsFiles.map { it.fileId }.toMutableSet()
            val files = fileRepository.findPage(model).map { it.toModel() }
            files.forEach { file ->
                run {
                    val lessonId = lessonsFiles.find { lessonFile -> lessonFile.fileId == file.id }
                    if (lessonId != null)
                        file.url = "/api/files/lessons?lessonId=${lessonId.lessonId}&fileId=${lessonId.fileId}"
                }
            }
            return files
        }
        throw BadRequestException("Не задана фильтрация")
    }

    fun deleteLessonFile(lessonId: UUID, fileId: UUID) {
        val file = fileRepository.findByIdOrNull(fileId)
        if(file != null) {
            fileStoreService.deleteLessonFile(lessonId, file.toModel())
            lessonsFilesService.deleteLessonsFiles(lessonId, fileId)
            fileRepository.deleteById(fileId)
        }
    }
}

private fun FileEntity.toModel() = FileModel(id, name, type, "", contentType, uploadingDateTime, lastUpdateDateTime)
