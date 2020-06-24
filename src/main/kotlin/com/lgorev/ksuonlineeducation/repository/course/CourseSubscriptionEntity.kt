package com.lgorev.ksuonlineeducation.repository.course

import java.io.Serializable
import java.util.*
import javax.persistence.*

@Entity
@Table(name = "course_subscription")
data class CourseSubscriptionEntity(
        @EmbeddedId
        val id: CourseSubscriptionId
)

@Embeddable
data class CourseSubscriptionId(
        @Column(name = "course_id")
        val courseId: UUID,
        @Column(name = "user_id")
        val userId: UUID?
) : Serializable
