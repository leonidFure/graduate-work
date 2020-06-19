package com.lgorev.ksuonlineeducation.service

import com.lgorev.ksuonlineeducation.repository.lesson.LessonsThemesRepository
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import java.util.*

@Service
@Transactional
class LessonsThemesService (private val lessonsThemesRepository: LessonsThemesRepository) {
    fun getLessonsThemesByLessonIds(ids: MutableSet<UUID>) = lessonsThemesRepository.findByLessonIds(ids)
    fun getLessonsThemesByThemeIds(ids: MutableSet<UUID>) = lessonsThemesRepository.findByThemeIds(ids)
    fun getLessonsThemesByLessonId(id: UUID) = lessonsThemesRepository.findAllByLessonsThemesIdLessonId(id)
}