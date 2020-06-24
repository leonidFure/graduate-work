package com.lgorev.ksuonlineeducation.repository.lesson

import org.springframework.data.repository.PagingAndSortingRepository
import org.springframework.stereotype.Repository
import java.time.LocalDate
import java.util.*

@Repository
interface LessonRepository : PagingAndSortingRepository<LessonEntity, UUID>, LessonPagingRepository {
    fun deleteAllByCourseId(courseId: UUID)
    fun findAllByCourseIdInAndDateBetween(courseIds: MutableSet<UUID>, fromDate: LocalDate, toDate: LocalDate): MutableList<LessonEntity>
    fun findAllByCourseId(courseId: UUID): MutableList<LessonEntity>
}