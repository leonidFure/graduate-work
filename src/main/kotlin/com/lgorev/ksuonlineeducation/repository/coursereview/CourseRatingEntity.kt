package com.lgorev.ksuonlineeducation.repository.coursereview

import java.util.*
import javax.persistence.Entity
import javax.persistence.Id

@Entity
data class CourseRatingEntity(
        @Id
        val courseId: UUID,
        val rating: Double,
        val count: Long
)