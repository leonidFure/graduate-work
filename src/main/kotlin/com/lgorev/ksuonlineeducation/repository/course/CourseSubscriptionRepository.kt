package com.lgorev.ksuonlineeducation.repository.course

import org.springframework.data.jpa.repository.Query
import org.springframework.data.repository.CrudRepository
import org.springframework.data.repository.query.Param
import java.util.*

interface CourseSubscriptionRepository : CrudRepository<CourseSubscriptionEntity, CourseSubscriptionId> {
    @Query("SELECT e FROM CourseSubscriptionEntity e WHERE e.id.userId = :userId")
    fun findByUserId(@Param("userId") userId: UUID): MutableSet<CourseSubscriptionEntity>
}

