package com.lgorev.ksuonlineeducation.repository.coursereview

import org.springframework.data.jpa.repository.Query
import org.springframework.data.repository.CrudRepository
import org.springframework.data.repository.query.Param
import java.util.*

interface CourseReviewRepository : CrudRepository<CourseReviewEntity, CourseReviewId> {
    @Query("SELECT new CourseRatingEntity(e.courseReviewId.courseId, CASE WHEN COUNT(e) > 0 THEN (AVG(e.rating)) ELSE 0.0 END, COUNT(e)) FROM CourseReviewEntity e WHERE e.courseReviewId.courseId = :courseId GROUP BY e.courseReviewId.courseId")
    fun findAllByCourseId(@Param("courseId") courseId: UUID): CourseRatingEntity?

    @Query("SELECT new CourseRatingEntity(e.courseReviewId.courseId, CASE WHEN COUNT(e) > 0 THEN (AVG(e.rating)) ELSE 0.0 END, COUNT(e)) FROM CourseReviewEntity e WHERE e.courseReviewId.courseId in :courseIds GROUP BY e.courseReviewId.courseId")
    fun findAllByCourseIds(@Param("courseIds") courseIds: MutableSet<UUID>): MutableSet<CourseRatingEntity>
}