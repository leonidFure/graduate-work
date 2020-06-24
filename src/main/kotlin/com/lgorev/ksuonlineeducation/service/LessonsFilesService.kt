package com.lgorev.ksuonlineeducation.service

import com.lgorev.ksuonlineeducation.exception.BadRequestException
import com.lgorev.ksuonlineeducation.exception.NotFoundException
import com.lgorev.ksuonlineeducation.repository.lesson.LessonsFilesEntity
import com.lgorev.ksuonlineeducation.repository.lesson.LessonsFilesId
import com.lgorev.ksuonlineeducation.repository.lesson.LessonsFilesRepository
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.data.repository.findByIdOrNull
import org.springframework.stereotype.Service
import java.util.*

@Service
class LessonsFilesService(private val lessonsFilesRepository: LessonsFilesRepository) {

    @Autowired
    private lateinit var lessonService: LessonService

    @Autowired
    private lateinit var fileService: FileService

    fun addFileToLesson(lessonId: UUID, fileId: UUID) {
        if (!lessonService.existsLessonById(lessonId))
            throw BadRequestException("Занятие не найдено")
        if(!fileService.existsFileById(fileId))
            throw NotFoundException("Файл не найден")
        lessonsFilesRepository.save(LessonsFilesEntity(LessonsFilesId(lessonId, fileId)))
    }

    fun getLessonsFilesByLessonId(lessonId: UUID) = lessonsFilesRepository.findAllByLessonsFilesIdLessonId(lessonId).map { it.toModel() }

    fun getLessonsFilesById(lessonId: UUID, fileId: UUID): LessonsFilesModel {
        lessonsFilesRepository.findByIdOrNull(LessonsFilesId(lessonId, fileId))?.let {
            return it.toModel()
        }
        throw NotFoundException("Файл не найден")
    }

    fun deleteLessonsFiles(lessonId: UUID, fileId: UUID) {
        lessonsFilesRepository.deleteById(LessonsFilesId(lessonId, fileId))
    }
}

private fun LessonsFilesEntity.toModel() = LessonsFilesModel(lessonsFilesId.lessonId, lessonsFilesId.fileId)

data class LessonsFilesModel(val lessonId: UUID, val fileId: UUID)