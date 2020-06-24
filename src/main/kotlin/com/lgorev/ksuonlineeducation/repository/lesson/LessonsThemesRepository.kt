package com.lgorev.ksuonlineeducation.repository.lesson

import org.springframework.data.jpa.repository.Query
import org.springframework.data.repository.CrudRepository
import org.springframework.data.repository.query.Param
import org.springframework.stereotype.Repository
import java.util.*

@Repository
interface LessonsThemesRepository : CrudRepository<LessonsThemesEntity, LessonsThemesId> {
    @Query("SELECT e FROM LessonsThemesEntity e WHERE e.lessonsThemesId.themesId in :themeIds")
    fun findByThemeIds(@Param("themeIds") ids: MutableSet<UUID>): MutableSet<LessonsThemesEntity>

    @Query("SELECT e FROM LessonsThemesEntity e WHERE e.lessonsThemesId.lessonId in :lessonIds")
    fun findByLessonIds(@Param("lessonIds") ids: MutableSet<UUID>): MutableSet<LessonsThemesEntity>

    fun findAllByLessonsThemesIdLessonId(id: UUID): MutableSet<LessonsThemesEntity>
}