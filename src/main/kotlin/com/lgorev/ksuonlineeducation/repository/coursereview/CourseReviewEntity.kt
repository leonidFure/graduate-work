package com.lgorev.ksuonlineeducation.repository.coursereview

import java.io.Serializable
import java.util.*
import javax.persistence.*

@Entity
@Table(name = "course_review")
data class CourseReviewEntity(
        @EmbeddedId
        val courseReviewId: CourseReviewId,
        @Column(name = "rating")
        val rating: Int,
        @Column(name = "comment_head")
        val commentHead: String,
        @Column(name = "comment_body")
        val commentBody: String
)

@Embeddable
data class CourseReviewId(
        @Column(name = "course_id")
        val courseId: UUID,
        @Column(name = "user_id")
        val userId: UUID?
) : Serializable
