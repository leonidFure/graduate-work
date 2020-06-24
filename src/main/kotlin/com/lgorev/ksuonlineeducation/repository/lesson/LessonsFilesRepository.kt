package com.lgorev.ksuonlineeducation.repository.lesson

import org.springframework.data.repository.CrudRepository
import org.springframework.stereotype.Repository
import java.util.*

@Repository
interface LessonsFilesRepository : CrudRepository<LessonsFilesEntity, LessonsFilesId> {
    fun findAllByLessonsFilesIdLessonId(lessonId: UUID): MutableSet<LessonsFilesEntity>
}