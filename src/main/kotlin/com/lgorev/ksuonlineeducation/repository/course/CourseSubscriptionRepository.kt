package com.lgorev.ksuonlineeducation.repository.course

import org.springframework.data.jpa.repository.Query
import org.springframework.data.repository.CrudRepository
import org.springframework.data.repository.query.Param
import org.springframework.stereotype.Repository
import java.util.*

@Repository
interface CourseSubscriptionRepository : CrudRepository<CourseSubscriptionEntity, CourseSubscriptionId> {
    @Query("SELECT e FROM CourseSubscriptionEntity e WHERE e.id.userId = :userId")
    fun findListByUserId(@Param("userId") userId: UUID?): MutableSet<CourseSubscriptionEntity>

    @Query("SELECT e FROM CourseSubscriptionEntity e WHERE e.id.userId = :userId and e.id.courseId = :courseId")
    fun findByUserId(@Param("userId") userId: UUID?,@Param("courseId") courseId: UUID): CourseSubscriptionEntity?

    @Query("SELECT e FROM CourseSubscriptionEntity e WHERE e.id.courseId = :courseId ")
    fun findByCourseId(@Param("courseId") courseId: UUID): MutableSet<CourseSubscriptionEntity>

    @Query("SELECT e FROM CourseSubscriptionEntity e WHERE e.id.courseId in :courseIds")
    fun findByCourseIds(@Param("courseIds") courseIds: MutableSet<UUID>): MutableSet<CourseSubscriptionEntity>

}

