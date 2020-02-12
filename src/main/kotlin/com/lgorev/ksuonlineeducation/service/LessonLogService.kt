package com.lgorev.ksuonlineeducation.service

import com.lgorev.ksuonlineeducation.domain.lesson.LessonLogModel
import com.lgorev.ksuonlineeducation.domain.lesson.LessonLogPageRequestModel
import com.lgorev.ksuonlineeducation.repository.lesson.LessonLogEntity
import com.lgorev.ksuonlineeducation.repository.lesson.LessonLogRepository
import com.lgorev.ksuonlineeducation.repository.lesson.LessonRepository
import javassist.NotFoundException
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
@Transactional
class LessonLogService(private val lessonLogRepository: LessonLogRepository,
                       private val lessonRepository: LessonRepository) {

    @Throws(NotFoundException::class)
    fun addLessonLog(model: LessonLogModel): LessonLogModel {
        if (lessonRepository.existsById(model.lessonId))
            throw NotFoundException("Занятие не найдено")
        return lessonLogRepository.save(model.toEntity()).toModel()
    }

    fun getLessonLogPage(model: LessonLogPageRequestModel) =
            lessonLogRepository.getPage(model).map { it.toModel() }
}

private fun LessonLogModel.toEntity() =
        LessonLogEntity(lessonId = lessonId, datetime = datetime, oldStatus = oldStatus, newStatus = newStatus)

private fun LessonLogEntity.toModel() = LessonLogModel(lessonId, datetime, oldStatus, newStatus)